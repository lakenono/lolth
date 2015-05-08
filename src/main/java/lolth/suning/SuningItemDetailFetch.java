package lolth.suning;

import java.io.IOException;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.suning.bean.SuningItemBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SuningItemDetailFetch extends PageParseFetchTaskHandler {

	public SuningItemDetailFetch() {
		super(SuningSearchListFetch.SUNING_ITEM_DETAIL);
	}
	
	public static void main(String[] args) {
		SuningItemDetailFetch fetch = new SuningItemDetailFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		SuningItemBean bean = null;

		Elements titles = doc.select("div.proinfo-title h1#itemDisplayName");

		if (titles.isEmpty()) {
			return;
		}

		bean = new SuningItemBean();

		String url = task.getUrl();
		String id = StringUtils.substringAfterLast(url, "/");
		id = StringUtils.substringBefore(id, ".");

		bean.setId(id);
		bean.setUrl(url);
		bean.setTaskName(task.getName());
		bean.setKeyword(task.getExtra());

		bean.setTitle(titles.first().text());

		// 价格
		Element price = doc.getElementById("promotionPrice");
		if (price != null) {
			bean.setPrice(price.text());
		}

		// 店铺
		Elements shop = doc.select("span#shopName");
		if (!shop.isEmpty()) {
			String shopStr = shop.first().text();
			shopStr = StringUtils.substringBetween(shopStr, "\"");
			bean.setShop(shopStr);
		}

		Elements trs = doc.select("table#itemParameter tr");
		for (Element tr : trs) {
			String data = tr.text();
			if (StringUtils.startsWith(data, "品牌")) {
				bean.setBrand(StringUtils.trim(StringUtils.substringBetween(data, "品牌","纠错")));
			}

			if (StringUtils.startsWith(data, "产地")) {
				bean.setProduceArea(StringUtils.trim(StringUtils.substringBetween(data, "产地","纠错")));
			}

			if (StringUtils.startsWith(data, "包装")) {
				bean.setPacking(StringUtils.trim(StringUtils.substringBetween(data, "包装","纠错")));
			}

			if (StringUtils.startsWith(data, "段位")) {
				bean.setGrowthStage(StringUtils.trim(StringUtils.substringBetween(data, "段位","纠错")));
			}
		}

		bean.persistOnNotExist();
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.dynamicFetch.document(task.getUrl());
		this.parsePage(doc, task);
	}
	
	

}
