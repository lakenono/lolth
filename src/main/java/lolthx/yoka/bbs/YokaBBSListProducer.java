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
			buildTask.setStartDate(DateUtils.parseDate("20140831", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20150831", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args){
		String projectName = "yoka test 1";
		String[] keywords={
			"干燥"
		};
		
		for(int i = 0 ; i< keywords.length; i++){
			new YokaBBSListProducer(projectName,keywords[i]).run();
		}
	}

	
	
	
}

