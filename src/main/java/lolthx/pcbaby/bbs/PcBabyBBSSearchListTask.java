package lolthx.pcbaby.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PcBabyBBSSearchListTask extends DistributedParser {

	
	@Override
	public String getQueueName() {
		return "pcbaby_bbs_search_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("ul#JaList > li");
		if (elements.isEmpty()) {
			return;
		}
		Elements link = null;
		for (Element element : elements) {
			link = element.select("dd.oh a");
			if (link.isEmpty()) {
				continue;
			}
			Task newTask = buildTask(link.attr("href"),"pcbaby_bbs_topic",task);
			Queue.push(newTask);
		}
	}

}
