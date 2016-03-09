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
		String count = "0";
		int page = 0;
		if(countText == null){
			Elements els = doc.select("div.topics table.olt tr.pl");
			if(els != null){
				page = 1;
			}
		}else{
			count =  StringUtils.substringBetween(countText, "(共", "个结果)");
			page = Integer.valueOf(count) / 50 + 1;
			if(page > 40){
				page = 40;
			}
		}
		return page;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(DOUBBAN_SEARCH_URL, String.valueOf((pageNum- 1)*50) ,URLEncoder.encode(keyword, "utf-8"));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("20100101", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20151121", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args){
		String projectName = "华扬联众演讲数据-豆瓣-20151116";
		String[] keywords = { "LaMer","海蓝之谜","HR,赫莲娜","赫莲娜","CHANEL","香奈儿","Dior","迪奥","EsteeLauder","雅诗兰黛","SK-II","LANCOME","兰蔻","ElizabethArden","伊丽莎白雅顿","Clarins","娇韵诗","Sisley",
				"希思黎","Guerlain","娇兰","Clinique","倩碧","Maybelline","美宝莲","Avene","雅漾","Mentholatum","曼秀雷敦","SHISEIDO","资生堂","Biotherm","碧欧泉","Olay","玉兰油",
				"L OREAL","巴黎欧莱雅","AUPRES","欧珀莱","VICHY","薇姿","NIVEA","妮维雅","kiehl s","契尔氏","Garnier","卡尼尔",
				"DHC","蝶翠诗","Za","姬芮","ARTISTRY","雅姿","Charmzone","婵真","Mamonde","梦妆","hera","赫拉","Innisfree","悦诗风吟",
				"SKIN FOOD","THE FACE SHOP","谜尚","MISSHA","it`s skin","伊思","IOPE","LANEIGE","兰芝","Sulwhasoo","雪花秀","whoo后",
				"ohui","欧蕙","ETUDE HOUSE","爱丽小屋","相宜本草",
				"LouisVuitton","路易威登","Gucci","古驰","CHANL","香奈儿","小香","PRADA","普拉达","HERMES","爱马仕","Dior","迪奥",
				"bottegaveneta","葆蝶家","Burberry","博柏利","巴宝莉","chloe","蔻依","克洛伊","AlexanderMqueen","亚历山大·麦昆","marcjacobs","马克·雅可布",
				"MansurGavriel","曼苏丽尔","versace","范思哲","CELINE","思琳","赛琳","FENDI","芬迪","MiuMiu","缪缪","givenchy","纪梵希",
				"marcbymarcjacobs","Coach","蔻驰","寇驰","MichaelKors","迈克.科尔斯","Katespade","凯特·丝蓓","ToryBurch","汤丽柏琦","MCM","Furla","芙拉","GerardDarel","杰哈·达黑勒"
 }; 
		
		for(int i = 0 ; i < keywords.length ; i++){
			new DoubanSearchProducer(projectName, keywords[i]).run();
		}
	}
	
}
