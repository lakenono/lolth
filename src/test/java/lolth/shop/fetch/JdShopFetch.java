package lolth.shop.fetch;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.shop.bean.Item;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JdShopFetch
{
	public static void main(String[] args) throws Exception
	{
		String shopName = "柏品JD旗舰店";
		String shopUrl = "http://mall.jd.com/advance_search-181854-56658-52578-0-0-0-1-{0}-24.html?other=";
		int shopPageSize = 13;
		
		//String shopUrl = "http://mall.jd.com/advance_search-117267-37586-35527-0-0-0-1-{0}-24.html?other=";
		//String shopUrl = "http://mall.jd.com/advance_search-181854-56658-52578-0-0-0-1-{0}-24.html?other=";

		JdShopFetch shopFetcher = new JdShopFetch();

		shopFetcher.fetchShop(shopName, shopUrl, shopPageSize);
	}

	public void fetchShop(String shopName, String ShopUrl, int shopPageSize) throws Exception
	{
		for (int i = 1; i <= shopPageSize; i++)
		{
			String url = MessageFormat.format(ShopUrl, i);
			List<Item> items = this.fetchPage(shopName, url);

			Thread.sleep(5000);
			System.out.println("fetch page " + i + "...");
			FileUtils.writeLines(new File("data/jd-" + shopName + ".txt"), "UTF-8", items, true);
		}

	}

	private List<Item> fetchPage(String shopName, String url) throws Exception
	{
		List<Item> items = new ArrayList<Item>();

		Document document = GlobalComponents.fetcher.document(url);

		Elements itemDoms = document.select("div.jGoodsInfo");

		for (Element itemDom : itemDoms)
		{
			Element element = itemDom.select("div.jDesc a").get(0);

			Item item = new Item();
			item.setId("jd-" + StringUtils.substringBetween(element.attr("href"), "http://item.jd.com/", ".html"));
			item.setClassification(shopName);
			item.setName(element.html());
			item.setDomain("jd.com");
			item.setPrice("0");

			items.add(item);
		}

		return items;
	}
}
