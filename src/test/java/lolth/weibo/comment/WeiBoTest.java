package lolth.weibo.comment;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lolth.weibo.com.fetch.comment.bean.CommentBean;
import lolth.weibo.com.fetch.repost.bean.RepostBean;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboTimeUtils;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WeiBoTest {

	@Test
	public void testWeiBoComment() throws IOException {
		String resp = ioToHtml();
		System.out.println(resp);

		JSONObject parseObject = JSON.parseObject(resp);
		parseObject = parseObject.getJSONObject("data");

		Object object2 = parseObject.getJSONObject("page").get("totalpage");
		System.out.println(object2);

		Object object = parseObject.get("html");
		String html = object.toString();
		// body > div > div > div:nth-child(1)
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("div.list_li");
		if (elements.isEmpty()) {
			return;
		}

		List<CommentBean> repostBeans = new ArrayList<>();
		CommentBean commentBean = null;

		for (Element element : elements) {
			commentBean = new CommentBean();
			String cid = element.attr("comment_id");
			commentBean.setCommentId(cid);

			Elements selects = element.select("div.WB_text");
			// 用户名
			Elements nick = selects.select("a:nth-child(1)");
			commentBean.setNick(nick.first().text());
			String userid = nick.first().attr("usercard");
			commentBean.setUserId(userid.substring(3, userid.length()));
			//
			commentBean.setText(selects.text());
			String[] spliteContents = WeiboContentSpliter.spliteContent(selects.text());
			if (spliteContents != null) {
				// 转发的微博正文
				commentBean.setForwardList(spliteContents[0]);
				// at的用户
				commentBean.setAt(spliteContents[1]);
				// 转发
				int index = spliteContents[2].indexOf("：");
				if (index > -1) {
					commentBean.setMainText(spliteContents[2].substring(index + 1, spliteContents[2].length()));
				}
				// 话题
				commentBean.setTopic(spliteContents[3]);
			}
			// 表情
			selects = selects.select("img");
			if (!selects.isEmpty()) {
				String img = selects.attr("alt");
				commentBean.setFeelings(img);
			}

			selects = element.select("div.WB_func.clearfix > div.WB_handle.W_fr > ul > li");
			if (!selects.isEmpty()) {
				// 点赞
				String like = selects.get(2).text();
				commentBean.setLike(like);
			}

			selects = element.select("div.WB_from");
			if (!selects.isEmpty()) {
				String time = selects.text();
				String normalTime = WeiboTimeUtils.getNormalTime(time, LocalDateTime.now());
				commentBean.setPostTime(normalTime);
			}
			System.out.println(commentBean.toString());
		}

	}

	@Test
	public void testWeiBoReposts() throws IOException {
		String resp = ioToHtml();
		System.out.println(resp);
		JSONObject parseObject = JSON.parseObject(resp);
		parseObject = parseObject.getJSONObject("data");

		// Object object2 = parseObject.getJSONObject("page").get("totalpage");
		// System.out.println(object2);

		Object object = parseObject.get("html");
		String html = object.toString();
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("div.list_li");
		if (elements.isEmpty()) {
			return;
		}
		List<RepostBean> repostBeans = new ArrayList<>();
		RepostBean repostBean = null;
		for (Element element : elements) {
			repostBean = new RepostBean();
			//
			String mid = element.attr("mid");
			repostBean.setWeboId(mid);
			//
			Elements selects = element.select("div.WB_text");
			// 用户名
			Elements nick = selects.select("a:nth-child(1)");
			repostBean.setNick(nick.first().text());
			String userid = nick.first().attr("usercard");
			repostBean.setUserId(userid.substring(3, userid.length()));
			//
			repostBean.setText(selects.text());
			String[] spliteContents = WeiboContentSpliter.spliteContent(selects.text());
			if (spliteContents != null) {
				// 转发的微博正文
				repostBean.setForwardList(spliteContents[0]);
				// at的用户
				repostBean.setAt(spliteContents[1]);
				// 转发
				int index = spliteContents[2].indexOf("：");
				if (index > -1) {
					repostBean.setMainText(spliteContents[2].substring(index + 1, spliteContents[2].length()));
				}
				// 话题
				repostBean.setTopic(spliteContents[3]);
			}
			// 表情
			selects = selects.select("img");
			if (!selects.isEmpty()) {
				String img = selects.attr("alt");
				repostBean.setFeelings(img);
			}
			//
			selects = element.select("div.WB_func.clearfix > div.WB_handle.W_fr > ul > li");
			if (!selects.isEmpty()) {
				// 二次转发数
				String reposts = selects.get(1).text();
				if (reposts.length() > 2) {
					repostBean.setReposts(reposts.substring(3, reposts.length()));
				} else {
					repostBean.setReposts("0");
				}
				// 点赞
				String like = selects.get(2).text();
				repostBean.setLike(like);
			}
			System.out.println(repostBean.toString());
			System.out.println("------------------------------------");
		}

	}

	private String ioToHtml() throws IOException {
		InputStream is = WeiBoTest.class.getResourceAsStream("test.html");
		return IOUtils.toString(is);
	}
}
