package lolth.weibo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public class WeiboTimeUtils {
	
	/**
	 * 日期形式：48分钟前，03月29日 20:40，今天 08:32
	 * 转换格式：2014-09-29 02:18:02 
	 * @param postTime
	 * @param now
	 * @return
	 */
	public static  String getNormalTime(String postTime, LocalDateTime now) {
		//48分钟前
		if (StringUtils.contains(postTime, "分钟前")) {
			postTime = StringUtils.substringBefore(postTime, "分钟前");
			postTime = StringUtils.trim(postTime);
			if (StringUtils.isNumeric(postTime)) {
				return now.minusMinutes(Long.parseLong(postTime)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}

		}
		
		//今天 08:32
		if (StringUtils.contains(postTime, "今天")) {
			String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			return StringUtils.replaceOnce(postTime, "今天", date)+":00";
			
		}
		
		//03月29日 20:40
		if(StringUtils.contains(postTime, "月")){
			postTime = StringUtils.replaceChars(postTime, '月','-');
			postTime = StringUtils.remove(postTime, '日');
			return now.getYear()+"-"+postTime+":00";
		}
		
		return postTime;
	}
	
	public static boolean isBefore(String time,String timeRange){
		LocalDateTime timeObj = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime timeRangeObj = LocalDateTime.parse(timeRange, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return timeObj.isBefore(timeRangeObj);
	}
	
	public static void main(String[] args) {
		String[] times = {"48分钟前","03月29日 20:40","2014-09-29 02:18:02","今天 08:32"};
		for(String t : times){
			String normalTime = getNormalTime(t,LocalDateTime.now());
			System.out.println(normalTime);
			System.out.println(isBefore(normalTime,"2014-10-01 00:00:00"));
			System.out.println("===========");
		}
		
	}
}
