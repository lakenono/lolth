package lolthx.baidu.webpage;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

@Slf4j
public class BaiduSpotsListFetch  extends DistributedParser {

	private String BAIDU_SPOTS_LIST_FETCH = 	"https://www.baidu.com/s"+
			"?wd={0}"+
			"&pn=750"+
			"&ie=utf-8"+
			"&gpc=stf%3D{1}%2C{2}|stftype%3D2"+
			"&tfflag=1";
	
	@Override
	public String getQueueName() {
		return "baidu_spots_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		String city =  task.getExtra();
		
		String keywordRegex = "'rsv_re_ename':'(.*?)'";
		
		Pattern p = Pattern.compile(keywordRegex);
		Matcher m = p.matcher(result.toString());
		
		while (m.find()) {
			try {
				String keyword = m.group(1);
				String start = "20140831";
				String end = "20150831";
				Date startDate = DateUtils.parseDate(start, new String[] { "yyyyMMdd" });
				Date endDate = DateUtils.parseDate(end, new String[] { "yyyyMMdd" });
				int i = 1;
				while (true){
					try {
						String sat = DateFormatUtils.format(startDate, "yyyyMMdd");
						String url = this.buildUrl(keyword,sat);
						String extra = city + ":" + keyword +":" + sat;
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
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle baidu Spots error : {}",e.getMessage(),e,task.getExtra());
				continue;
			}
			
		}
	}
	
	private String buildUrl(String keyword,String start) throws Exception{
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(start + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });

		String str0 = URLEncoder.encode(keyword, "utf-8");
		String str1 = String.valueOf(startDate.getTime() / 1000);
		String str2 = String.valueOf(endDate.getTime() / 1000);	
		
		return MessageFormat.format(BAIDU_SPOTS_LIST_FETCH, str0 , str1 , str2  );
	}
	
	public static void main(String args[]){
		for(int i = 1; i <= 70 ; i++){
			new BaiduSpotsListFetch().run();
		}
	}
	
}
