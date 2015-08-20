package lolthx.weixin.sogou;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class WeiXinArticleListProducer extends Producer {

	private static final String WEIXIN_ARTICLE_LIST_URL = "http://weixin.sogou.com/weixin?query={0}&fr=sgsearch&type=2&page={1}&ie=utf8";

	private String keyword;

	public WeiXinArticleListProducer(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "weixin_article_list";
	}

	@Override
	protected int parse() throws Exception {
		String cookies = "ABTEST=0|1439273437|v1; IPLOC=CN1100; SUID=C50BE83D6A20900A0000000055C991DD; SUIR=1439273437; SUV=00B578AB3DE80BC555C991DD6C0BE345; SUID=C50BE83D6A28920A0000000055C991DE; SNUID=15DB38EDD0CACDD227A202D8D0DBFBD2; sct=5; LSTMV=280%2C262; LCLKINT=2992";
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1), cookies, "utf-8");
		// String html = GlobalComponents.seleniumFetcher.fetch(buildUrl(1));
		Document doc = Jsoup.parse(html);

		Elements pages = doc.select("div#pagebar_container a");
		if (pages.size() == 1) {
			return 1;
		}
		if (pages.size() > 2) {
			int maxPageIdx = pages.size() - 2;
			String pageStr = pages.get(maxPageIdx).text();
			System.out.println(pageStr);
			return Integer.parseInt(pageStr);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(WEIXIN_ARTICLE_LIST_URL, URLEncoder.encode(keyword, "UTF-8"), String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String args[]) throws Exception {
		String projectName = "weixin test";
		String[] keywords = { "电动汽车" };
		
		for (int i = 0; i < keywords.length; i++) {
			new WeiXinArticleListProducer(projectName,  keywords[i]).run();
		}
		
	}

}
