package lolth.pacuto.bbs;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.selenium.SeleniumHttpFetcher;
import lakenono.fetch.adv.selenium.SeleniumHttpFetcher.SeleniumWebAction;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lolth.pacuto.bbs.bean.PacutoBBSPostBean;
import lolth.pacuto.bbs.bean.PacutoBBSUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

/**
 * 解析详情页帖子，用户，入库
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class PacutoBBSDetailTaskFetch extends FetchTaskHandler {

	public PacutoBBSDetailTaskFetch() {
		super(PacutoBBSDetailTaskProducer.PACUTO_BBS_POST_DETAIL);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PacutoBBSDetailTaskFetch fetch = new PacutoBBSDetailTaskFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	protected void parsePage(Document doc, FetchTask task) throws Exception {
		// TODO Auto-generated method stub
		PacutoBBSPostBean post = null;
		PacutoBBSUserBean user = null;

		Elements postList = doc.select("div.psot_wrap_first");

		if (!postList.isEmpty()) {

			// user对象
			user = new PacutoBBSUserBean();

			// userId name url
			Elements userInfoElements = postList.select("td.post_left p.uName>a");
			String url = userInfoElements.first().absUrl("href");
			user.setUrl(url);
			String userId = StringUtils.substringBetween(url, "http://my.pcauto.com.cn/", "/forum/");
			user.setId(userId);
			String name = userInfoElements.first().text();
			user.setName(name);

			// city
			Elements areaElements = doc.select("td.post_left div.user_info a.dblue");
			String city = areaElements.first().text();
			user.setCity(city);

			// post对象
			post = new PacutoBBSPostBean();

			// content
			Elements contentElements = postList.select("td.post_right div.post_main");
			String content = contentElements.first().text();
			post.setContent(content);

			// postTime
			Elements postTimeElements = postList.select("td.post_right div.post_time");
			String postTime = StringUtils.substringAfter(postTimeElements.first().text(), "发表于");
			post.setPostTime(postTime);

			// views replys
			Elements overView = doc.select("td.post_left p.overView");
			String viewReply = overView.first().text();
			String views = StringUtils.substringBetween(viewReply, "查看：", " ");
			String replys = StringUtils.substringAfter(viewReply, "回复：");
			post.setViews(views);
			post.setReplys(replys);

			// title
			Elements titleElements = doc.select("td.post_right div.post_r_tit>h1");
			String title = titleElements.first().text();
			post.setTitle(title);

			// id
			String postUrl = task.getUrl();
			String postId = StringUtils.substringBetween(postUrl, "http://bbs.pcauto.com.cn/topic-", ".html");
			post.setId(postId);
			post.setAuthorId(userId);

		}

		if (post != null) {
			String id = StringUtils.substringBetween(task.getUrl(), "-", ".html");
			post.setKeyword(task.getName());
			post.setId(id);
			post.setUrl(task.getUrl());

			String extra[] = StringUtils.splitByWholeSeparator(task.getExtra(), ",");
			post.setForumId(extra[0]);
			post.setType(extra[1]);

			if (user != null) {
				user.persistOnNotExist();
				post.setAuthorId(user.getId());
			}

			post.persistOnNotExist();
		}

		log.debug("Parse post:{} ", post);
		log.debug("Parse user:{} ", user);

	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		HttpFetcher fetcher = new SeleniumHttpFetcher(new SeleniumWebAction() {

			@Override
			public boolean doWeb(WebDriver webDriver, HttpRequest req, HttpResponse resp) {
				return true;
			}
		});
		byte[] content = fetcher.run(task.getUrl());

		Document doc = Jsoup.parse(new String(content, "UTF-8"));
		parsePage(doc, task);
		fetcher.close();

		// Document doc = GlobalComponents.fetcher.document(task.getUrl());
		// parsePage(doc, task);
		// Thread.sleep(sleep);
	}

}
