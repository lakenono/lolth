package lolthx.autohome.bbs;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.autohome.bbs.bean.AutoHomeBBSBean;

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
			try {
				if (bean.persistOnNotExist()) {
					Task newTask = buildTask(bean.getUrl(), "autohome_bbs_topic", task);
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

}
