package lolth.zhaopin;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

public class BaiduBBSSearchListTask extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_search_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		Elements divs = doc.select("div.result");
		for (Element div : divs) {
			Task newTask = null;
			if(task.getProjectName().equals("bbs.sysu.edu.cn")){
				newTask = buildTask(div.select("h3 a").attr("href"),"baidu_search_bbs_cascade_list",task);
			}else{
				newTask = buildTask(div.select("h3 a").attr("href"),"baidu_search_bbs_topic",task);
			}
			Queue.push(newTask);
		}
	}

}
