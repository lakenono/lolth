package lolth.babytree.ask;

import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.babytree.ask.bean.AnswerBean;
import lolth.babytree.ask.bean.AskBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BabytreeAskAnswersFetch {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws Exception {
		while (true) {
			List<String> urls = GlobalComponents.db.getRunner().query("select url from data_babytree_ask where status is null and date > '2014-10-00'", new ColumnListHandler<String>());
			for (String url : urls) {
				this.log.info("begin.. {}", url);

				try {
					this.process(url);
				} catch (Exception e) {
					this.log.error("", e);
				}
				Thread.sleep(2000);
			}
			Thread.sleep(3000000);
		}
	}

	public void process(String url) throws Exception {
		// ---问题信息补完
		AskBean ask = new AskBean();

		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		// url
		ask.setUrl(url);

		if (document.select("div.qa-related div.qa-contributor ul li a.color-blue span").size() != 0) {
			// questioner 提问者
			String questioner = document.select("div.qa-related div.qa-contributor ul li a.color-blue span").first().text();
			ask.setQuestioner(questioner);

			// questionerUrl 提问者url
			String questionerUrl = "http://www.babytree.com" + document.select("div.qa-related div.qa-contributor ul li a.color-blue").attr("href");
			ask.setQuestionerUrl(questionerUrl);
		} else {
			ask.setQuestioner("匿名提问");
			ask.setQuestionerUrl("none");
		}

		// babyStatus 宝宝状态
		if (document.select("li[itemprop=status] a").size() == 0) {
			ask.setBabyStatus("还没有宝宝");
		} else {
			String babyStatus = document.select("li[itemprop=status] a").first().text();
			ask.setBabyStatus(babyStatus);
		}

		// 回答数
		String answerNumber = document.select("span[itemprop=reply_count]").first().text();
		ask.setAnswerNumber(answerNumber);

		// 浏览数
		String views = document.select("span[itemprop=view_count]").first().text();
		ask.setViews(views);

		// title
		String title = document.select("h1[itemprop=title]").first().text();
		ask.setTitle(title);

		this.log.info(ask.toString());
		ask.update();

		// ---回答信息

		List<AnswerBean> answerBeans = new LinkedList<AnswerBean>();

		// 最佳答案
		if (document.select("div#qa-answer-best").size() != 0) {
			AnswerBean bean = new AnswerBean();

			// url
			bean.setUrl(url);

			// floor
			bean.setFloor("b1");

			// author
			String author = document.select("div#qa-answer-best").first().select("span[itemprop=accountName]").first().text();
			bean.setAuthor(author);

			// author url
			String authorUrl = "http://www.babytree.com" + document.select("div#qa-answer-best").select("p.user-avatar a").first().attr("href");
			bean.setAuthorUrl(authorUrl);

			// baby status
			Elements scripts = document.select("div#qa-answer-best script");
			for (Element element : scripts) {
				if (StringUtils.contains(element.html(), "宝宝年龄")) {
					String babyStatus = StringUtils.substringBetween(element.html(), ">", "<");
					bean.setBabyStatus(babyStatus);
				}
			}
			if (bean.getBabyStatus() == null) {
				bean.setBabyStatus("还没有宝宝");
			}

			// post time
			String postTime = document.select("div#qa-answer-best").select("li.timestamp abbr").first().text();
			bean.setPostTime(postTime);

			answerBeans.add(bean);
			this.log.info(bean.toString());
		}

		// 其他回答
		if (document.select("li.answer-item").size() != 0) {
			Elements elements = document.select("li.answer-item");

			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				AnswerBean bean = new AnswerBean();

				// url
				bean.setUrl(url);

				// floor
				bean.setFloor("o" + (i + 1));

				// author
				String author = element.select("span[itemprop=accountName]").text();
				bean.setAuthor(author);

				// author url
				String authorUrl = "http://www.babytree.com" + element.select("li.username a").attr("href");
				bean.setAuthorUrl(authorUrl);

				// baby status
				String babyStatusText = element.select("li.username span script").text();
				String babyStatus = StringUtils.substringBetween(babyStatusText, "'", "'");
				if (babyStatus == null) {
					bean.setBabyStatus("还没有宝宝");
				} else {
					bean.setBabyStatus(babyStatus);
				}

				// post time
				String postTime = element.select("li.timestamp abbr").attr("title");
				bean.setPostTime(postTime);

				this.log.info(bean.toString());

				answerBeans.add(bean);
			}

		}

		for (AnswerBean answerBean : answerBeans) {
			 answerBean.persist();
		}

		// 设置状态位
		GlobalComponents.db.getRunner().update("update data_babytree_ask set status='answer_success' where url=?", url);

	}

	public static void main(String[] args) throws Exception {
		new BabytreeAskAnswersFetch().run();
	}
}
