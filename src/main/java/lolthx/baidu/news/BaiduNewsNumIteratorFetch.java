package lolthx.baidu.news;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.baidu.news.bean.BaiduNewsNumBean;
import lolthx.baidu.webpage.BaiduWebpageListFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduNewsNumIteratorFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "baidu_news_iterator_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		BaiduNewsNumBean bean = new BaiduNewsNumBean();
		
		bean.setId(task.getUrl());
		bean.setProjectName(task.getProjectName());
		
		String[] args = task.getExtra().split(":");
		bean.setCity(args[0]);
		bean.setKeyword(args[1]);
		bean.setDate(args[2]);
		
		try {
			Document doc = Jsoup.parse(result);
			int pageNums = 0;
			int nums = 0;
			
			Element ele = doc.select("div#page span.pc").last();
			if(ele == null){
				pageNums = 0;
			}else{
				try {
					String text = ele.text().trim();
					pageNums = (Integer.valueOf(text) - 1 ) * 10;
				} catch (Exception e) {
					e.printStackTrace();
					pageNums = 0;
				}
			}
			
			Elements elements = doc.select("div.result[id]");
			nums = elements.size();
			
			int all = pageNums + nums;
			System.out.println(">>>>>>>>>>>>>>>>> all > " + all);
			if(pageNums + nums > 0){
				bean.setNum(String.valueOf(all));
				bean.saveOnNotExist();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("handle baidu news iterator error : {}",e.getMessage(),e);
		}
		
	}

	public static void main(String args[]){
		for(int i = 1 ; i <=62329 ; i++){
			new BaiduNewsNumIteratorFetch().run();
		}
	}
	
	
}
