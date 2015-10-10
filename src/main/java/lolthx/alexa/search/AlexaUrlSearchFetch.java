package lolthx.alexa.search;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.alexa.bean.AlexaUrlBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AlexaUrlSearchFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "alexa_url_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		AlexaUrlBean bean = new AlexaUrlBean(); 
		
		Document doc = Jsoup.parse(result);

		
		
		String[] str = task.getExtra().split(":");
		
		bean.setId(task.getUrl());
		bean.setProjectName(task.getProjectName());
		bean.setKeyword(str[0]);
		bean.setUrl(str[1] + ":" + str[2]);
		
		if(doc.select("font#w_15") == null || "".equals(doc.select("font#w_15").text().trim())){
			bean.setW15("未获取");
		}else{
			String w15 = doc.select("font#w_15").text().trim();
			bean.setW15(w15);
		}
		
		bean.saveOnNotExist();	
	}

	
	public static void main(String[] args){
		for(int i = 1 ; i <=103 ; i++){
			AlexaUrlSearchFetch ala = new AlexaUrlSearchFetch();
			ala.useDynamicFetch();
			ala.run();
		}
	}
	
}
