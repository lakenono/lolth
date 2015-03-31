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

public class YhdShopFetch
{
	public static void main(String[] args) throws Exception
	{
		String shopName = "静佳品牌官方旗舰店";
		String shopUrl = "http://shop.yhd.com/search-0-129480-1--.html?productMerchantDto.start={0}&productMerchantDto.limit=48";
		int shopPageSize = 6;

		YhdShopFetch shopFetcher = new YhdShopFetch();

		shopFetcher.fetchShop(shopName, shopUrl, shopPageSize);
	}

	public void fetchShop(String shopName, String ShopUrl, int shopPageSize) throws Exception
	{
		for (int i = 0; i < shopPageSize; i++)
		{
			String url = MessageFormat.format(ShopUrl, i * 48);
			List<Item> items = this.fetchPage(shopName, url);

			Thread.sleep(5000);
			System.out.println("fetch page " + i + "...");
			FileUtils.writeLines(new File("data/yhd-" + shopName + ".txt"), "UTF-8", items, true);
		}

	}

	private List<Item> fetchPage(String shopName, String url) throws Exception
	{
		System.out.println(url);

		List<Item> items = new ArrayList<Item>();

		Document document = GlobalComponents.fetcher.document(url);

		Elements itemDoms = document.select("html body#comParamId div.inshop_wrap div.inshop_content div.inshop_layout.clearfix div#C31.inshop_layout_colmain div.inshop_searchCat.inshop_main.mt10 div.inshop_moudle.inshop_main div#fix_inshop_product_list.inshop_tags.inshop_prolists.inshop_box.inshop_con ul.clearfix li");

		for (Element itemDom : itemDoms)
		{
			Item item = new Item();

			item.setId("yhd-" + StringUtils.substringAfter(itemDom.select("h3.pro_name a").get(0).attr("href"), "http://item.yhd.com/item/"));
			item.setClassification(shopName);
			item.setName(itemDom.select("h3.pro_name a").get(0).html());
			item.setDomain("yhd.com");
			item.setPrice("0");

			items.add(item);
		}

		return items;
	}

}
