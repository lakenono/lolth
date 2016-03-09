package lolthx.weixin.sogou;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class WeiXinArticleListProducer extends Producer {

	private static final String WEIXIN_ARTICLE_LIST_URL = "http://weixin.sogou.com/weixin?query={0}&fr=sgsearch&type=2&page={1}&ie=utf8";
	
	//http://weixin.sogou.com/weixin?query=%E8%83%A1%E6%AD%8C&fr=sgsearch&ie=utf8&type=2&w=01015002&oq=huge+&sourceid=sugg

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
		if(1 == 1){
			return 10;
		}
		
		String SUV = "";
		String SNUID = "";
		String SUID = "";
		String SSUID = "";
		String abtestTime = String.valueOf(new Date().getTime()/1000) ;
		
		SUID = "C50BE83D6A28920A00000000" + Integer.toHexString(new Random().nextInt());
		SSUID = "C50BE83D6A28920A00000000" + Integer.toHexString(new Random().nextInt());
		for(int i  = 1 ; i <= 4 ; i++){
			SUV = SUV + Integer.toHexString(new Random().nextInt());
			SNUID = SNUID + Integer.toHexString(new Random().nextInt());
		}
		
		String cookies = "SUID=" + SUID + "; weixinIndexVisited=1; SNUID=" + SNUID + "; ABTEST=5|" + abtestTime +"|v1; IPLOC=CN1100; SUID=" + SSUID + "; SUV=" + SUV  + "; sct=3; wapsogou_qq_nickname=";
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1), cookies, "utf-8");
		
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
		String projectName = "中粮生态谷大数据调研-微信爬取-20151028";
		String[] keywords = { "郊游"
		};
		
		for (int i = 0; i < keywords.length; i++) {
			new WeiXinArticleListProducer(projectName,  keywords[i]).run();
		}
		
	}

}
