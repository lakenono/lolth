package lolthx.autohome.k;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.autohome.bbs.AutoHomeBBSListResloveFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoHomeWordOfMouthResloveFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "autohome_kb_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		
		String text = doc.select("div.page-cont a.page-item-last").attr("href");
		String page = StringUtils.substringBetween(text, "index_", ".html");
		
		if(page == null ){
			log.error("handle autohomebbs reslove  error : {}",task.getExtra(),"主页解析错误");
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "_1.html", "_{0}.html");
		for(int i= 1 ; i<= pagenum ; i++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "autohome_kb_list", task);
			Queue.push(newTask);
		}
	}
	
	public String buildUrl(String url, int pageNum) {
		System.out.println(" >>>>>>>>>>>>>>>>>" + MessageFormat.format(url, String.valueOf(pageNum)));
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		new AutoHomeWordOfMouthResloveFetch().run();
	}
	
	
}
