package lolthx.yhd.fetch;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.yhd.bean.YHDAskBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 一号店问答爬取 爬取类型json_fetch
 * 
 * @author yanghp
 *
 */
@Slf4j
public class YHDAskFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return YHDGoodsFetch.ASKQUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {

		Document doc = getDocment(result);
		if (doc == null) {
			return;
		}
		fetchBeans(doc, task);
		// 获取总页数
		String pages = doc.select("li.latestnewtotalpage").text();
		if(StringUtils.isBlank(pages)){
			log.info("ask page is empty!");
			return;
		}
		int p = Integer.parseInt(StringUtils.substringBetween(pages, "共", "页"));
		log.info("ask page totel is {}", p);
		if (p > 1) {
			// 翻页继续抓
			for (int m = 2; m <= p; m++) {
				String url = task.getUrl();
				url = url.substring(0, url.length() - 1) + m;
				String text = GlobalComponents.jsonFetch.text(url);
				Document docment = getDocment(text);
				fetchBeans(docment, task);
			}
		}

	}

	private Document getDocment(String result) {
		JSONObject parseObject = JSON.parseObject(result);
		Object value = parseObject.get("value");
		return Jsoup.parse((String) value);
	}

	private void fetchBeans(Document doc, Task task) throws Exception {

		Elements seles = doc.select("ul.question li");
		for (Element element : seles) {
			String question = element.select("p.question_ques").text();
			String answer = element.select("p.answer_con").text();
			String url = element.select("p.answer_con a").attr("href");
			String askId = StringUtils.substringBetween(url, "detail-", ".");
			String date = element.select("em.datetime").text();
			YHDAskBean bean = new YHDAskBean();
			bean.setAnswer(answer);
			bean.setAskId(askId);
			bean.setDate(date);
			bean.setGoodsId(task.getProjectName());
			bean.setQuestion(question);
			if (!bean.exist()) {
				bean.persist();
			}

		}

	}
}
