package lolthx.autohome.bbs;

import java.sql.SQLException;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.autohome.bbs.bean.AutoHomeBBSBean;
import lolthx.autohome.bbs.bean.AutoHomeBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoHomeBBSTopicFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "autohome_bbs_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		AutoHomeBBSBean bean = new AutoHomeBBSBean();
		String id = StringUtils.substringBetween(task.getUrl(), "bbs/", ".html");
		bean.setId(id);
		// views
		String views = doc.select("font#x-views").first().text();
		bean.setViews(views);

		// replys
		String replys = doc.select("font#x-replys").first().text();
		bean.setReplys(replys);

		// text
		String text = doc.select("div.rconten div.conttxt").first().text();
		{
			Elements elements = doc.select("div.rconten div.conttxt img");
			for (Element element : elements) {
				String attr = element.attr("src");
				text = text + " " + attr;
			}
		}

		// 车主信息
		bean.setText(text);
		try {
			bean.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		parseUser(doc);
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
		} catch (IllegalArgumentException | IllegalAccessException |  SQLException e) {
			e.printStackTrace();
		}
	}

}
