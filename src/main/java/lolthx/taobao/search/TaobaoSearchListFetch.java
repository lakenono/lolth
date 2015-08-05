package lolthx.taobao.search;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

public class TaobaoSearchListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "taobao_item_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		Elements divs = doc.select("div.items > div.item  ");
		Elements tmp = null;
		Task newTask = null;
		for (Element div : divs) {
			tmp = div.select("div.row-4 span.icon-service-tianmao");
			String itemId = div.select("div.row-2 a").attr("data-nid");
			if (tmp.isEmpty()) {
				// 淘宝
				newTask = buildTask("https://item.taobao.com/item.htm?id=" + itemId, "taobao_item_detail", task);
			} else {
				// 天猫
				newTask = buildTask("https://detail.tmall.com/item.htm?id=" + itemId, "tmall_item_detail", task);
			}
			Queue.push(newTask);
		}

	}

}
