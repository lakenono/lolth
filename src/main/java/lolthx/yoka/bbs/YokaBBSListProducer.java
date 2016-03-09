package lolthx.yoka.bbs;

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

public class YokaBBSListProducer extends Producer{

	private static final String YOKA_BBS_LIST_URL = 
			"http://zhannei.baidu.com/cse/search?q={0}&p={1}&s=2300126470136754598&srt=lds&nsid=3";
	
	/**
	 * stp	0全文搜索	1标题搜索
	 * sti	0全部时间	60一小时内	1440一天内	10080一周内	43200一个月内
	 * srt 	def默认排序	lds按时间排序
	 */

	private String keyword;
	
	public YokaBBSListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}


	@Override
	public String getQueueName() {
		return "yoka_bbs_list_resolve";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(74));
		Document doc = Jsoup.parse(html);
		
		String pages = doc.select("div#footer span.pager-current-foot").text();
	
		
		if(pages == null || pages.equals("")){
			String instr = doc.select("div#results span.support-text-top").text();
			String inStr1 = StringUtils.substringBetween(instr, "相关结果", "个");
			int page =	Integer.valueOf(inStr1);
			if(page == 0){
				return page;
			}else{
				return 1;
			}
		}
		
		return Integer.valueOf(pages);
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(YOKA_BBS_LIST_URL, URLEncoder.encode(keyword, "utf-8") , String.valueOf(pageNum));
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
		String projectName = "华扬联众演讲数据-YOKA-20151116";
		String[] keywords={
				"LouisVuitton","路易威登","Gucci","古驰","CHANL","香奈儿","小香","PRADA","普拉达","HERMES","爱马仕","Dior","迪奥",
				"bottegaveneta","葆蝶家","Burberry","博柏利","巴宝莉","chloe","蔻依","克洛伊","AlexanderMqueen","亚历山大·麦昆","marcjacobs","马克·雅可布",
				"MansurGavriel","曼苏丽尔","versace","范思哲","CELINE","思琳","赛琳","FENDI","芬迪","MiuMiu","缪缪","givenchy","纪梵希",
				"marcbymarcjacobs","Coach","蔻驰","寇驰","MichaelKors","迈克.科尔斯","Katespade","凯特·丝蓓","ToryBurch","汤丽柏琦","MCM","芙拉","GerardDarel","杰哈·达黑勒"
		};
		
		for(int i = 0 ; i< keywords.length; i++){
			new YokaBBSListProducer(projectName,keywords[i]).run();
		}
	}

	
	
	
}

