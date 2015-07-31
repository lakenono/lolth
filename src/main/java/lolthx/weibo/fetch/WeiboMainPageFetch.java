package lolthx.weibo.fetch;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboIdUtils;
import lolth.weibo.utils.WeiboTimeUtils;
import lolthx.weibo.task.WeiboMainPageTask;
import lombok.extern.slf4j.Slf4j;
/**
 * 微博主页爬取
 * @author yanghp
 *
 */
@Slf4j
public class WeiboMainPageFetch extends DistributedParser{
	
	private WeiboSearchFetch weibo = new WeiboSearchFetch();
	@Override
	public String getQueueName() {
		return WeiboMainPageTask.MAIN_PAGE_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			log.info("weibo main page result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
		List<WeiboBean> beans = parse(doc, task.getExtra());

		for (WeiboBean b : beans) {
			try {
				b.setUserurl(StringUtils.substringBefore(task.getUrl(), "?"));
				b.setKeyword(task.getProjectName());
				b.persistOnNotExist();
				weibo.bulidWeiboUserTask(b.getUserid(), task.getProjectName());
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
}
