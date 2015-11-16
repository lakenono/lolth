package lolthx.onlylady.bbs.search;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.onlylady.bbs.bean.OnlyladyUserBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlyladyBBSUser extends DistributedParser {

	@Override
	public String getQueueName() {
		return "onlylady_bbs_user";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "uid-", ".html");
		if (StringUtils.isBlank(id)) {
			log.info("onlylady user id is empty : " + url);
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements td = doc.select("td.on_userinfo");
		if(td.isEmpty()){
			log.info("onlylady user page is empty!");
			return;
		}
		
		OnlyladyUserBean bean = new OnlyladyUserBean();
		bean.setUrl(url);
		bean.setId(id);
		bean.setUserName(td.select("h3").text());
		Elements ps = td.select("p");
		for (Element p : ps) {
			String value = p.text();
			if (value.startsWith("性别")) {
				bean.setSex(value.substring(3));
			} else if (value.startsWith("城市")) {
				bean.setCity(value.substring(3));
			} else if (value.startsWith("级别")) {
				bean.setLevel(value.substring(3));
			} else if (value.startsWith("出生")) {
				bean.setBirthday(value.substring(5));
			}
		}
		bean.setInterest(td.select("dl > dd").text());
		bean.saveOnNotExist();
//		System.out.println(bean.toString());
	}

	public static void main(String[] args) throws Exception {
//		String url = "http://bbs.onlylady.com/space-uid-12339457.html";
//		String fetch = GlobalComponents.jsoupFetcher.fetch(url);
//		Document parse = Jsoup.parse(fetch);
//		OnlyladyUserBean bean = new OnlyladyUserBean();
//		Elements td = parse.select("td.on_userinfo");
//		bean.setUrl(url);
//		bean.setId(StringUtils.substringBetween(url, "uid-", ".html"));
//		bean.setUserName(td.select("h3").text());
//		Elements ps = td.select("p");
//		for (Element p : ps) {
//			String value = p.text();
//			if (value.startsWith("性别")) {
//				bean.setSex(value.substring(3));
//			} else if (value.startsWith("城市")) {
//				bean.setCity(value.substring(3));
//			} else if (value.startsWith("级别")) {
//				bean.setLevel(value.substring(3));
//			} else if (value.startsWith("出生")) {
//				bean.setBirthday(value.substring(5));
//			}
//		}
//		bean.setInterest(td.select("dl > dd").text());
//
//		System.out.println(bean.toString());
		Random r =new Random();
		for(int i = 0;i<20;i++){
			int nextInt = r.nextInt(9999);
			System.out.println(nextInt);
			new OnlyladyBBSUser().run();
			Thread.sleep(nextInt);
		}
	}

}
