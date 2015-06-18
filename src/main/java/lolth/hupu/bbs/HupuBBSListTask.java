package lolth.hupu.bbs;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

public class HupuBBSListTask extends DistributedParser {

	private static Date start = null;
	private static Date end = null;
	
	static{
		try {
			start = DateUtils.parseDate("2014-10-30", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-05-01", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getQueueName() {
		return "hupu_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("#pl tbody tr[mid]");
		for(Element element: elements){
			Elements tmp = element.select("td.p_author");
			Node childNode = tmp.first().childNode(2);
			String time = childNode.toString();
			if(StringUtils.isBlank(time)){
				continue;
			}
			if(!isTime(time)){
				continue;
			}
			String href = element.select("td.p_title a").attr("href");
			Task newTask = buildTask("http://bbs.hupu.com"+href,"hupu_bbs_topic",task);
			Queue.push(newTask);
			newTask= buildTask(tmp.first().select("a").attr("href"),"hupu_bbs_user",task);
			Queue.push(newTask);
		}
	}

	private boolean isTime(String time) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
			return between(start,end,srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean between(Date beginDate, Date endDate, Date src){
		return beginDate.before(src) && endDate.after(src); 
	}

}
