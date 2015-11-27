package lolthx.zhihu.search;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ZhihuKwListProducer extends Producer {
	
	private static final String ZHIHU_KW_LIST_URL = "http://zhihu.sogou.com/zhihu?ie=utf8&query={0}&page={1}";
	
	private String keyword;
	
	public ZhihuKwListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "zhihu_kw_reslove";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(76));
		
		Document doc = Jsoup.parse(html);
		
		Integer page = 0;
		String pageText = doc.select("div.result-page li.active").text();
		
		if(pageText != null ){
			page = Integer.valueOf(pageText);
		}else{
			page = 1;
		}
		return page;
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("20110101", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20151116", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(ZHIHU_KW_LIST_URL, URLEncoder.encode(keyword, "utf-8") , String.valueOf(pageNum));
	}
	
	public static void main(String[] args){
		String projectName = "zhihu test";
		String[] keywords = {"dota"};
		for(int i = 0 ; i < keywords.length ; i++){
			new ZhihuKwListProducer(projectName,keywords[i]).run();
		}
	}
	
}
