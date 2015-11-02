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
		String[] keywords = { "郊游","农家乐","休闲游","生态游","农业观光游","亲子主题游","一日游","酒店度假","湿地公园","农业","植物园","农场","春游","秋游","拓展训练","展览馆","博物馆",
				"科技馆","户外活动","房山","琉璃河","郊区","生态","农民","卫星城","小镇 ","远郊 ","农业博览会","农业展览馆","农业科技","农科院","农业产业","农业科普","农业教育",
				"农业公司","农业产业链","农业开发","农业产业园","有机农业","健康产业","郊区买房","投资买房","度假买房","养老公寓","郊区配套","郊区别墅","郊区花园洋房"
		};
		
		for (int i = 0; i < keywords.length; i++) {
			new WeiXinArticleListProducer(projectName,  keywords[i]).run();
		}
		
	}

}
