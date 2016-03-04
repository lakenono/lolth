package lolthx.bitauto.bbs;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BitautoBBSListResloveFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "bitauto_bbs_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		//bitauto_bbs_list
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements pageEs = doc.select("div.the_pages a");
		
		// 没有分页标签
		int page = 0;
		if (pageEs.isEmpty()) {
			if (!doc.select("div.postslist_xh").isEmpty()) {
				page = 1;
			}
		}
		// 有分页标签
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				page =  Integer.parseInt(pages);
			}
		}
		
		int pagenum = Integer.valueOf(page);
		String url = StringUtils.replace(task.getUrl(), "-1-1.html", "-{0}-1.html");
		
		for(int i = 1 ; i <= pagenum ; i ++){
			String sendUrl = buildUrl(url, i);
			Task newTask = buildTask(sendUrl, "bitauto_bbs_list", task);
			Queue.push(newTask);
		}
		
	}
	
	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		new BitautoBBSListResloveFetch().run();
	}
	
	
}
