package lolthx.baidu.visualize;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.baidu.webpage.BaiduWebSiteFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduWebpageVisFirstFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "baidu_webpage_vis_first";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String start = format.format(task.getStartDate());
		String end = format.format(task.getEndDate());
		Date startDate = format.parse(start);
		Date endDate = format.parse(end);

		int i = 0;
		while (true){
			try {
				String sat = DateFormatUtils.format(startDate, "yyyyMMdd");
				
				String url = this.buildUrl(task.getUrl(),sat);
				Task newTask = buildTask(url, "baidu_webpage_vis_second", task);
				Queue.push(newTask);
				if (DateUtils.isSameDay(startDate, endDate) || i >= 400){
					break;
				}
				startDate = DateUtils.addDays(startDate, 1);
				i++;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle baidu webpage vis for error : {}",e.getMessage(),e,task.getExtra());
				continue;
			}
		}
		
	}
	
	private String buildUrl(String url,String start) throws Exception{
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(start + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });

		String str1 = String.valueOf(startDate.getTime() / 1000);
		String str2 = String.valueOf(endDate.getTime() / 1000);	
		
		return MessageFormat.format(url,  "" , str1 , str2  );
	}
	
	
	public static void main(String[] args){
		for(int i = 1; i<= 200 ; i++){
			new BaiduWebpageVisFirstFetch().run();
		}
	}
	

}
