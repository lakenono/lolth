package lolthx.lefeng.search;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

public class LeFengListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "lefeng_item_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		Elements divs = doc.select("div.pruwrap");
		Task newTask = null;
		for (Element div : divs) {
			String id = div.attr("id");
			if (StringUtils.isBlank(id)) {
				continue;
			}
			// 标示这个是团购
			Elements times = div.select("dd.sale_time");
			if (!times.isEmpty()) {
				continue;
			}
			Elements a = div.select("dd.nam > a");
			newTask = buildTask(a.attr("href"), "lefeng_item_detail", task);
			Queue.push(newTask);
		}

	}

}
