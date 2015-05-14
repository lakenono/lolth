package lolth.baidu.zhidao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.baidu.zhidao.bean.BaiduZhidaoQuestionBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaiduZhidaoDetailFetch extends PageParseFetchTaskHandler {

	public static final String BAIDU_ZHIDAO_USER = "baidu_zhidao_user";

	private static final String BAIDU_ZHIDAO_USER_URL_TEMPLATE = "http://www.baidu.com/p/{0}/detail";
	private FetchTaskProducer producer = null;

	public BaiduZhidaoDetailFetch() {
		super(BaiduZhidaoListFetch.BAIDU_ZHIDAO_DETAIL);
		producer = new FetchTaskProducer(BAIDU_ZHIDAO_USER);
	}

	public static void main(String[] args) throws Exception {
		BaiduZhidaoDetailFetch fetch = new BaiduZhidaoDetailFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		BaiduZhidaoQuestionBean bean = null;

		if (StringUtils.startsWith(task.getUrl(), "http://zhidao.baidu.")) {
			bean = parseZhidaoDetail(doc);
		}

		if (StringUtils.startsWith(task.getUrl(), "http://zuoye.baidu.")) {
			bean = parseZuoyeDetail(doc);
		}

		if (bean != null) {
			String id = StringUtils.substringAfterLast(task.getUrl(), "/");
			id = StringUtils.substringBefore(id, ".");

			bean.setId(id);
			bean.setUrl(task.getUrl());
			bean.setTaskName(task.getName());
			bean.setKeyword(task.getExtra());

			bean.persistOnNotExist();

			// 推送用户
			if (StringUtils.isNoneBlank(bean.getAskerId())) {
				producer.saveAndPushTask(buildUserTask(bean.getAskerId(), task));
			}

			// 推送用户
			if (StringUtils.isNoneBlank(bean.getAnswererId())) {
				producer.saveAndPushTask(buildUserTask(bean.getAnswererId(), task));
			}
		}
	}

	private BaiduZhidaoQuestionBean parseZhidaoDetail(Document doc) {
		// 问题
		Elements asks = doc.select("div.wgt-ask");

		if (asks.isEmpty()) {
			return null;
		}

		Element ask = asks.first();

		BaiduZhidaoQuestionBean bean = new BaiduZhidaoQuestionBean();

		// 标题
		Elements title = ask.select("span.ask-title");
		if (!title.isEmpty()) {
			bean.setAskTitle(title.first().text());
		}

		// 发表时间
		Elements askTime = ask.select("div#ask-info span.ask-time");
		if (!askTime.isEmpty()) {
			bean.setAskTime(askTime.first().text());
		}
		//悬赏积分
		Elements moneyAward = ask.getElementsMatchingOwnText("提问者悬赏：");
		if (!moneyAward.isEmpty()) {
			String moneyAwardText = moneyAward.first().text();
			moneyAwardText = StringUtils.substringAfter(moneyAwardText, "：");
			bean.setMoneyAward(moneyAwardText);
		}

		// 提问者
		Elements asker = ask.select("div#ask-info a.user-name");
		if (!asker.isEmpty()) {
			bean.setAskerId(asker.first().text());
		}

		// 來自
		Elements from = ask.getElementsMatchingOwnText("来自");
		if (!from.isEmpty()) {
			String fromText = from.first().text();
			fromText = StringUtils.substringAfter(fromText, "来自");
			bean.setFrom(fromText);
		}

		// 分類
		Elements type = ask.select("div#ask-info span.classinfo a");
		if (!type.isEmpty()) {
			bean.setType(type.first().text());
		}

		// 浏览次数
		Elements views = ask.select("div#ask-info span.browse-times");
		if (!views.isEmpty()) {
			String viewsText = views.first().text();
			viewsText = StringUtils.substringBetween(viewsText, "浏览", "次");
			bean.setViews(viewsText);
		}
		
		//内容
		Elements askContent = ask.select("pre.q-content");
		if (!askContent.isEmpty()) {
			bean.setAskContent(askContent.first().text());
		}

		// 答案
		Elements answers = doc.select("div.wgt-best");
		if (!answers.isEmpty()) {
			Element answer = answers.first();
			// 回答
			Elements content = answer.select("div.content pre");
			if (!content.isEmpty()) {
				bean.setAnswerContent(content.first().text());
			}
			
			Elements answerTime = answer.select("span.pos-time");
			if (!answerTime.isEmpty()) {
				bean.setAnswerTime(answerTime.first().text());
			}

			Elements comments = answers.select("span.evaluate");
			for (Element comment : comments) {
				String value = comment.attr("data-evaluate");
				if (!StringUtils.contains(comment.attr("class"), "evaluate-bad")) {
					// 支持数
					bean.setAnswerSupports(value);
				} else {
					// 反对数
					bean.setAnswerOppositions(value);
				}
			}

			Elements username = answers.select("a.user-name");
			if (!username.isEmpty()) {
				bean.setAnswererId(username.first().text());
			}
		}

		return bean;

	}

	private BaiduZhidaoQuestionBean parseZuoyeDetail(Document doc) {
		// 问题
		Elements questions = doc.select("div.main-con dl.question");
		if (questions.isEmpty()) {
			return null;
		}

		BaiduZhidaoQuestionBean bean = new BaiduZhidaoQuestionBean();

		Element question = questions.first();

		// 标题
		Elements askTitle = question.select("span.qb-title");
		if (!askTitle.isEmpty()) {
			bean.setAskTitle(askTitle.first().text());
		}
		// 内容
		Elements askContent = question.select("span.qb-content");
		if (!askContent.isEmpty()) {
			bean.setAskContent(askContent.first().text());
		}
		// 用户
		Elements extraLis = question.select("div.ext-info i");
		if (extraLis.size() == 2) {
			bean.setAskerId(extraLis.first().text());
			bean.setAskTime(extraLis.get(1).text());
		}

		// 答案
		Elements answers = doc.select("div.main-con dl.good-answer");
		if (!answers.isEmpty()) {
			Element answer = answers.first();

			// 内容
			Elements answerContent = answer.select("dd span");
			if (!answerContent.isEmpty()) {
				bean.setAnswerContent(answerContent.first().text());
			}

			// 用户
			Elements answerLis = answer.select("div.ext-info i");
			if (answerLis.size() == 2) {
				bean.setAnswererId(answerLis.first().text());
			}
		}

		return bean;
	}

	public FetchTask buildUserTask(String name, FetchTask oldTask) {
		try {
			FetchTask task = oldTask.clone();
			task.setBatchName(BAIDU_ZHIDAO_USER);
			task.setUrl(MessageFormat.format(BAIDU_ZHIDAO_USER_URL_TEMPLATE, URLEncoder.encode(name, "utf-8")));
			task.setExtra(name);
			return task;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}
