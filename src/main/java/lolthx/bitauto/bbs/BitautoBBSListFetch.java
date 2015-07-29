package lolthx.bitauto.bbs;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolth.autohome.newbbs.bean.AutoHomeBBSBean;
import lolthx.bitauto.bean.BitautoBBSBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoBBSListFetch extends DistributedParser {
	
	private static Date start = null;//开始时间
	private static Date end = null;//结束时间
	
	static {
		try {
			start = DateUtils.parseDate("2015-02-28", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-07-28", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getQueueName() {
		return "bitauto_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		BitautoBBSBean bean  = null;
		Elements elements = doc.select("div.postscontent>div.postslist_xh");
		
		System.out.println("elements Size = " + elements.size());
		
		//循环列表元素
		for (Element element : elements) {
			// 发帖时间
			String postTime = element.select("li.zhhf").first().text();
			
			if (!isTime(postTime)) {
				continue;
			}
			
			bean =  new BitautoBBSBean();
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
			
			//是否必须带thread？主ID StringUtils.substringBefore(task.getExtra(), ":")
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
			bean.setKeyword(task.getExtra());
			try {
				if (bean.persistOnNotExist()) {
					Task newTask = buildTask(bean.getUrl(), "bitauto_bbs_topic", task);
					Queue.push(newTask);
				}
			} catch (Exception e) {

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

	public static void main(String[] args) throws Exception {
		new BitautoBBSListFetch().run();
	}
	
}
