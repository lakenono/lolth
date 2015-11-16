package lolthx.onlylady.bbs.search;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpRequestMethod;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class OnlyladyBBSSearchProducer extends Producer {

	private String origin = "http://bbs.onlylady.com/";

	private String onlylady_search_post_url = "http://bbs.onlylady.com/search.php?mod=forum&searchid={0}&orderby=lastpost&ascdesc=desc&searchsubmit=yes&page={1}";

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	private String keyword;

	private String searchid;

	public OnlyladyBBSSearchProducer(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "onlylady_bbs_search_list";
	}

	@Override
	protected int parse() throws Exception {
		String html = pageHtml();
		if (StringUtils.isBlank(html)) {
			log.error("onlylady max page is error");
			return 0;
		}
		Document doc = Jsoup.parse(html);
		Elements as = doc.select("div.pgs.cl.mbm > div > a");
		if (as.isEmpty()) {
			log.error("onlylady max page is empty");
			return 0;
		}

		Element a = as.get(as.size() - 2);
		String pageStr = a.text();
		Matcher matcher = pattern.matcher(pageStr);
		pageStr = matcher.replaceAll("");
		if (!StringUtils.isNumeric(pageStr)) {
			log.error("onlylady max page is empty : " + a);
			return 0;
		}
		log.info("onlylady max page is : " + pageStr);
		return Integer.parseInt(pageStr);
	}

	private String pageHtml() throws Exception {
		HttpClientFetcher fetcher = new HttpClientFetcher();
		HttpRequest request = new HttpRequest(origin + "search.php?mod=forum");
		request.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
		HttpResponse resp = fetcher.run(request);
		String html = new String(resp.getContent(), "GBK");
		if (StringUtils.isBlank(html)) {
			log.error("onlylady第一次获取页面为空！");
			fetcher.close();
			return "";
		}

		Map<String, String> maps = new HashMap<String, String>(4);
		Document doc = Jsoup.parse(html);
		String formhash = doc.select("form > input[type=\"hidden\"]").attr("value");
		maps.put("formhash", formhash);
		maps.put("srchtxt", keyword);
		maps.put("searchsubmit", "yes");
		maps.put("chart", "gbk");
		request.setParams(maps);
		request.setMethod(HttpRequestMethod.POST);
		resp = fetcher.run(request);
		html = new String(resp.getContent());
		if (StringUtils.isBlank(html)) {
			log.error("onlylady post提交返回数据为空！");
			fetcher.close();
			return "";
		}
		searchid = HttpURLUtils.getUrlParams(html, "gbk").get("searchid");
		if (StringUtils.isBlank(searchid)) {
			log.error("onlylady searchid is empty");
			fetcher.close();
			return "";
		}
		request.setUrl(origin + html);
		Thread.sleep(4000);
		resp = fetcher.run(request);
		html = new String(resp.getContent(), "GBK");
		fetcher.close();
		return html;

	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(onlylady_search_post_url, searchid, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) {
		String[] keywords = {"LaMer","海蓝之谜","HR","赫莲娜 ","CHANEL","香奈儿","Dior","迪奥 ","EsteeLauder","雅诗兰黛","SK-II ","LANCOME","兰蔻 ","ElizabethArden","伊丽莎白雅顿","Clarins","娇韵诗","Sisley","希思黎","Guerlain","娇兰","Clinique","倩碧","Maybelline","美宝莲","Avene","雅漾","Mentholatum","曼秀雷敦","SHISEIDO","资生堂 ","Biotherm","碧欧泉","Olay","玉兰油","L'OREAL","巴黎欧莱雅 ","AUPRES","欧珀莱","VICHY","薇姿","NIVEA","妮维雅","kiehl's","契尔氏","Garnier","卡尼尔","DHC","蝶翠诗","Za","姬芮","ARTISTRY","雅姿","Charmzone","婵真","Mamonde","梦妆 ","hera","赫拉 ","Innisfree","悦诗风吟 ","SKINFOOD","THE FACE SHOP","谜尚","MISSHA","it`s skin","伊思","IOPE","LANEIGE","兰芝 ","Sulwhasoo","雪花秀","whoo后","ohui","欧蕙 ","ETUDE HOUSE","爱丽小屋","相宜本草","LouisVuitton","路易威登","Gucci","古驰","CHANL","香奈儿","小香","PRADA","普拉达","HERMES","爱马仕","Dior","迪奥","bottegaveneta","葆蝶家","Burberry","博柏利","巴宝莉","chloe","蔻依","克洛伊","AlexanderMqueen","亚历山大·麦昆","marcjacobs","马克·雅可布","MansurGavriel","曼苏丽尔","versace","范思哲","CELINE","思琳","赛琳","FENDI","芬迪","MiuMiu","缪缪","givenchy","纪梵希","marcbymarcjacobs","Coach","蔻驰","寇驰","MichaelKors","迈克.科尔斯","Katespade","凯特·丝蓓","ToryBurch","汤丽柏琦","MCM","Furla","芙拉","GerardDarel","杰哈·达黑勒"};
		String projectName = "A20151113";
		for (String keyword : keywords) {
			new OnlyladyBBSSearchProducer(projectName, keyword).run();
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
