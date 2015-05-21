package lolth.weibo.com.fetch.comment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lakenono.fetch.adv.utils.CookiesUtils;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.weibo.cn.WeiboUserTaskFetch.WeiboUserTaskProducer;
import lolth.weibo.com.fetch.comment.bean.CommentBean;
import lolth.weibo.task.WeiboUserConcernListTaskProducer;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboTimeUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

@Slf4j
public class CommentFetch extends PageParseFetchTaskHandler {
	private WeiboUserTaskProducer userTaskProducer = null;
	HttpFetcher fetcher = new HttpClientFetcher();

	public CommentFetch() {
		super(CommentTask.WEIBO_TEXT_COMMENT);
		userTaskProducer = new WeiboUserTaskProducer();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements elements = doc.select("div.list_li");
		if (elements.isEmpty()) {
			return;
		}
		List<CommentBean> commentBeans = new ArrayList<>();
		CommentBean commentBean = null;
		for (Element element : elements) {
			commentBean = new CommentBean();
			commentBean.setKeyword(task.getName());
			String cid = element.attr("comment_id");
			commentBean.setCommentId(cid);

			parseTextOrUser(element, commentBean);

			Elements selects = element.select("div.WB_func.clearfix > div.WB_handle.W_fr > ul > li");
			if (!selects.isEmpty()) {
				// 点赞
				String like = selects.get(2).text();
				commentBean.setLike(like);
			}
			// 时间
			selects = element.select("div.WB_from");
			if (!selects.isEmpty()) {
				String time = selects.text();
				String normalTime = WeiboTimeUtils.getNormalTime(time, LocalDateTime.now());
				commentBean.setPostTime(normalTime);
			}
			commentBeans.add(commentBean);

			log.debug(commentBean.toString());
		}
		if (commentBeans.isEmpty()) {
			return;
		}

		for (CommentBean comment : commentBeans) {
			try {
				comment.persistOnNotExist();
			} catch (Exception e) {

			}
			try {
				userTaskProducer.push(comment.getUserId(), task.getName());
			} catch (Exception e) {

			}
			try {
				new WeiboUserConcernListTaskProducer(comment.getUserId(), task.getName()).run();
			} catch (Exception e) {

			}
		}
	}

	private void parseTextOrUser(Element element, CommentBean commentBean) {
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

	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		String url = task.getUrl();
		if (StringUtils.isBlank(url)) {
			return;
		}
		FetchTask fetchTask = task.clone();
		parsePage(fetchTask);
		fetchTask = null;
		task = null;
	}

	private void parsePage(FetchTask fetchTask) throws Exception {
		HttpRequest req = new HttpRequest();
		req.setNeedContent(true);
		req.setUrl(fetchTask.getUrl() + System.currentTimeMillis());
		req.setCookies(CookiesUtils.getCookies(CommentTask.COOKIS));
		String json;
		try {
			json = new String(fetcher.run(req).getContent());
		} catch (Exception e) {
			log.error("解析json出错了！", e);
			return;
		}
		Object object = JSON.parseObject(json).getJSONObject("data").get("html");
		String html = object.toString();
		if (StringUtils.isBlank(html)) {
			return;
		}
		Document doc = Jsoup.parse(html);
		parsePage(doc, fetchTask);
	}

	public static void main(String[] args) {
		CommentFetch fetch = new CommentFetch();
		fetch.setSleep(15000);
		fetch.run();
	}
}
