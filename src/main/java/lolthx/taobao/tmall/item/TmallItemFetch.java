package lolthx.taobao.tmall.item;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolthx.taobao.tmall.item.bean.TmallItemBean;

public class TmallItemFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "tmall_item_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		TmallItemBean itemBean = new TmallItemBean();
		itemBean.setUrl(task.getUrl());
		// #J_DetailMeta div.tb-detail-hd > h1
		itemBean.setTitle(doc.select("#J_DetailMeta div.tb-detail-hd > h1").text());
		// #J_PromoPrice > dd span.tm-price
		itemBean.setPrice(doc.select("#J_PromoPrice > dd span.tm-price").text());
		// #J_DetailMeta li.tm-ind-sellCount span.tm-count
		itemBean.setMonthSales(doc.select("#J_DetailMeta li.tm-ind-sellCount span.tm-count").text());
		// #J_DetailMeta li.tm-ind-reviewCount span.tm-count
		itemBean.setComments(doc.select("#J_DetailMeta li.tm-ind-reviewCount span.tm-count").text());
		// #dsr-userid
		itemBean.setUserId(doc.select("#dsr-userid").attr("value"));
		// #LineZing
		String itemId = doc.select("#LineZing").attr("itemid");
		if (StringUtils.isBlank(itemId)) {
			itemId = HttpURLUtils.getUrlParams(itemBean.getUrl(), "GBK").get("id");
		}
		itemBean.setItemId(itemId);
		String shopUrl = doc.select("#shopExtra > div.slogo > a").attr("href");
		
		task.setExtra("1");
		if (itemBean.persistOnNotExist()) {
			Task newTask = buildTask("https:" + shopUrl, "taobao_item_shop", task);
			Queue.push(newTask);
			
			if (StringUtils.isNotBlank(itemBean.getComments()) && !"0".equals(itemBean.getComments())) {
				parseComment(itemBean.getItemId(), itemBean.getUserId(), itemBean.getComments(), task);
			}
		}
	}

	
	private static final String TAMLL_COMMENT_URL = "http://rate.tmall.com/list_detail_rate.htm?itemId={0}&sellerId={1}&currentPage={2}";
	private static final int COMMENT_PAGE_SIZE = 20;
	
	private void parseComment(String itemId, String userId, String comments, Task task) {
		int count = Integer.parseInt(comments);
		if (count == 0) {
			return;
		}
		// 计算总页数
		int pages = count / COMMENT_PAGE_SIZE + 1;
		// 天猫最多提供99页
		if (pages > 99) {
			pages = 99;
		}

		for (int i = 1; i <= pages; i++) {
			Task buildTask = buildTask(MessageFormat.format(TAMLL_COMMENT_URL, itemId, userId, i), "taobao_item_commons", task);
			Queue.push(buildTask);
		}
	}

}
