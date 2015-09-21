package lolthx.suning.keysearch;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.suning.bean.SuningItemBean;

public class SuningSearchItemDetailFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "suning_search_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
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
		bean.setProjectName(task.getProjectName());
		bean.setKeyword(task.getExtra());

		bean.setTitle(titles.first().text());

		// 价格
		Element price = doc.getElementById("promotionPrice");
		if (price != null) {
			bean.setPrice(price.text());
		}

		// 店铺
		Elements shop = doc.select("div.nopro-attr.clearfix span#shopName");
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
			
			/**
			if (StringUtils.startsWith(data, "段位")) {
				bean.setGrowthStage(StringUtils.trim(StringUtils.substringBetween(data, "段位","纠错")));
			}
			*/
		}
		
		//String commentSize = doc.select("div#productCommTitle").text();
		
		bean.saveOnNotExist();
		
	}
	
	public static void main(String args[]){
		for(int i = 1 ; i<=10;i++){
			new SuningSearchItemDetailFetch().run();
		}
	}
	
}
