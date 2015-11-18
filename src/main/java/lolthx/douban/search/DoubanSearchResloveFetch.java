package lolthx.douban.search;

import java.text.MessageFormat;
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

public class DoubanSearchResloveFetch extends DistributedParser{
	
	@Override
	public String getQueueName() {
		return "douban_search_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		String extra = task.getExtra(); 
		
		Elements els = doc.select("div.article div.topics table.olt tr.pl");
		for(Element el : els){
			String postTime = el.select("td.td-time").attr("title").split(" ")[0];
			if (!isTime(postTime,task.getStartDate(),task.getEndDate())) {
				continue;
			}
			String detailurl = el.select("td.td-subject a").attr("href");
			
			
			String replys = el.select("td.td-reply span").text();
			String reply = StringUtils.substringBefore(replys, "回应");
			String extras = extra + ":" + reply;
			String detailQueuename = "douban_search_detail";
			
			task.setExtra(extras);
			Task detailTask = buildTask(detailurl, detailQueuename , task);
			Queue.push(detailTask);
			
			
			if( Integer.valueOf(reply) > 0){
				String sendUrl = detailurl +"?start={0}";
				Integer replayPages = Integer.valueOf(reply) / 100 + 1;
				String commentQuenename = "douban_search_comment";
				for(int i = 1 ; i <= replayPages ; i++ ){
					String commentUrl = this.buildUrl(sendUrl, i );
					Task commentTask = buildTask(commentUrl, commentQuenename , task);
					Queue.push(commentTask);
				}
			}
		}
	}
	
	public String buildUrl(String url , int pageNum){
		return MessageFormat.format(url, String.valueOf((pageNum - 1) * 100));
	}
	
	private boolean isTime(String time,Date start,Date end) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
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
		for(int i = 1 ;i <= 1;i++){
			new DoubanSearchResloveFetch().run();
		}
	}
	
}
