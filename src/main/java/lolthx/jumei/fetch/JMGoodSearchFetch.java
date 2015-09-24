package lolthx.jumei.fetch;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.jumei.bean.JMGoodsBean;
import lolthx.jumei.task.JMGoodsSearchTask;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class JMGoodSearchFetch extends DistributedParser {
	public final String KOUBEI_URL = "http://koubei.jumei.com/ajax/reports_for_deal_newpage.json?init=1&product_id={0}";
	public final static String KOUBEI_QUEUE = "jm_koubei";
	public Pattern p = Pattern.compile("(?:\\d+[a-zA-Z]*)(\\d+)");

	@Override
	public String getQueueName() {
		return JMGoodsSearchTask.QUEUENAME;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("div.products_wrap > ul > li");
		if (elements.isEmpty()) {
			return;
		}
		for (Element element : elements) {
			JMGoodsBean bean = new JMGoodsBean();
			String id = element.attr("h_p_m_id");
			if (StringUtils.isBlank(id)) {
				continue;
			}
			String name = element.select("div.s_l_name").text();
			if (StringUtils.contains(name, "/")) {
				name = StringUtils.substringAfter(name, "/");
			}
			String url = element.select("div.s_l_name > a").attr("href");
			String price = element.select("div.search_list_price > span").text();
			String deals = element.select("div.search_pl").text();
			if (StringUtils.contains(deals, "人")) {
				deals = StringUtils.substringBefore(deals, "人");
			} else {
				deals = "";
			}
			// 生成口碑url，推送mq
			String uid = id;
			// 如果id里包含p，正则提取uid
			if (StringUtils.containsAny(id, 'p')) {
				Matcher matcher = p.matcher(id);
				if (matcher.find()) {
					uid = matcher.group(1);
				}
			}

			String koubei_url = MessageFormat.format(KOUBEI_URL, uid);
			Task t = buildTask(koubei_url, KOUBEI_QUEUE, id, task);

			bean.setId(id);
			bean.setName(name);
			bean.setUrl(url);
			bean.setPrice(price);
			bean.setDeals(deals);
			if (bean.saveOnNotExist()) {
				// 如果储存完成就发生口碑task
				Queue.push(t);
			}
		}
	}

	private Task buildTask(String kb_url, String koubeiQueue, String id, Task task) {
		Task t = new Task();
		t.setExtra(id);
		t.setQueueName(koubeiQueue);
		t.setUrl(kb_url);
		t.setProjectName(task.getProjectName());
		return t;
	}

}
