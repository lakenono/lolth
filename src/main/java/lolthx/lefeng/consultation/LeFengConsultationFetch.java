package lolthx.lefeng.consultation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.lefeng.bean.LeFengConsultationBean;

public class LeFengConsultationFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "lefeng_item_consultation";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		String itemId = doc.select("#productId").attr("value");
		Elements wens = doc.select("div.spzx > div.wen");
		Elements das = doc.select("div.spzx > div.da");
		Element tmp;
		LeFengConsultationBean bean = null;
		for (int i = 0; i < wens.size(); i++) {
			bean = new LeFengConsultationBean();
			bean.setItemId(itemId);
			tmp = wens.get(i);
			bean.setWenNick(tmp.select("span>i").text());
			bean.setWenTime(tmp.select("span>em").text());
			bean.setWenText(tmp.select("b").text());

			tmp = das.get(i);
			bean.setDaText(tmp.select("span").text());
			bean.setDaTime(tmp.select("em").text());
			bean.setUrl(task.getUrl());

			try {
				bean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
