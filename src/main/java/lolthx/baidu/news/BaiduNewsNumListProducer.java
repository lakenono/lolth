package lolthx.baidu.news;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;

import lakenono.base.Producer;
import lakenono.base.Task;

public class BaiduNewsNumListProducer extends Producer{

	private static final String BAIDU_NEWS_NUM_URL = 
			"http://news.baidu.com/ns?from=news&cl=2"
			+"&q1={0}"
			+ "&bt={1}&et={2}"
			+ "&q3=&q4=&mt=0&lm=&s=2&tn=newstitledy&ct=0&rn=20";

	
	private String city;
	private String keyword;
	
	public BaiduNewsNumListProducer(String projectName,String city,String keyword) {
		super(projectName);
		this.city = city;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "baidu_news_num_list";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String str0 = URLEncoder.encode(keyword, "utf-8");
		
		System.out.println(MessageFormat.format(BAIDU_NEWS_NUM_URL, str0));
		
		return MessageFormat.format(BAIDU_NEWS_NUM_URL, str0);
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(city + ":" + keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("20140831", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20150831", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String args[]){
		String projectName = "网络媒体";
		String[] citys = {
				
		};
		String[] keywords = {
				
		};
		
		for(int i = 0 ; i< citys.length; i++){
			new BaiduNewsNumListProducer(projectName,citys[i],keywords[i]).run();
		}
		
	}
	

}
