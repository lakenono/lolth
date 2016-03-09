package lolthx.xcar.k;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class XCarWordOfMouthListResloveFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "xcar_kb_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		
		Elements pages = doc.select("div.pagers a");
		int page = 0;
		if (pages.size() == 1) {
			page =  1;
		}

		if (pages.size() > 2) {
			int maxPageIdx = pages.size() - 2;
			String pageStr = pages.get(maxPageIdx).text();
			page = Integer.parseInt(pageStr);
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "_1.htm", "_{0}.htm");
		
		for(int i = 1 ; i <= pagenum ; i ++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "xcar_kb_list", task);
			Queue.push(newTask);
		}
		
	}
	
	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		new XCarWordOfMouthListResloveFetch().run();
	}
	
}
