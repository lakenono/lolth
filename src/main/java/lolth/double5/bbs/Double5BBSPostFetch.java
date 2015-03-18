package lolth.double5.bbs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.double5.bbs.bean.Double5BBSPostBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 55BBS爬取列表
 * 
 * @author shi.lei
 *
 */
public class Double5BBSPostFetch {
	private static final Logger log = LoggerFactory
			.getLogger(Double5BBSPostFetch.class);

	public static void main(String[] args) throws Exception {
		new Double5BBSPostFetch().run();
	}

	private void run() throws Exception {
		List<String> todo = getNeedFetchUrl();
		while (true) {
			if (todo.isEmpty()) {
				break;
			}

			for (String url : todo) {
				try {
					Double5BBSPostBean bean = parsePage(url);
					bean.update();
					log.debug("{} success", url);
				} catch (HttpStatusException e1) {
					log.error("{} error : {}", url, e1);
				} catch (Exception e) {
					log.error("{} error : {}", url, e);
				}
			}

		}
		log.info("55BBS post fetch finish!");
	}

	private List<String> getNeedFetchUrl() throws SQLException {
		List<String> urls = GlobalComponents.db.getRunner().query(
				"select url from "
						+ BaseBean.getTableName(Double5BBSPostBean.class)
						+ " where text is null limit 1000",
				new ColumnListHandler<String>());

		return urls;
	}

	private Double5BBSPostBean parsePage(String url) throws IOException,
			InterruptedException {
		Double5BBSPostBean bean = new Double5BBSPostBean();
		Document doc = GlobalComponents.fetcher.document(url);

		Elements mainboxs = doc.select("div#mainbox.mainbox.viewthread");
		if (mainboxs.size() > 0) {
			// 回复查看
			String viewAndReplay = mainboxs.first().select("span.l_hf span.hf")
					.text();

			if (StringUtils.isNotBlank(viewAndReplay)) {
				bean.setReplys(StringUtils.substringBetween(viewAndReplay,
						"回复：", "查看").trim());
				
				bean.setViews(StringUtils.substringBetween(viewAndReplay,
						"查看：", "】"));
			}
			// 用户信息
			Elements authorElement = mainboxs.first().select(
					"td.postauthor cite a");
			bean.setAuthor(authorElement.text());
			bean.setAuthorUrl(authorElement.attr("href"));

			Element authorInfoElement = mainboxs.first()
					.select("td.postauthor").first();

			if (authorInfoElement.getElementsMatchingOwnText("帖子").size() != 0) {
				String posts = authorInfoElement
						.getElementsMatchingOwnText("帖子").first()
						.nextElementSibling().text();
				bean.setPosts(posts);
			}

			if (authorInfoElement.getElementsMatchingOwnText("精华").size() != 0) {
				String classicPosts = authorInfoElement
						.getElementsMatchingOwnText("精华").first()
						.nextElementSibling().text();
				bean.setClassicPosts(classicPosts);
			}

			if (authorInfoElement.getElementsMatchingOwnText("城市").size() != 0) {
				String region = authorInfoElement
						.getElementsMatchingOwnText("城市").first()
						.nextElementSibling().text();
				bean.setRegion(region);
			}

			if (authorInfoElement.getElementsMatchingOwnText("注册时间").size() != 0) {
				String regTime = authorInfoElement
						.getElementsMatchingOwnText("注册时间").first()
						.nextElementSibling().text();
				bean.setRegTime(regTime);
			}

			Element contentElement = mainboxs.first().select("td.postcontent")
					.first();
			if (contentElement.getElementsMatchingOwnText("发表于").size() != 0) {
				String postTime = contentElement
						.getElementsMatchingOwnText("发表于").first().ownText();
				postTime = StringUtils.substringAfter(postTime, "发表于");
				bean.setPostTime(postTime);
			}

			// 内容信息
			String content = "删帖";
			if (contentElement.select("div.t_msgfont").first() != null) {
				content = contentElement.select("div.t_msgfont").first().text();
			}

			bean.setText(content);
		} else {
			bean.setText("page can not parse");
		}
		bean.setUrl(url);
		return bean;
	}
}
