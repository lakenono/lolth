package lolth.muying;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class MuYingTest {

	@Test
	public void testShoppingListDetail() throws Exception{
		String url = "http://www.muyingzhijia.com/Shopping/SearchResult.aspx?condition=%E6%83%A0%E6%B0%8F%E5%90%AF%E8%B5%8B&page=1";
		FetchTask task = new FetchTask();
		task.setUrl(url);
		String taskQueueName = MuYingCommoditySearchList.MUYING_SHOP_LIST;
		MuYingCommodityDetailTaskProducer shoppingDetailTaskProducer = new MuYingCommodityDetailTaskProducer(taskQueueName);
		shoppingDetailTaskProducer.handleTask(task);
	}
	
	@Test
	public void testShoppingDetail() throws Exception{
		String url = "http://item.muyingzhijia.com/151687.html";
		FetchTask task = new FetchTask();
		task.setUrl(url);
		String taskQueueName = MuYingCommodityDetailTaskProducer.MUYING_SHOP_LIST_DETAIL;
		MuYingCommodityDetailFetch shoppingDetailFetch = new MuYingCommodityDetailFetch(taskQueueName);
		shoppingDetailFetch.handleTask(task);
	}
	
	@Test
	public void testShooingCommentNum() throws Exception{
		String url = "http://web.api.muyingzhijia.com/Api/GetUserComment?id=61074&top=10&pageNumber=1&comtype=0";
		String fetch = GlobalComponents.dynamicFetch.fetch(url);
		System.out.println(fetch);
		String between = StringUtils.substringBetween(fetch,"ProductCommentAll\":", ",\"ProductCommentFav");
		System.out.println(between);
	}
	
	@Test
	public void testShoppingComment() throws Exception{
		String url = "http://web.api.muyingzhijia.com/Api/GetComment?id=61069&top=50&pageNumber=1&comtype=0";
		FetchTask task = new FetchTask();
		task.setUrl(url);
		String taskQueueName = MuYingCommodityDetailFetch.MUYING_SHOP_DETAIL_COMMENT;
		MuYingCommodityCommentDetail detail = new MuYingCommodityCommentDetail(taskQueueName);
		detail.handleTask(task);
	}
	
}
