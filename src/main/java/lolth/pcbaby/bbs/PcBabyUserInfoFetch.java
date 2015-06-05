package lolth.pcbaby.bbs;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.pcbaby.bbs.Bean.UserInfoBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PcBabyUserInfoFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "pcbaby_userinfo";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		String url = task.getUrl();
		UserInfoBean infoBean = new UserInfoBean();
		String id = StringUtils.substringBetween(url, "id/", "/bbs");
		infoBean.setId(id);
		infoBean.setUrl(url);
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("div#myInfo");
		parseUser(elements, infoBean);
		elements = doc.select("div#myBaby");
		parseUserBaby(elements, infoBean);

		log.debug(infoBean.toString());
	}

	// 用户宝宝信息
	private void parseUserBaby(Elements elements, UserInfoBean infoBean) {
		if (elements.isEmpty()) {
			return;
		}
		Elements tmp = elements.select("div.n_title02 p");
		if (!tmp.isEmpty()) {
			String str;
			String value;
			for (Element element : tmp) {
				str = element.text();
				value = element.select("span").text();
				if (str.indexOf("姓名") > -1) {
					infoBean.setBabyName(infoBean.getBabyName() == null ? value : infoBean.getBabyName() + "|" + value);
				} else if (str.indexOf("性别") > -1) {
					infoBean.setBabySex(infoBean.getBabySex() == null ? value : infoBean.getBabySex() + "|" + value);
				} else if (str.indexOf("年龄") > -1) {
					infoBean.setBabySex(infoBean.getBabyAge() == null ? value : infoBean.getBabyAge() + "|" + value);
				} else if (str.indexOf("生日") > -1) {
					infoBean.setBabyBirthday(value);
				} else if (str.indexOf("生肖") > -1) {
					infoBean.setBabyZodiac(value);
				} else if (str.indexOf("星座") > -1) {
					infoBean.setBabyConstellation(value);
				}
			}
		}

	}

	// 用户信息
	private void parseUser(Elements elements, UserInfoBean infoBean) {
		if (elements.isEmpty()) {
			return;
		}

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

	}

}
