package lolthx.lefeng.comment;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.lefeng.bean.LeFengCommentBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LeFengCommentFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "lefeng_item_comment";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		Elements divs = doc.select("div.photo.ClearFix");
		String[] temps = null;
		LeFengCommentBean bean = null;
		for (Element div : divs) {
			bean = new LeFengCommentBean();
			String classStr = div.attr("class");
			if(classStr.equals("photo ClearFix")){
				bean.setType("1");
			}else{
				bean.setType("0");
			}
			bean.setUrl(task.getUrl());
			bean.setNick(div.select("div.pho_left > p").text());
			bean.setScore(div.select("div.star_big > span").attr("class"));
			bean.setText(div.select("div.pj_text").text());
			String time = div.select("span.ctime").text();
			if (StringUtils.isNumeric(time)) {
				bean.setTime(DateFormatUtils.format(Long.parseLong(time), "yyyy-MM-dd HH:mm:ss"));
			}
			String useful = div.select("div.zy_bt a[id$=\"_vote\"]").text();
			if (StringUtils.isNoneBlank(useful)) {
				bean.setUseful(StringUtils.substring(useful, 3, useful.length() - 1));
			}
			String href = div.select("div.zy_bt a.reply_bt").attr("href");
			href = StringUtils.substringBetween(href, "reply/", ".html");
			temps = StringUtils.split(href, "-");
			if (temps.length > 1) {
				bean.setId(temps[1]);
				bean.setItemId(temps[0]);
			}
			try {
				bean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
