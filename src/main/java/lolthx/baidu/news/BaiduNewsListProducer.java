package lolthx.baidu.news;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.nodes.Document;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class BaiduNewsListProducer extends Producer {

	private static final String BAIDU_NEWS_URL = "http://news.baidu.com/ns?from=news&cl=2&bt={0}&y0={1}&m0={2}&d0={3}&y1={4}&m1={5}&d1={6}&et={7}&q1={8}&tn={9}&ct1={10}&ct={11}&pn={12}";

	private String keyword;
	private String sort;
	private String type;
	private String start;
	private String end;

	/**
	 * keyword: 关键字 sort: 排序方式，1表示按焦点排序，0表示按时间排序 type:newsdy/新闻全文
	 * newstitledy/新闻标题 start:开始日期 end: 结束日期
	 */
	public BaiduNewsListProducer(String projectName, String keyword, String sort, String type, String start, String end) {
		super(projectName);
		this.keyword = keyword;
		this.sort = sort;
		this.type = type;
		this.start = start;
		this.end = end;
	}

	@Override
	public String getQueueName() {
		return "baidu_news_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		System.out.println(buildUrl(1));
		String text = document.select("div#header_top_bar span.nums").first().text();
		String pageNum = StringUtils.substringBetween(text, "新闻", "篇");
		pageNum = pageNum.replaceAll(",", "");
		if("0".equals(pageNum)){
			return 0;
		}else{
			Integer  page =  Integer.valueOf(pageNum)/10 +1 ;
			if(page >= 66){
				return 66;
			}else{
				return page;
			}
		}
	
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(end + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });
		Calendar cal = Calendar.getInstance();

		String str0 = String.valueOf(startDate.getTime() / 1000);

		cal.setTime(startDate);
		String str1 = String.valueOf(cal.get(Calendar.YEAR));
		String str2 = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String str3 = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		cal.setTime(endDate);
		String str4 = String.valueOf(cal.get(Calendar.YEAR));
		String str5 = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String str6 = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String str7 = String.valueOf(endDate.getTime() / 1000);

		String str8 = URLEncoder.encode(keyword, "utf-8");
		String str9 = type;

		String str10 = sort;
		String str11 = sort;

		int str12 = (pageNum - 1) * 10;
		

		return MessageFormat.format(BAIDU_NEWS_URL, str0, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12);
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	/**
	 * keyword: 关键字 
	 * sort: 排序方式，1表示按焦点排序，0表示按时间排序 
	 * type:newsdy/新闻全文 newstitledy/新闻标题 
	 * start:开始日期 end: 结束日期
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		String projectName = "baidu news list";
		String[] keywords = { "观致SUV" };
		String sort = "0";// 排序方式，1表示按焦点排序，0表示按时间排序。
		String type = "newsdy";
		String start = "20150918";
		String end = "20150918";
		
		Date startDate = DateUtils.parseDate(start, new String[] { "yyyyMMdd" });
		Date endDate = DateUtils.parseDate(end, new String[] { "yyyyMMdd" });
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + startDate.toString() + "::" + endDate.toString() );
		
		for (int i = 0; i < keywords.length; i++) {
			while (true){
				new BaiduNewsListProducer(projectName, keywords[i], sort, type, DateFormatUtils.format(startDate, "yyyyMMdd"), DateFormatUtils.format(startDate, "yyyyMMdd")).run();
			
				if (DateUtils.isSameDay(startDate, endDate)){
					break;
				}
				startDate = DateUtils.addDays(startDate, 1);
			}
		}
	}

}
