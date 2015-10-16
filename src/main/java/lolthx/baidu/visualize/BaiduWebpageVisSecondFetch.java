package lolthx.baidu.visualize;

import java.text.MessageFormat;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BaiduWebpageVisSecondFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "baidu_webpage_vis_second";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document document = Jsoup.parse(result);
		
		Element ele = document.select("div#page span.pc").last();
			if(ele == null){
				return;
			}else{
				String text = ele.text().trim();
				int num =  Integer.valueOf(text);
				for(int i = 0 ; i < num ;i++ ){
					String url = this.buildUrl(task.getUrl(),i);
					Task newTask = buildTask(url, "baidu_webpage_vis_list", task);
					Queue.push(newTask);
				}
			}
	}
	
	private String buildUrl(String url,int page) throws Exception{
		String pn = "pn=" + String.valueOf(page * 10);
		url = url.replaceFirst("pn=750", pn);
		return url;
	}
	
	public static void main(String[] args){
		for(int i = 1; i <= 73200 ;i++){
			new BaiduWebpageVisSecondFetch().run();
		}
	}
}
