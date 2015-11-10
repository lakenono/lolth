package lolthx.yoka.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YokaBBSListResolveFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "yoka_bbs_list_resolve";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("div#results div.result");
				
		for (Element element : elements) {
			String url = element.select("h3.c-title a").attr("href");
			
			String[] urls = url.split("-");
			if(urls.length == 4){
				url = urls[0] + "-" + urls[1] + "-" + "1" + "-"+ urls[3];		
				Task newTask = buildTask(url, "yoka_bbs_detail", task);
				Queue.push(newTask);
			}
			
		}
	}
	
	
	
	@Override
	protected Task buildTask(String url, String queueName, Task perTask) {
		Task task =  super.buildTask(url, queueName, perTask);
		task.setStartDate(perTask.getStartDate());
		task.setEndDate(perTask.getEndDate());
		return task;
	}

	public static void main(String args[]) {
		for(int i = 1; i <= 5 ;i++){
			new YokaBBSListResolveFetch().run();
		}
	}
	
}
