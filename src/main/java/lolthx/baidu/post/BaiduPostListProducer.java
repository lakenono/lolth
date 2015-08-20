package lolthx.baidu.post;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BaiduPostListProducer extends Producer {

	private static final String BAIDU_POST_SEARCH_URL = "http://tieba.baidu.com/f?kw={0}&ie=utf-8&pn={1}";
																							  //http://tieba.baidu.com/f?kw=%E8%87%AA%E6%88%91&ie=utf-8&pn=50

	private static final int PAGE_SIZE = 50;

	private String keyword;

	public BaiduPostListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "baidu_tieba_list";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1));
		Document doc = Jsoup.parse(html);
		Elements div = doc.select("div.forum_foot div.th_footer_2 div.th_footer_bright div.th_footer_l");
		if (div.size() == 0) {
			return 0;
		}

		String pageSize = div.first().text();
		pageSize = StringUtils.substringBetween(pageSize, "共有主题数", "个");

		if (!StringUtils.isNumeric(pageSize)) {
			return 0;
		}

		int maxpage = Integer.parseInt(pageSize) / PAGE_SIZE + 1;
		if (maxpage > 500) {
			maxpage = 500;
		}
		return maxpage;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(BAIDU_POST_SEARCH_URL, URLEncoder.encode(keyword, "utf-8"), String.valueOf((pageNum - 1) * PAGE_SIZE));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	public static void main(String args[]) throws Exception{
		String projectName = "tieba test";
		String[] keywords = { "电动汽车"};
		for (int i = 0; i < keywords.length ; i++) {
			new BaiduPostListProducer(projectName,keywords[i]).run();
		}
			
	}
	
}
