package lolthx.baidu.webpage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.core.GlobalComponents;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaiduSpotsListProducer {
	private String BAIDU_SPOTS_LIST = "https://www.baidu.com/s?ie=utf-8&f=8&wd={0}&pn=10&usm=6";
	
	public String[] solveHtmlPage(String city){	
		String[] descriptions = new String[50];
		String html = null;
		try {
			String str0 = URLEncoder.encode(city, "utf-8");
			html = GlobalComponents.jsoupFetcher.fetch("https://www.baidu.com/s?ie=utf-8&f=8&wd=" + str0 +"&pn=10&usm=6");
		
			Document doc = Jsoup.parse(html);
			
			String keywordRegex = "'rsv_re_ename':'(.*?)'";
			
			Pattern p = Pattern.compile(keywordRegex);
			Matcher m = p.matcher(html.toString());
			
			int i = 0;
			System.out.print(city + ">>");
			while (m.find()) {
				if(i > 49){
					break;
				}
				descriptions[i] = m.group(1);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return descriptions;
	}
	
	
	public static void main(String args[]) throws Exception{
		
		String projectName = "联想词汇";
		
		BaiduSpotsListProducer bslp = new BaiduSpotsListProducer();
		
		String[] citys = {
				"北京","天津","上海","重庆","石家庄"
				,"郑州","武汉","长沙","南京","南昌",
				"沈阳","长春","哈尔滨","西安","太原",
				"成都","西宁","合肥","海口","广州",
				"贵阳","杭州","福州","台北","兰州",
				"昆明","拉萨","银川","南宁","乌鲁木齐",
				"呼和浩特","香港","澳门"
		};
		for(int i = 0 ; i < citys.length ; i++){
			String []  descriptions = bslp.solveHtmlPage(citys[i]);
			for(int n = 0 ; n<descriptions.length;n++){
				if(descriptions[n] == null || descriptions[n].equals("")){
					System.out.println("数据为空！执行下一次循环");
					break;
				}
				
				String start = "20140831";
				String end = "20150831";
				Date startDate = DateUtils.parseDate(start, new String[] { "yyyyMMdd" });
				Date endDate = DateUtils.parseDate(end, new String[] { "yyyyMMdd" });
				while (true){
					new BaiduWebpageListProducer(projectName, citys[i],descriptions[n], DateFormatUtils.format(startDate, "yyyyMMdd"), DateFormatUtils.format(startDate, "yyyyMMdd")).run();
					if (DateUtils.isSameDay(startDate, endDate)){
						break;
					}
					startDate = DateUtils.addDays(startDate, 1);
				}
			}
		}
	}
	
}
