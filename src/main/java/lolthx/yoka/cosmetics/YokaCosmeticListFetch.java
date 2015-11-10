package lolthx.yoka.cosmetics;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YokaCosmeticListFetch extends DistributedParser {
	
	@Override
	public String getQueueName() {
		return "yoka_cosmetic_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result); 
		
		Date start = task.getStartDate();
		Date end = task.getEndDate();
		
		String extra = task.getExtra();
		Elements elements = doc.select("div#cp-iwom-list div.iwom");
		for(Element el : elements){
			String postTime = el.select("span.date").first().text().trim();
			if (!isTime(postTime,start,end)) {
				continue;
			}
			
			String userUrl = el.select("dl.user_info dd h2 a").first().attr("href");
			Task userTask = buildTask(userUrl, "yoka_cosmetic_user", task);
			Queue.push(userTask);
			
			String sendExtra = extra + ":" + el.select("dl.user_info div.pf div.r").text();
			sendExtra = sendExtra.replace("更多", "");
			task.setExtra(sendExtra);
			String sendUrl  = "http://brand.yoka.com/" + el.select("div.user_txt > h2 > a").attr("href"); 
			Task sendTask = buildTask(sendUrl, "yoka_cosmetic_detail", task);
			Queue.push(sendTask);
		}
		
	}

	private boolean isTime(String time,Date start,Date end) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd hh:mm");
			return between(start, end, srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}
	
	public static void main(String[] args){
		for(int i = 1; i <= 100 ; i++){
			new YokaCosmeticListFetch().run();
		}

	}
	
}
