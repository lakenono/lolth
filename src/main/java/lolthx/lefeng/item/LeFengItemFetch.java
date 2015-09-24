package lolthx.lefeng.item;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.lefeng.bean.LeFengItemBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 动态抓取
 * 
 * @author gbs
 *
 */
public class LeFengItemFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "lefeng_item_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		LeFengItemBean bean = new LeFengItemBean();
		bean.setUrl(task.getUrl());
		bean.setProjectName(task.getProjectName());
		bean.setKeyword(task.getExtra());

		String title = doc.select("h1 > span").text();
		bean.setTitle(title);

		Elements inputs = doc.select("input");
		String price = inputs.select("#uprice").attr("value");
		if (StringUtils.isBlank(price)) {
			price = doc.select("p.specials > span").text();
			price = price.substring(5);
		}
		bean.setPrice(price);
		String id = inputs.select("#productId").attr("value");
		bean.setId(id);

		String goodCommentPercent = doc.select("table.hpl b").text();
		bean.setGoodCommentPercent(goodCommentPercent + "%");

		Elements lis = doc.select("div.tag_wm.wrap_tag li");
		if (lis.isEmpty()) {
			Thread.sleep(1000);
			String tmpUrl = "http://review.lefeng.com/review/" + bean.getId() + "-0-0-a-1.html";
			Document document = GlobalComponents.fetcher.document(tmpUrl);
			bean.setGoodCommentPercent(document.select("#haoping").attr("value"));
			lis = document.select("div.tag_wm li");
		}
		String value = "";
		for (Element li : lis) {
			value = li.text();
			if (StringUtils.contains(value, "口碑评价")) {
				bean.setComment(value.substring(5, value.length() - 1));
			} else if (StringUtils.contains(value, "全部评价")) {
				bean.setComment(value.substring(5, value.length() - 1));
			} else if (StringUtils.contains(value, "好评")) {
				bean.setGoodComment(value.substring(3, value.length() - 1));
			} else if (StringUtils.contains(value, "中评")) {
				bean.setInComment(value.substring(3, value.length() - 1));
			} else if (StringUtils.contains(value, "差评")) {
				bean.setBadComment(value.substring(3, value.length() - 1));
			}
		}

		bean.saveOnNotExist();

		// 咨询
		// String wytw = doc.select("div.wytw > h4 > span > a > em").text();
		// wytw = StringUtils.substring(wytw, 1, wytw.length()-1);
		//
		// pushUrl(wytw, bean.getId(), "lefeng_item_consultation",
		// "http://product.lefeng.com/goods/{0}-0-0-all-{1}.html", task);
		// 评论
		pushUrl(bean.getComment(), bean.getId(), "lefeng_item_comment", "http://review.lefeng.com/review/{0}-0-0-a-{1}.html", task);

	}

	private void pushUrl(String countStr, String id, String queueName, String urlStr, Task task) {
		if (!StringUtils.isNumeric(countStr)) {
			return;
		}
		int count = Integer.parseInt(countStr);
		int size = 20;
		int page = count / size;
		if (count % size != 0) {
			page += 1;
		}
		Task newTask = null;
		for (int i = 1; i <= page; i++) {
			String url = MessageFormat.format(urlStr, id, String.valueOf(i));
			newTask = buildTask(url, queueName, task);
			Queue.push(newTask);
		}
	}
}
