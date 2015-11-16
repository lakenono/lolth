package lolthx.onlylady.bbs.search;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OnlyladyBBSSearchList extends DistributedParser{

	@Override
	public String getQueueName() {
		return "onlylady_bbs_search_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements lis = doc.select("#threadlist > ul > li");
		Task newTask = null;
		for(Element ls : lis){
			newTask = buildTask(ls.select("h3 > a").attr("href"), "onlylady_bbs_topic", task);
			Queue.push(newTask);
		}

		
	}
	
	public static void main(String[] args) throws Exception {
//		String gg = "http://bbs.onlylady.com/search.php?mod=forum&searchid=1893847&orderby=lastpost&ascdesc=desc&searchsubmit=yes&page=22";
//		String fetch = GlobalComponents.jsoupFetcher.fetch(gg);
////		System.out.println(fetch);
//		//#threadlist > ul
//		Document doc = Jsoup.parse(fetch);
//		Elements lis = doc.select("#threadlist > ul > li");
//		for(Element ls : lis){
//			System.out.println(ls.select("h3 > a").attr("href"));
//		}
		new OnlyladyBBSSearchList().run();
	}

	
}
