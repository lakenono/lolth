package lolthx.bitauto.k;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class BitautoWordOfMouthResloveFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "bitauto_kb_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		//bitauto_kb_list
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements pageEs = doc.select("div.the_pages a");
		
		// 没有分页标签
		int page = 0;
		if (pageEs.isEmpty()) {
			if (!doc.select("div.postslist_xh").isEmpty()) {
				page =  1;
			}
		}
		// 有分页标签
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				page = Integer.parseInt(pages);
			}
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "-1-0.html", "-{0}-0.html");
		
		for(int i = 1 ; i <= pagenum ; i ++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "bitauto_kb_list", task);
			Queue.push(newTask);
		}
		
	}
	
	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		new BitautoWordOfMouthResloveFetch().run();
	}
	
}
