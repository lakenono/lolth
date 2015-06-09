package lolth.jd.search;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JDCommoditySearchListTask extends DistributedParser{

	@Override
	public String getQueueName() {
		return "jd_search_commodity_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements links = doc.select("div.lh-wrap>div.p-name a");
		for(Element link:links){
			Task newTask = buildTask(link.attr("href"),"jd_commodity",task);
			Queue.push(newTask);
		}
	}
	
}
