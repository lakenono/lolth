package lolthx.autohome.bbs;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class AutoHomeBBSListResloveFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "autohome_bbs_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		
		String text = doc.select("div.pagearea span.fr").text();
		String page = StringUtils.substringBetween(text, "共", "页");	
		if(page == null ){
			log.error("handle autohomebbs reslove  error : {}",task.getExtra(),"主页解析错误");
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "-1.html", "-{0}.html");
		for(int i = 1 ; i <= pagenum ; i ++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "autohome_bbs_list", task);
			Queue.push(newTask);
		}
		
	}
	
	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
			new AutoHomeBBSListResloveFetch().run();	
	}

}
