package lolth.weibo.cn;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lolth.weibo.task.WeiboUserMainPageTaskProducer;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboIdUtils;
import lolth.weibo.utils.WeiboTimeUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboUserMainPageFetch extends FetchTaskHandler {

	private static final String USER_NEED_FETCH_MAIN_PAGE = "user_need_fetch_main_page";
	
	private String timeRange = null;

	public WeiboUserMainPageFetch(String taskQueueName,String timeRange) {
		super(taskQueueName);
		this.timeRange = timeRange;
	}

	public static void main(String[] args) throws Exception {
		String taskQueueName = WeiboUserMainPageTaskProducer.USER_MAIN_PAGE;
		WeiboUserMainPageFetch fetch = new WeiboUserMainPageFetch(taskQueueName,"2014-10-01 00:00:00");
		fetch.setSleep(15000);
		fetch.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		// 如果任务已经处理完成
//		if (isBatchFinish(task.getExtra())) {
//			return;
//		}
		
//		Thread.sleep(15000);
		Document document = WeiboFetcher.cnFetcher.fetch(task.getUrl());

		List<WeiboBean> beans = parse(document, task.getExtra());

		for (WeiboBean b : beans) {
			try {
				b.setUserurl(StringUtils.substringBefore(task.getUrl(), "?"));
				b.setKeyword(task.getName());
				b.persistOnNotExist();
			} catch (Exception e) {
				log.error("{} persist error ", b, e);
			}
		}
	}

	public List<WeiboBean> parse(Document document, String uid) throws IOException, ParseException {
		List<WeiboBean> weiboBeans = new LinkedList<WeiboBean>();
		
		String username = "";
		Elements user = document.select("div.ut");
		if (user.size() > 0) {
			username = user.first().ownText();
		}

		Elements elements = document.select("div.c[id]");

		LocalDateTime now = LocalDateTime.now();
		String fetchTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

		for (Element element : elements) {
			String html = element.html();

			WeiboBean bean = new WeiboBean();

			bean.setUserid(uid);

			// mid
			String mid = StringUtils.substringAfter(element.attr("id"), "M_");
			bean.setMid(mid);

			// id
			bean.setId(WeiboIdUtils.toId(mid));

			// 发布时间
			String postTimeText = element.select("span.ct").text();
			postTimeText = StringUtils.substringBefore(postTimeText, "来自");
			postTimeText = StringUtils.stripEnd(postTimeText, " ");
			postTimeText = WeiboTimeUtils.getNormalTime(postTimeText, now);

			// 超过需要处理的范围
//			if (WeiboTimeUtils.isBefore(postTimeText, timeRange)) {
//				setBatchFinish(uid);
//				return weiboBeans;
//			}
			
			bean.setPostTime(postTimeText);

			// weibourl
			bean.setWeibourl("http://weibo.cn/comment/" + mid);

			// source
			String source = element.select("span.ct").text();
			source = StringUtils.substringAfter(source, "来自");
			bean.setSource(source);

			// 赞
			Element likesElement = element.getElementsMatchingOwnText("赞\\[").last();
			String likes = StringUtils.substringBetween(likesElement.text(), "赞[", "]");
			bean.setLikes(likes);

			// 转发
			String forwards = StringUtils.substringBetween(html, ">转发[", "]");
			bean.setReposts(forwards);

			// 评论
			String comments = StringUtils.substringBetween(html, ">评论[", "]");
			bean.setComments(comments);

			// 原创
			if (StringUtils.contains(html, "转发理由")) {
				// text
				String pweibourl = element.select("a.cc").first().attr("href");
				bean.setPweibourl(pweibourl);

				String pmid = StringUtils.substringBetween(pweibourl, "comment/", "?");
				bean.setPmid(pmid);

				String pid = WeiboIdUtils.toId(pmid);
				bean.setPid(pid);

				String text = StringUtils.substringBetween(element.select("div").last().text(), "转发理由:", "赞[");
				bean.setText(text);
			} else {
				String text = element.select("span.ctt").text();
				bean.setText(text);
			}

			WeiboContentSpliter.spliteContent(bean);
			bean.setUsername(username);
			bean.setFetchTime(fetchTime);
			weiboBeans.add(bean);

			log.debug(bean.toString());
		}
		return weiboBeans;
	}

//	private boolean isBatchFinish(String uid) {
//		return GlobalComponents.jedis.hexists(USER_NEED_FETCH_MAIN_PAGE, uid);
//	}
//
//	private void setBatchFinish(String uid) {
//		GlobalComponents.jedis.hset(USER_NEED_FETCH_MAIN_PAGE, uid, "finish");
//	}
}
