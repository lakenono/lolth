package lolth.pcpaby.bbs;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.core.GlobalComponents;
import lolth.pcbaby.bbs.Bean.TopicBean;
import lolth.pcbaby.bbs.Bean.UserInfoBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class PcBabyTest {

	@Test
	public void testFetchMaxPage() throws IOException, InterruptedException {
		String url = "http://ks.pcbaby.com.cn/kids_bbs.shtml?q=%BB%DD%CA%CF&menu=&pageNo=2";
		Document document = GlobalComponents.fetcher.document(url);
		Elements elements = document.select("div.main > p > em.red");
		if (!elements.isEmpty()) {
			int page;
			String text = elements.first().text();
			int parseInt = Integer.parseInt(text);
			if (parseInt >= 50 * 15) {
				page = 50;
			} else {
				page = parseInt / 15;
				page = page + (parseInt % 15 != 0 ? 1 : 0);
			}
			System.out.println(page);
		}

	}

	@Test
	public void testFetchList() throws IOException, InterruptedException {
		String url = "http://ks.pcbaby.com.cn/kids_bbs.shtml?q=%BB%DD%CA%CF&pageNo=5";
		Document doc = GlobalComponents.fetcher.document(url);
		Elements elements = doc.select("ul#JaList > li");
		if (elements.isEmpty()) {
			return;
		}
		Elements link = null;
		for (Element element : elements) {
			link = element.select("dd.oh a");
			if (link.isEmpty()) {
				continue;
			}
			String lin = link.attr("href");
			System.out.println(lin);
		}
	}

	@Test
	public void testFetchTopic() throws Exception {
		String url = "http://bbs.pcbaby.com.cn/topic-1926367.html";
		Document doc = GlobalComponents.dynamicFetch.document(url);
		// Document doc = GlobalComponents.fetcher.document(url);
		// System.out.println(doc);
		// div.post_list_top
		TopicBean topicBean = new TopicBean();
		// 头部信息
		Elements head = doc.select("div.post_list_top");
		Elements tmp = head.select("p.overView");
		if (!tmp.isEmpty()) {
			Elements spans = tmp.select("span");
			for (Element span : spans) {
				if ("views".equals(span.attr("id"))) {
					topicBean.setViews(span.text());
				} else {
					topicBean.setReply(span.text());
				}
			}
		}

		tmp = head.select("h1");
		if (!tmp.isEmpty()) {
			topicBean.setTitle(tmp.text());
		}
		// 楼主帖子
		head = doc.select("div.post_wrap_first");
		// 帖子左边
		tmp = head.select("th.post_left ");
		//
		Elements leftTmp = tmp.select("dt.fb a");
		if (!leftTmp.isEmpty()) {
			String tm = leftTmp.attr("href");
			topicBean.setNickName(leftTmp.text());
			String id = StringUtils.substringBetween(tm, "id/", "/bbs");
			topicBean.setUserId(id);
		}

		// 帖子右边
		tmp = head.select("td.post_right ");
		if (!tmp.isEmpty()) {
			// 帖子发表时间
			String time = tmp.select("div.post_info").text();
			Pattern p = Pattern.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}");
			Matcher m = p.matcher(time);
			while (m.find()) {
				String date = m.group();
				topicBean.setPostTime(date);
			}
			// 楼主的帖子正文
			topicBean.setText(tmp.select("div.replyBody").text());
		}
		System.out.println(topicBean.toString());

		Elements select = doc.select("div.pager > i");

		String page = StringUtils.substringBetween(select.first().text(), "/", "页");
		System.out.println(page);
		// div.post_wrap_first
	}

	@Test
	public void fetchUserInfo() throws Exception {
		String url = "http://my.pcbaby.com.cn/31354349/home/";
		// Document doc = GlobalComponents.dynamicFetch.document(url);
		Document doc = GlobalComponents.fetcher.document(url);
		// System.out.println(doc);
		UserInfoBean infoBean = new UserInfoBean();
		Elements elements = doc.select("div#myInfo");
		Elements tmp = elements.select("div.p1r1 p");
		if (!tmp.isEmpty()) {
			for (Element element : tmp) {
				Elements e = element.select("span.tex");
				String str;
				String value;
				if (e.isEmpty()) {
					str = element.text();
					value = element.select("span").text();
					if (str.indexOf("来自") > -1) {
						infoBean.setAddress(value);
					} else if (str.indexOf("性别") > -1) {
						infoBean.setSex(value);
					} else if (str.indexOf("年龄") > -1) {
						infoBean.setAge(value);
					}
				} else {
					infoBean.setUserName(e.select("a").text());
				}
			}
		}
		
		tmp = doc.select("div.n_title02 p");
		if (!tmp.isEmpty()) {
			String str;
			String value;
			for (Element element : tmp) {
				str = element.text();
				value = element.select("span").first().text();
				if (str.indexOf("姓名") > -1) {
					infoBean.setBabyName(infoBean.getBabyName() == null ? value : infoBean.getBabyName() + "|" + value);
				} else if (str.indexOf("性别") > -1) {
					infoBean.setBabySex(infoBean.getBabySex() == null ? value : infoBean.getBabySex() + "|" + value);
				} else if (str.indexOf("年龄") > -1) {
					infoBean.setBabyAge(infoBean.getBabyAge() == null ? value : infoBean.getBabyAge() + "|" + value);
				} else if (str.indexOf("生日") > -1) {
					infoBean.setBabyBirthday(value);
				} else if (str.indexOf("生肖") > -1) {
					infoBean.setBabyZodiac(value);
				} else if (str.indexOf("星座") > -1) {
					infoBean.setBabyConstellation(value);
				}
			}
		}
		System.out.println(infoBean.toString());

	}

	@Test
	public void sss() {
		String url = "http://bbs.pcbaby.com.cn/topic-1926367.html";
		String ss = StringUtils.substringBeforeLast(url, ".html") + "-{0}.html";
		System.out.println(ss);
		String id = StringUtils.substringBetween(url, "cn/", ".html");
		System.out.println(id);
		String tt = MessageFormat.format(ss, 2);
		System.out.println(tt);
		System.out.println(StringUtils.substringBetween(tt, "cn/", "-"));
		ss = "来自：sff";
		String ff = StringUtils.substringAfter(ss, "：");
		System.out.println(ff);
		// String ss = "发表于 2015-06-04 11:22 | 只看楼主 | 申请精华";
		// Pattern p =
		// Pattern.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}");
		// Matcher m = p.matcher(ss);
		// while (m.find()) {
		// String date = m.group();
		// System.out.println(date);
		// }

	}
}
