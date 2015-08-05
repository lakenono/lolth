package lolthx.taobao.shop;

import java.util.HashMap;
import java.util.Map;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.taobao.shop.bean.TaobaoShopBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TaobaoShopFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "taobao_item_shop";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		String type = task.getExtra();
		Elements tmp;
		TaobaoShopBean bean;
		if ("1".equals(type)) {
			tmp = doc.select("div#header div#headerCon div#shopExtra");
			if (tmp.isEmpty()) {
				return;
			}
			bean = parseTmallShop(tmp);
		} else {
			tmp = doc.select("div#header-content > div.shop-summary.J_TShopSummary");
			if (tmp.isEmpty()) {
				return;
			}
			bean = parseTaobaoShop(tmp);
		}

		if (bean == null) {
			return;
		}

		bean.setType(type);
		String title = doc.title();
		bean.setName(StringUtils.substringBetween(title, "-"));
		String datas = doc.head().select("meta[name=microscope-data]").attr("content");
		Map<String, String> stringToMap = stringToMap(datas);
		bean.setUserId(stringToMap.get("userId"));
		bean.setShopId(stringToMap.get("shopId"));

		bean.persistOnNotExist();
	}

	private Map<String, String> stringToMap(String value) {
		String[] split = StringUtils.split(value, ";");
		Map<String, String> maps = new HashMap<String, String>(split.length);
		String[] tmp;
		for (String str : split) {
			tmp = StringUtils.split(str, "=");
			maps.put(tmp[0], tmp[1]);
		}
		return maps;
	}

	// 淘宝
	private TaobaoShopBean parseTaobaoShop(Elements tmp) {
		TaobaoShopBean bean = new TaobaoShopBean();
		Element shop = tmp.first();

		Elements a = shop.select("span.shop-name a");
		if (!a.isEmpty()) {
			bean.setUrl("https:" + a.first().attr("href"));
		}

		if (StringUtils.isBlank(bean.getUrl())) {
			a = shop.select("div.shop-name a");
			if (!a.isEmpty()) {
				bean.setUrl("https:" + a.first().attr("href"));
			}
		}
		// #header-content > div.shop-summary.J_TShopSummary > div.shop-name
		Elements first = shop.select("div.summary-popup.J_TSummaryPopup");

		if (!first.isEmpty()) {

			Elements temp = first.first().getElementsMatchingOwnText("描述相符");
			if (!temp.isEmpty()) {
				bean.setDescScore(temp.first().select("em.count").text());
				bean.setDescLevel(temp.first().select("span").text());
			}
			temp = first.first().getElementsMatchingOwnText("服务态度");
			if (!temp.isEmpty()) {
				bean.setServiceScore(temp.first().select("em.count").text());
				bean.setServiceLevel(temp.first().select("span").text());
			}
			temp = first.first().getElementsMatchingOwnText("物流服务");
			if (!temp.isEmpty()) {
				bean.setDeliverScore(temp.first().select("em.count").text());
				bean.setDeliverLevel(temp.first().select("span").text());
			} else {
				temp = first.first().getElementsMatchingOwnText("发货速度");
				if (!temp.isEmpty()) {
					bean.setDeliverScore(temp.first().select("em.count").text());
					bean.setDeliverLevel(temp.first().select("span").text());
				}
			}
			temp = first.first().getElementsMatchingOwnText("公司名称：");
			if (!temp.isEmpty()) {
				bean.setCompany(temp.parents().select("span.company-name").text());
			}
		}
		return bean;
	}

	// 天猫
	private TaobaoShopBean parseTmallShop(Elements tmp) {
		TaobaoShopBean bean = new TaobaoShopBean();
		;
		Element shop = tmp.first();

		Elements a = shop.select("div.slogo a.slogo-shopname");
		if (!a.isEmpty()) {
			bean.setUrl("https:" + a.first().attr("href"));
		}

		Elements textarea = shop.select("div.extra-info textarea.ks-datalazyload");
		if (!textarea.isEmpty()) {
			Document doc = Jsoup.parseBodyFragment(textarea.text());
			Elements desc = doc.getElementsMatchingOwnText("描述相符：");
			if (!desc.isEmpty()) {
				bean.setDescScore(desc.first().select("a em.count").text());
				bean.setDescLevel(desc.first().select("a span.rateinfo").text());
			}

			Elements service = doc.getElementsMatchingOwnText("服务态度：");
			if (!service.isEmpty()) {
				bean.setServiceScore(service.first().select("a em.count").text());
				bean.setServiceLevel(service.first().select("a span.rateinfo").text());
			}

			Elements deliver = doc.getElementsMatchingOwnText("发货速度：");
			if (!deliver.isEmpty()) {
				bean.setDeliverScore(deliver.first().select("a em.count").text());
				bean.setDeliverLevel(deliver.first().select("a span.rateinfo").text());

			}
			// company
			Elements company = doc.getElementsMatchingOwnText("公 司 名：");
			if (!company.isEmpty()) {
				bean.setCompany(company.first().nextElementSibling().text());
			}

			Elements area = doc.getElementsMatchingOwnText("所 在 地：");
			if (!area.isEmpty()) {
				bean.setArea(area.first().nextElementSibling().text());
			}
		}
		return bean;
	}

}
