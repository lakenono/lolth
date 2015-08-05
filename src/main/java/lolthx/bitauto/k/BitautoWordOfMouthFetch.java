package lolthx.bitauto.k;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.bitauto.bean.BitautoWordOfMouthBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoWordOfMouthFetch extends DistributedParser {
	
	private static Date start = null;
	private static Date end = null;

	static {
		try {
			start = DateUtils.parseDate("2014-07-22", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-07-21", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getQueueName() {
		return "bitauto_kb_list";
	}

	@Override 
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
			
		Document doc = Jsoup.parse(result);
		BitautoWordOfMouthBean bean  = null;
		Elements elements = doc.select("div.postscontent div.postslist_xh");
			
		//循环列表元素 循环数据 根据elements先存储一部分数据到数据库
		for (Element element : elements) {
			String postTime = element.select("li.zhhf").first().text();
			if (!isTime(postTime)) {
				continue;
			}
			
			bean = new BitautoWordOfMouthBean();
			bean.setPostTime(postTime);
			
			// title
			String title = element.select("li.bt a span").text().trim();
			bean.setTitle(title);
						
			//类型，是否为精品帖子 等 
			String type = element.select("li.tu a").attr("class");
			bean.setType(type);

			// url
			String url = element.select("li.bt a").attr("href");
			bean.setUrl(url);
						
			//是否必须带thread？主ID
			String id = StringUtils.substringBetween(url, "-", ".html");
			bean.setId(id);
						
			// 作者
			String author = element.select("li.zz a").text().trim();
			bean.setAuthor(author);

			// 作者url
			String authorUrl = element.select("li.zz a").attr("href");
			String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");
						
			bean.setAuthorId(authorId);
			bean.setProjectName(task.getProjectName());
			bean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
			bean.setKeyword(StringUtils.substringAfter(task.getExtra(),":"));
			
			try {
				if (bean.persistOnNotExist()) {
					Task newTask = buildTask(bean.getUrl(), "bitauto_kb_topic", task);
					Queue.push(newTask);
				}
			} catch (Exception e) {
				System.out.println("有问题-O(∩_∩)O~");
			}	
		}
		
	}

	
	private boolean isTime(String time) {
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

	public static void main(String args[]){
		new BitautoWordOfMouthFetch().run();
	}
	
}
