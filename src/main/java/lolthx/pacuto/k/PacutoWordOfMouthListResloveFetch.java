package lolthx.pacuto.k;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class PacutoWordOfMouthListResloveFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "pacuto_kb_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements pageEs = doc.select("div#pcauto_page a");
		
		int page = 0;
		if (pageEs.isEmpty()) {
			if (!doc.select("div.main_table.clearfix").isEmpty()) {
				page = 1;
			}
		}
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				page =  Integer.parseInt(pages);
			}
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "p1.html", "p{0}.html");
		
		for(int i = 1 ; i <= pagenum ; i ++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "pacuto_kb_list", task);
			Queue.push(newTask);
		}
		
	}
	
	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		new PacutoWordOfMouthListResloveFetch().run();
	}
	
}
