package lolthx.douban.search;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DoubanSearchProducer extends Producer{
	
	private static final String DOUBBAN_SEARCH_URL = "http://www.douban.com/group/search?start={0}&cat=1013&q={1}&sort=time"; 
	
	private String keyword;
	
	public DoubanSearchProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "douban_search_reslove";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1));
		Document doc = Jsoup.parse(html);
		
		String countText = doc.select("div.paginator span.count").text();
		System.out.println(">>> countText >> " + countText);
		String count = "0";
		int page = 0;
		if(countText == null){
			Elements els = doc.select("div.topics table.olt tr.pl");
			if(els != null){
				page = 1;
			}
		}else{
			count =  StringUtils.substringBetween(countText, "(共", "个结果)");
			System.out.println(">>> count " + count);
			page = Integer.valueOf(count) / 50 + 1;
			if(page > 40){
				page = 40;
			}
		}
		return page;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		System.out.println(">>>> url:  " + MessageFormat.format(DOUBBAN_SEARCH_URL, String.valueOf((pageNum- 1)*50) ,URLEncoder.encode(keyword, "utf-8")));
		return MessageFormat.format(DOUBBAN_SEARCH_URL, String.valueOf((pageNum- 1)*50) ,URLEncoder.encode(keyword, "utf-8"));
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
	
	public static void main(String[] args){
		String projectName = "douban test 20151117";
		String[] keywords = {"LouisVuitton","路易威登","Gucci","古驰","CHANL","香奈儿"		}; 
		
		for(int i = 0 ; i < keywords.length ; i++){
			new DoubanSearchProducer(projectName, keywords[i]).run();
		}

	}
	
}
