package lolthx.autohome.bbs;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.autohome.bbs.bean.AutoHomeBBSBean;
import lolthx.autohome.bbs.bean.AutoHomeBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoHomeBBSListFetch extends DistributedParser {

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
		return "autohome_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		AutoHomeBBSBean bean = null;
		Elements elements = doc.select("div#subcontent dl.list_dl[lang]");
		for (Element element : elements) {
			// 发帖时间
			String postTime = element.select("dd").first().select("span").text();
			if (!isTime(postTime)) {
				continue;
			}
			bean = new AutoHomeBBSBean();
			bean.setPostTime(postTime);

			// title
			String title = element.select("dt a").first().text();
			bean.setTitle(title);

			String type = element.select("dt span").first().attr("class");
			bean.setType(type);

			// url
			String url = element.select("dt a").first().attr("href");
			bean.setUrl("http://club.autohome.com.cn" + url);
			String id = StringUtils.substringBetween(url, "bbs/", ".html");
			bean.setId(id);
			// 作者
			String author = element.select("dd").first().select("a").first().text();
			bean.setAuthor(author);

			// 作者url
			String authorUrl = element.select("dd").first().select("a").first().attr("href");
			String authorId = StringUtils.substringAfter(authorUrl, "cn/");
			bean.setAuthorId(authorId);
			bean.setProjectName(task.getProjectName());
			bean.setKeyword(task.getExtra());
			
			String html = GlobalComponents.fetcher.fetch(bean.getUrl());
			if (StringUtils.isBlank(html)) {
				return;
			}
			Document docDetail = Jsoup.parse(html);

			// views
			String views = docDetail.select("font#x-views").first().text();
			bean.setViews(views);

			// replys
			String replys = docDetail.select("font#x-replys").first().text();
			bean.setReplys(replys);

			// text
			String text = docDetail.select("div.rconten div.conttxt").first().text();
			{
				Elements els= docDetail.select("div.rconten div.conttxt img");
				for (Element el : els) {
					String attr = el.attr("src");
					text = text + " " + attr;
				}
			}

			// 车主信息
			bean.setText(text);
			try {
				bean.saveOnNotExist();
				parseUser(docDetail);
					
				String sendurl = StringUtils.replace(bean.getUrl(), "-1.html", "-{0}.html");
				int maxpage = this.getMaxPage(docDetail);//执行页面用户评论推送
				for(int pagenum = 1 ; pagenum<= maxpage ;pagenum++ ){
					String seUrl = buildUrl(sendurl,pagenum);
					Task newTask = buildTask(seUrl, "autohome_bbs_comment", task);
					Queue.push(newTask);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	private int getMaxPage(Document doc) throws Exception{
		String attr = doc.select("span.gopage span.fs").first().attr("title");
		String page = StringUtils.substringBetween(attr, "共", "页").trim();
		return Integer.valueOf(page);
	}
	
	public String buildUrl(String url,int pageNum){
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	private void parseUser(Document doc) {
		Element topicElement = doc.select("div#maxwrap-maintopic").first();

		AutoHomeBBSUserBean bean = new AutoHomeBBSUserBean();
		Element ulElement = topicElement.select("ul.maxw").first();
		Element a = ulElement.select("li a").first();
		String name = a.text();
		bean.setName(name);
		String url = a.attr("href");
		bean.setAuthorUrl(url);
		String id = StringUtils.substringBetween(url, "cn/", "/home");
		bean.setId(id);
		ulElement = topicElement.select("ul.leftlist").first();
		Elements lis = ulElement.select("li");
		String value;
		int split = 3;
		for (Element li : lis) {
			value = li.text();
			if (value.startsWith("来自")) {
				bean.setArea(StringUtils.substring(value, split));
			} else if (value.startsWith("关注")) {
				bean.setConcern(StringUtils.substring(value, split));
			} else if (value.startsWith("爱车")) {
				bean.setCar(StringUtils.substring(value, split));
			}
		}
		try {
			bean.saveOnNotExist();
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
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
		new AutoHomeBBSListFetch().run();
	}

}
