package lolthx.weibo.fetch;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboIdUtils;
import lolth.weibo.utils.WeiboTimeUtils;
import lolthx.weibo.task.WeiboSearchTask;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

/**
 * 微博新框架爬取，爬取过程中发送微博用户task
 * @author yanghp
 *
 */
@Slf4j
public class WeiboSearchFetch extends DistributedParser {
	
	private int sleep = 15000;
	private final String WEIBO_USER_URL_TEMPLATE = "http://weibo.cn/{0}";
	private final String WEIBO_USER_INFO_URL_TEMPLAGE = "http://weibo.cn/{0}/info";
	public static final String USER_QUEUE_NAME = "weibo_user_name_queue";

	@Override
	public String getQueueName() {
		return WeiboSearchTask.WEIBO_SEARCH_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			log.info("weibo search result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
		List<WeiboBean> beans = parse(doc,task);
		for (WeiboBean b : beans) {
			try {
				b.persistOnNotExist();
				//发送微博id爬取任务
//				bulidWeiboUserTask(b.getUserid(),task.getProjectName());
			} catch (Exception e) {
				log.error("{} persist error ", b, e);
			}
		}
		beans.clear();
	}
	
	public void bulidWeiboUserTask(String id, String keyword) {
		try {
			String uid = id;
			if (!StringUtils.isNumeric(uid)) {
				Thread.sleep(sleep);
				uid = getUid(id);
			}

			if (Strings.isNullOrEmpty(uid)) {
				throw new RuntimeException("uid can not get id : " + id);
			}

			Task t = new Task();
			t.setProjectName(keyword);
			t.setQueueName(USER_QUEUE_NAME);
			t.setUrl(buildUserInfoUrl(uid));
			t.setExtra(id+","+uid);
			Queue.push(t);
			
		} catch (Exception e) {
			log.error("{} get uid error :", id, e);
		}

		
	}
	
	private String buildUserInfoUrl(String uid) {
		return MessageFormat.format(WEIBO_USER_INFO_URL_TEMPLAGE, uid);
	}
	
	private String getUid(String id) throws IOException, InterruptedException, TException {
		String uid = null;
		String userUrl = buildUserUrl(id);
		String cookies = GlobalComponents.authService.getCookies(getCookieDomain());
//		String cookies = "_T_WM=381052f5df15a47db4b6c216d9fa6b8e; SUB=_2A254qy2qDeSRGeNL7FQS9inIyj-IHXVYV7PirDV6PUJbrdANLVPhkW1Mx5Pwf3qtPcXl9Bixn6Md_eO72Q..; gsid_CTandWM=4uDre42b1a7eMv2kMnqKPnoFp6F";
		String html = GlobalComponents.jsoupFetcher.fetch(userUrl, cookies);
		Document doc = Jsoup.parse(html);
		Elements imgElements = doc.select("img.por");
		if (imgElements.size() > 0) {
			uid = imgElements.first().parent().attr("href");
			uid = StringUtils.substringBetween(uid, "/", "/");
		}
		return uid;
	}
	
	

	private String buildUserUrl(String id) {
		return MessageFormat.format(WEIBO_USER_URL_TEMPLATE, id);
	}

	public static void main(String[] args) {
		new WeiboSearchFetch().run();
	}
	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}
	
	public List<WeiboBean> parse(Document document,Task task) throws IOException, ParseException {
		Elements elements = document.select("div.c[id]");

		LocalDateTime now = LocalDateTime.now();
		String fetchTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

		List<WeiboBean> weiboBeans = new LinkedList<WeiboBean>();

		for (Element element : elements) {
			String html = element.html();

			WeiboBean bean = new WeiboBean();

			// mid
			String mid = StringUtils.substringAfter(element.attr("id"), "M_");
			bean.setMid(mid);

			// id
			bean.setId(WeiboIdUtils.toId(mid));

			// 发布时间
			String postTimeText = element.select("span.ct").text();
			postTimeText = StringUtils.substringBefore(postTimeText, "来自");
			postTimeText = WeiboTimeUtils.getNormalTime(postTimeText, now);
			bean.setPostTime(postTimeText);

			// username
			String username = element.select("a.nk[href]").first().text();
			bean.setUsername(username);

			// userurl
			String userurl = element.select("a.nk[href]").first().attr("href");
			bean.setUserurl(userurl);

			// userid
			String userid = StringUtils.substringAfterLast(userurl, "/");
			bean.setUserid(userid);

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
			if (!StringUtils.contains(html, "原文转发")) {
				// text
				String text = element.select("span.ctt").text();
				bean.setText(StringUtils.substringAfter(text, ":"));
			} else {
				String pweibourl = element.select("a.cc").first().attr("href");
				bean.setPweibourl(pweibourl);

				String pmid = StringUtils.substringBetween(pweibourl, "comment/", "?");
				bean.setPmid(pmid);

				String pid = WeiboIdUtils.toId(pmid);
				bean.setPid(pid);

				String text = StringUtils.substringBetween(element.select("div").last().text(), "转发理由:", "赞[");
				bean.setText(text);
			}

			WeiboContentSpliter.spliteContent(bean);

			bean.setFetchTime(fetchTime);
			bean.setKeyword(task.getExtra());
			bean.setProjectName(task.getProjectName());
			weiboBeans.add(bean);

			log.debug(bean.toString());
		}
		return weiboBeans;
	}
}
