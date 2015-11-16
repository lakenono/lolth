package lolthx.baidu.post;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class BaiduPostDetailByKwProducer extends Producer{

	private String keyword;
	
	public BaiduPostDetailByKwProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	private static final String BAIDU_POST_SEARCH_BY_KW_URL = "http://tieba.baidu.com/f/search/res?ie=utf-8&isnew=1&kw=&qw={0}&rn=10&only_thread=0&pn={1}";
	
	@Override
	public String getQueueName() {
		return "baidu_tieba_list_bykw_first";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(76));
		
		Document doc = Jsoup.parse(html);
		String pageSize = "0";

		pageSize = doc.select("div.pager.pager-search span.cur").first().text();
		
		System.out.println(pageSize);
		
		return Integer.valueOf(pageSize);
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		System.out.println(" keyword " + keyword + "  url :" +  MessageFormat.format(BAIDU_POST_SEARCH_BY_KW_URL, URLEncoder.encode(keyword, "utf-8"), pageNum));
		return MessageFormat.format(BAIDU_POST_SEARCH_BY_KW_URL, URLEncoder.encode(keyword, "utf-8"), pageNum);
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	public static void main(String args[]) {
		String projectName = "中粮生态谷大数据调研-百度贴吧关键字-20151028";
		String[] keywords={
				"郊游","农家乐","休闲游"
		};
		
		for (int i = 0; i < keywords.length ; i++) {
			new BaiduPostDetailByKwProducer(projectName, keywords[i]).run();
		}
		
	}
	
	
	
	
}
