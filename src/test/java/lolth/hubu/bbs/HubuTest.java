package lolth.hubu.bbs;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.hupu.bbs.bean.TopicBean;
import lolth.hupu.bbs.bean.UserBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HubuTest {
	private String host = "http://bbs.hupu.com";

	@Test
	public void testFetchList() throws IOException, InterruptedException, ParseException {
		Date start = DateUtils.parseDate("2014-10-30", "yyyy-MM-dd");
		Date end = DateUtils.parseDate("2015-05-01", "yyyy-MM-dd");
		String url = "http://bbs.hupu.com/cuba-2";
		Document doc = GlobalComponents.fetcher.document(url);
		// #pl
		// #pl > tbody:nth-child(2)
		Elements elements = doc.select("#pl tbody tr[mid]");
		for (Element element : elements) {
			String href = element.select("td.p_title a").attr("href");
			System.out.println(host + href);
			Elements tmp = element.select("td.p_author");
			Node childNode = tmp.first().childNode(2);
			String time = childNode.toString();
			Date parseDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
			if (start.before(parseDate) && end.after(parseDate)) {
				System.out.println(time);
				System.out.println(tmp.first().select("a").attr("href"));
			}
		}

	}

	@Test
	public void testFetchTopic() throws IOException, InterruptedException {
		String url = "http://bbs.hupu.com/12358986.html";
		Document doc = GlobalComponents.fetcher.document(url);
		TopicBean topicBean = new TopicBean();
		// #t_main > div.bbs_head
		Elements head = doc.select("#t_main > div.bbs_head");
		String title = head.select("div.bbs-hd-h1 > h1").text();
		topicBean.setTitle(title);
		String tmp = head.select("div.bbs-hd-h1 > span").text();
		String[] split = tmp.split(" ");
		if (split.length == 2) {
			if (split[0].indexOf("/") > -1) {
				String[] spl = split[0].split("/");
				topicBean.setReply(spl[0].substring(0, spl[0].length() - 2));
				topicBean.setBright(spl[1].substring(0, spl[1].length() - 1));
			} else {
				topicBean.setReply(split[0].substring(0, split[0].length() - 2));
			}
			topicBean.setBrowse(split[1].substring(0, split[1].length() - 2));
		}
		// #tpc
		Elements tpc = doc.select("#tpc");
		String uid = tpc.select("div.user > div").attr("uid");
		topicBean.setUserId(uid);
		topicBean.setUrl(url);
		String time = tpc.select("div.floor_box > div.author > div.left > span.stime").text();
		topicBean.setTime(time);
		String text = tpc.select("div.floor_box > table.case > tbody > tr > td").text();
		topicBean.setText(text);
		System.out.println(topicBean.toString());
		// String uurl = tpc.select("div.user > div > a").attr("href");
	}

	@Test
	public void testFetchUser() throws IOException, InterruptedException {
		String url = "http://my.hupu.com/3497607";
		Document doc = GlobalComponents.fetcher.document(url);
		String uid = doc.select("#uid").attr("value");
		UserBean userBean = new UserBean();
		userBean.setId(uid);
		userBean.setUrl(url);
		
		Elements main = doc.select("#main");
		if(main.isEmpty()){
			return;
		}
		String name = main.select("h3.mpersonal > div").text();
		userBean.setUsername(name);
		Elements div = main.select("div.personalinfo");
		if(div.isEmpty()){
			return;
		}
		List<Node> childNodes = div.first().childNodes();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < childNodes.size(); i++) {
			Node node = childNodes.get(i);
			if (node instanceof Element) {
				Element tmp = (Element) node;
				if ("br".equals(tmp.tagName())) {
					sb.append("\t");
					continue;
				}
				if (StringUtils.isNotBlank(tmp.text())) {
					sb.append(StringUtils.deleteWhitespace(tmp.text()));
				}
			} else if (node instanceof TextNode) {
				TextNode tmp = (TextNode) node;
				if (StringUtils.isNotBlank(tmp.text())) {
					sb.append(StringUtils.deleteWhitespace(tmp.text()));
				}
			}
		}
		String [] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(sb.toString(), "\t");
		String [] tmp = null;
		sb.setLength(0);
		for (String token : tokens) {
			tmp = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, "：");
			if(tmp.length>1){
				if(tmp[0].indexOf("性")>-1){
					userBean.setSex(tmp[1]);
				}else if(tmp[0].indexOf("地")>-1){
					userBean.setAddress(tmp[1]);
				}else if(tmp[0].indexOf("主队")>-1){
					sb.append(tmp[1]).append("|");
				}else if(tmp[0].indexOf("团队")>-1){
					userBean.setTeam(tmp[1]);
				}else if(tmp[0].indexOf("职务")>-1){
					userBean.setDuty(tmp[1]);
				} else if(tmp[0].indexOf("等级")>-1){
					userBean.setLevel(tmp[1]);
				} else if(tmp[0].indexOf("卡")>-1) {
					userBean.setCalorie(tmp[1]);
				} else if(tmp[0].indexOf("在线")>-1){
					userBean.setOnlineTime(tmp[1]);
				} else if(tmp[0].indexOf("加入")>-1){
					userBean.setJoinedTime(tmp[1]);
				}else if(tmp[0].indexOf("登录")>-1){
					userBean.setRegistering(tmp[1]);
				}
				System.out.println(tmp[0]+","+tmp[1]);
			}
		}
		if(sb.length() > 1){
			userBean.setHomeTeam(sb.substring(0,sb.length()-1));
		}
		//#main > div.person_set > div.brief.m5px
		userBean.setInterest(main.select("div.brief.m5px").text());
		System.out.println(userBean.toString());
	}

	@Test
	public void test() throws ParseException {
		Date parseDate = DateUtils.parseDate("2014-11-00", "yyyy-MM-dd");
		Date start = DateUtils.parseDate("2014-10-30", "yyyy-MM-dd");
		Date end = DateUtils.parseDate("2015-05-01", "yyyy-MM-dd");
		if (start.before(parseDate) && end.after(parseDate)) {
			System.out.println(parseDate);
		}
	}
}
