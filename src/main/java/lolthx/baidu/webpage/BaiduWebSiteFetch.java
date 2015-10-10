package lolthx.baidu.webpage;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

@Slf4j
public class BaiduWebSiteFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_web_site_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String start = format.format(task.getStartDate());
		String end = format.format(task.getEndDate());
		Date startDate = format.parse(start);
		Date endDate = format.parse(end);
		
		String setExtra = task.getExtra();
		
		int i = 0;
		while (true){
			
			try {
				String sat = DateFormatUtils.format(startDate, "yyyyMMdd");
				
				String url = this.buildUrl(task.getUrl(),sat);
				String extra = setExtra +":" + sat;
				task.setExtra(extra);
				Task newTask = buildTask(url, "baidu_web_page_list", task);
				Queue.push(newTask);
				if (DateUtils.isSameDay(startDate, endDate) || i >= 400){
					break;
				}
				startDate = DateUtils.addDays(startDate, 1);
				i++;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle baidu Spots inner error : {}",e.getMessage(),e,task.getExtra());
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
	
	
	public static void main(String args[]){
		for(int i = 1; i <= 50 ; i++){
			new BaiduWebSiteFetch().run();
		}
	}

}
