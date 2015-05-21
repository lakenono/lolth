package lolth.weibo.com.fetch.repost;

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
import lolth.weibo.com.fetch.repost.bean.RepostBean;
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
public class CommentRepostFetch extends PageParseFetchTaskHandler {
	private WeiboUserTaskProducer userTaskProducer = null;

	HttpFetcher fetcher = new HttpClientFetcher();

	public CommentRepostFetch() {
		super(CommentRepostTask.WEIBO_TEXT_COMMENT_REPOST);
		userTaskProducer = new WeiboUserTaskProducer();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements elements = doc.select("div.list_li");
		if (elements.isEmpty()) {
			return;
		}
		List<RepostBean> repostBeans = new ArrayList<>();
		RepostBean repostBean = null;
		for (Element element : elements) {
			repostBean = new RepostBean();
			repostBean.setKeyword(task.getName());
			// 转发微博ID
			String mid = element.attr("mid");
			repostBean.setWeboId(mid);

			parseTextOrUser(element, repostBean);

			Elements selects = element.select("div.WB_func.clearfix > div.WB_handle.W_fr > ul > li");
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
			selects = element.select("div.WB_from > a");
			if (!selects.isEmpty()) {
				String time = selects.text();
				String normalTime = WeiboTimeUtils.getNormalTime(time, LocalDateTime.now());
				repostBean.setPostTime(normalTime);
			}

			log.debug(repostBean.toString());
			repostBeans.add(repostBean);
		}

		if (repostBeans.isEmpty()) {
			return;
		}
		for (RepostBean f : repostBeans) {
			try {
				f.persistOnNotExist();
			} catch (Exception e) {

			}
			try {
				userTaskProducer.push(f.getUserId(), task.getName());
			} catch (Exception e) {

			}
			try {
				new WeiboUserConcernListTaskProducer(f.getUserId(), task.getName()).run();
			} catch (Exception e) {

			}
		}

	}

	/*
	 * 解析正文和用户
	 */
	private void parseTextOrUser(Element element, RepostBean repostBean) {
		Elements selects = element.select("div.WB_text");
		if (!selects.isEmpty()) {
			repostBean.setText(selects.text());
			// 用户名
			Elements tmp = selects.select("a:nth-child(1)");
			if (!tmp.isEmpty()) {
				repostBean.setNick(tmp.first().text());
				String userId = tmp.first().attr("usercard");
				repostBean.setUserId(userId.substring(3, userId.length()));
			}
			String[] contents = WeiboContentSpliter.spliteContent(repostBean.getText());
			if (contents != null) {
				// 转发的微博正文
				repostBean.setForwardList(contents[0]);
				// at的用户
				repostBean.setAt(contents[1]);
				// 转发
				int index = contents[2].indexOf("：");
				if (index > -1) {
					repostBean.setMainText(contents[2].substring(index + 1, contents[2].length()));
				}
				// 话题
				repostBean.setTopic(contents[3]);
			}
			// 表情
			tmp = selects.select("img");
			if (!selects.isEmpty()) {
				String img = selects.attr("alt");
				repostBean.setFeelings(img);
			}
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
		req.setCookies(CookiesUtils.getCookies(CommentRepostTask.COOKIS));
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
		CommentRepostFetch fetch = new CommentRepostFetch();
		fetch.setSleep(15000);
		fetch.run();
	}

}
