package lolthx.taobao.item;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.taobao.item.bean.TaobaoItemBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TaobaoItemFetch extends DistributedParser {
	
	private boolean isMQ = true;
	public TaobaoItemFetch(){
		
	}
	public TaobaoItemFetch(boolean isMQ){
		this.isMQ = isMQ;
	}

	@Override
	public String getQueueName() {
		return "taobao_item_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		TaobaoItemBean bean = new TaobaoItemBean();
		// 名称
		String title = doc.select("#J_Title > h3").text();
		bean.setTitle(title);
		bean.setUrl(task.getUrl());
		bean.setKeyword(task.getExtra());
		bean.setProjectName(task.getProjectName());
		// #J_PromoPriceNum 价格
		String price = doc.select("#J_PromoPriceNum").text();
		if (StringUtils.isBlank(price)) {
			price = doc.select("#J_StrPrice > em.tb-rmb-num").text();
		}
		bean.setPrice(price);
		// #J_RateCounter 评价量
		String comments = doc.select("#J_RateCounter").text();
		bean.setComments(comments);
		// #J_SellCounter 成交量
		String monthSales = doc.select("#J_SellCounter").text();
		bean.setMonthSales(monthSales);
		// #J_Pine 旺旺id
		String userId = doc.select("#J_Pine").attr("data-sellerid");
		bean.setUserId(userId);
		String itemId = doc.select("#J_Pine").attr("data-itemid");
		bean.setItemId(itemId);
		// #J_ShopInfo strong > a 商铺url
		String shopUrl = doc.select("#J_ShopInfo strong > a").attr("href");
		//抓取商品和评论
		if (bean.persistOnNotExist() && isMQ) {
			task.setExtra("0");
			Task newTask = buildTask("https:" + shopUrl, "taobao_item_shop", task);
			Queue.push(newTask);
			
			if (StringUtils.isNotBlank(comments) && !"0".equals(comments)) {
				parseComment(bean.getItemId(), bean.getUserId(), bean.getComments(), task);
			}
		}

		// if (StringUtils.isNotBlank(comments) && !"0".equals(comments)) {
		// parseComment(bean.getItemId(), bean.getUserId(), bean.getComments(),
		// task);
		// }
	}

	private static final String TAOBAO_COMMENT_URL = "https://rate.taobao.com/feedRateList.htm?userNumId={0}&auctionNumId={1}&currentPageNum={2}";
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
			Task newTask = buildTask(MessageFormat.format(TAOBAO_COMMENT_URL, userId, itemId, i), "taobao_item_commons", task);
			Queue.push(newTask);
		}

	}

}
