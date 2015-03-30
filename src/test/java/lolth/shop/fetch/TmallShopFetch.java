package lolth.shop.fetch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.shop.bean.Item;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TmallShopFetch
{
	public static void main(String[] args) throws Exception
	{
		String shopName = "柏品tmall旗舰店";
		String shopUrl = "bycreations.tmall.com";
		int shopPageSize = 6;

		TmallShopFetch shopFetcher = new TmallShopFetch();

		shopFetcher.fetchShop(shopName, shopUrl, shopPageSize);
	}

	public List<Item> fetchPage(String shopName, String shopUrl, int pageNum) throws Exception
	{
		List<Item> items = new ArrayList<Item>();

		Document document = GlobalComponents.fetcher.document("http://" + shopUrl + "/search.htm?pageNo=" + pageNum);

		// 所有商品dom
		Elements elements = document.select("dl.item");

		for (Element element : elements)
		{
			Item item = new Item();

			item.setId("tmall-" + element.attr("data-id"));
			item.setClassification(shopName);
			item.setName(element.select("dd.detail a.item-name").get(0).html());
			item.setPrice(element.select("dd.detail div.attribute div.cprice-area span.c-price").get(0).html());
			item.setDomain("tmall.com");

			items.add(item);
		}

		return items;
	}

	public void fetchShop(String shopName, String shopUrl, int shopPageSize) throws Exception, IOException
	{
		for (int i = 1; i <= shopPageSize; i++)
		{
			List<Item> items = this.fetchPage(shopName, shopUrl, i);
			Thread.sleep(5000);
			System.out.println("fetch page " + i + "...");
			FileUtils.writeLines(new File("data/tmall-" + shopName + ".txt"), "UTF-8", items, true);
		}
	}
}
