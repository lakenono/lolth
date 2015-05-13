package lolth.muying;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.muying.bean.CommodityBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class MuYingCommodityDetailFetch extends PageParseFetchTaskHandler {

	public static final String MUYING_SHOP_DETAIL_COMMENT = "muying_shop_detail_comment";

	private FetchTaskProducer postDetailProducer = new FetchTaskProducer(MUYING_SHOP_DETAIL_COMMENT);

	public MuYingCommodityDetailFetch(String taskQueueName) {
		super(taskQueueName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		String url = task.getUrl();
		if (StringUtils.isBlank(url)) {
			return;
		}
		CommodityBean bean = new CommodityBean();
		String commodityId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
		bean.setCommodityId(commodityId);
		bean.setCommodityUrl(url);
		bean.setKeyword(task.getName());
		String extra = task.getExtra();
		String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(extra, ":");
		if (tokens.length > 0) {
			bean.setSubjectTask(tokens[0]);
		}
		if (tokens.length > 1) {
			bean.setCommodityStage(parseStage(tokens[1]));
		}
		//
		parseShopOrPrice(doc, bean);
		//
		parseShopDetail(doc, bean);
		// body > div:nth-child(27) > div > div.conTopRightBox > div.logo_div >
		// div > a > img
		// 商品品牌
		Elements elements = doc.select("body  div.conTopRightBox > div.logo_div > div > a > img");
		if (elements.size() > 0) {
			String shopBrand = elements.attr("alt");
			bean.setCommodityBrand(shopBrand);
		}
		log.debug(bean.toString());

		boolean exist = bean.exist();
		if (!exist) {
			bean.persist();
		}
		//
		 parseShopComment(commodityId, task);

	}

	private String parseStage(String cateId) {

		switch (cateId) {
		case "39":
			return "1段";
		case "205":
			return "2段";
		case "206":
			return "3段";
		case "207":
			return "4段";
		default:
			return "";
		}
	}

	/*
	 * 解析评论url 推送评论url
	 */
	private void parseShopComment(String commodityId, FetchTask task) throws Exception {
		Thread.sleep(sleep);
		String url = "http://web.api.muyingzhijia.com/Api/GetUserComment?id=" + commodityId + "&top=10&pageNumber=1&comtype=0";
		String fetch = GlobalComponents.dynamicFetch.fetch(url);
		String between = StringUtils.substringBetween(fetch, "ProductCommentAll\":", ",\"ProductCommentFav");
		if (!StringUtils.isNumeric(between)) {
			return;
		}
		// 评论总数
		int count = Integer.parseInt(between);
		Set<String> detailUrls = new TreeSet<>();
		int start = 0;
		int setp = 10;
		for (int i = 1;; i++) {
			start = i * setp;
			url = "http://web.api.muyingzhijia.com/Api/GetComment?id=" + commodityId + "&top=" + setp + "&pageNumber=" + i + "&comtype=0";
			detailUrls.add(url);
			if (start > count) {
				break;
			}
		}

		// 推送
		for (String purl : detailUrls) {
			if (StringUtils.isBlank(purl)) {
				continue;
			}
			task.setBatchName(MUYING_SHOP_DETAIL_COMMENT);
			task.setUrl(purl);
			task.setExtra(commodityId);
			postDetailProducer.saveAndPushTask(task);
			log.debug(task.toString());
		}

		// /////////////////
		detailUrls.clear();
		detailUrls = null;
		url = null;
		task = null;
	}

	private void parseShopDetail(Document doc, CommodityBean bean) {
		Elements elements = doc.select("#part3_R_3_div");
		if (elements.size() <= 0) {
			return;
		}
		Elements select = elements.select("#intro_box > li > ul > li");
		if (select.size() > 0) {
			// 商品产地
			if (select.size() >= 4) {
				String shopAddr = select.get(3).text();
				String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(shopAddr, "：");
				if (split.length > 1) {
					shopAddr = split[1];
				} else {
					shopAddr = split[0];
				}
				bean.setCommodityPlace(shopAddr.trim());
			}
			// 商品单位
			if (select.size() >= 6) {
				String shopUnit = select.get(5).text();
				String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(shopUnit, "：");
				if (split.length > 1) {
					shopUnit = split[1];
				} else {
					shopUnit = split[0];
				}
				bean.setCommodityUnit(shopUnit);
			}

		}

	}

	private void parseShopOrPrice(Document doc, CommodityBean bean) {
		// #name > h1
		Elements elements = doc.select("body div.conTopConBox");
		if (elements.size() <= 0) {
			return;
		}
		Elements select = elements.select("#name > h1");
		// 商品名称
		if (select.size() > 0) {
			String shopName = select.text();
			bean.setCommodityName(shopName);
		}
		// 商品价格
		select = elements.select("#summary-price > div.sx_dd > strong");
		if (select.size() > 0) {
			String shopPrice = select.text();
			bean.setCommodityPrice(shopPrice);
		}
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.dynamicFetch.document(task.getUrl());
		parsePage(doc, task);
	}

	public static void main(String[] args) {
		String taskQueueName = MuYingCommodityDetailTaskProducer.MUYING_SHOP_LIST_DETAIL;
		MuYingCommodityDetailFetch detailFetch = new MuYingCommodityDetailFetch(taskQueueName);
		detailFetch.setSleep(5000);
		detailFetch.run();
	}

}
