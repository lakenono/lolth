package lolth.zhaopin;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

public class BaiduBBSCascadeListTask extends DistributedParser{

	@Override
	public String getQueueName() {
		return "baidu_search_bbs_cascade_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements select = doc.select("body > form > table > tbody > tr:nth-child(2) > td > table > tbody td:nth-child(3) > a");
		String u = "";
		for (Element element : select) {
			u = "http://bbs.sysu.edu.cn/"+element.attr("href");
			Task newTask = buildTask(u,"baidu_search_bbs_topic",task);
			Queue.push(newTask);
		}
		
	}

}
