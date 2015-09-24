package lolthx.baidu.webpage;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.autohome.bbs.AutoHomeBBSCommentFetch;
import lolthx.baidu.webpage.bean.BaiduWebpageBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

@Slf4j
public class BaiduWebpageListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_web_page_list";
	}
	
	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		BaiduWebpageBean bean = new BaiduWebpageBean();
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
			
			Element ele = doc.select("div#page span.pc").first();
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
			
			Elements elements = doc.select("div#content_left div.result.c-container ");
			nums = elements.size();
			
			int all = pageNums + nums;
			if(pageNums + nums > 0){
				bean.setNum(String.valueOf(all));
				bean.saveOnNotExist();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("handle baidu webpage error : {}",e.getMessage(),e);
		}
		
		
	}

	public static void main(String args[]){
		for(int i = 1;i<=16104;i++){
			new BaiduWebpageListFetch().run();
		}
	}
	
	
	
}
