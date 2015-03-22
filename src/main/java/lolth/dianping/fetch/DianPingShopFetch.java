package lolth.dianping.fetch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.fetch.handlers.PageFetchHandler;
import lakenono.log.BaseLog;
import lolth.dianping.bean.DianPingShopBean;
import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AllArgsConstructor
public class DianPingShopFetch extends BaseLog implements PageFetchHandler
{
	public static void main(String[] args) throws Exception
	{
		new DianPingShopFetch("酸菜鱼", 2).run();
		new DianPingShopFetch("火锅", 2).run();
	}

	private String keyword;
	private int cityId;

	@Override
	public void run() throws Exception
	{
		int maxPage = this.getMaxPage();

		this.log.info("begin keyword:[{}][{}] 0/{}", keyword, cityId, maxPage);

		for (int i = 0; i < maxPage; i++)
		{
			String taskname = MessageFormat.format("dianping-shop-{0}-{1}-[{2}/{3}]", keyword, cityId, i + 1, maxPage);

			if (!GlobalComponents.taskService.isCompleted(taskname))
			{
				this.process(i);
				GlobalComponents.taskService.success(taskname);
			}
		}
	}

	@Override
	public void process(int i) throws Exception
	{
		String url = this.buildUrl(i);
		Document document = GlobalComponents.fetcher.document(url);

		Elements elements = document.select("div#shop-all-list.shop-list.J_shop-list.shop-all-list ul li");

		for (Element element : elements)
		{
			DianPingShopBean bean = new DianPingShopBean();

			String shopname = element.select("div.tit a h4").first().text();
			bean.setShopname(shopname);

			String shopurl = element.select("div.tit a").attr("href");
			bean.setShopurl("http://www.dianping.com" + shopurl);

			String shopid = StringUtils.substringAfterLast(shopurl, "/");
			bean.setShopid(shopid);

			if (element.select("div.tit a.shop-branch").size() > 0)
			{
				String branchUrl = element.select("div.tit a.shop-branch").first().attr("href");
				bean.setBranchUrl("http://www.dianping.com" + branchUrl);
			}

			String rank = element.select("span.sml-rank-stars").attr("title");
			bean.setRank(rank);

			if (element.select("div.comment a.review-num b").size() > 0)
			{
				String comments = element.select("div.comment a.review-num b").first().text();
				bean.setComments(Integer.parseInt(comments));
			}
			else
			{
				bean.setComments(0);
			}

			if (element.select("div.comment a.mean-price b").size() > 0)
			{
				String meanPrice = element.select("div.comment a.mean-price b").first().text();
				bean.setMeanPrice(Double.parseDouble(StringUtils.remove(meanPrice, "￥")));
			}
			else
			{
				bean.setMeanPrice(0);
			}

			String cuisines = element.select("div.tag-addr a span.tag").first().text();
			bean.setCuisines(cuisines);

			String addr = element.select("div.tag-addr span.addr").first().text();
			bean.setAddr(addr);

			bean.setCityid(this.cityId);
			bean.setKeyword(this.keyword);

			this.log.info(bean.toString());
			bean.persist();
		}
	}

	@Override
	public int getMaxPage() throws IOException, InterruptedException
	{
		String url = this.buildUrl(0);
		Document document = GlobalComponents.fetcher.document(url);
		String page = document.select("div.page a.PageLink").last().text();
		return Integer.parseInt(page);
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		// o10代表按照评论排序
		return MessageFormat.format("http://www.dianping.com/search/keyword/{0}/0_{1}/o10p{2}", this.cityId, URLEncoder.encode(this.keyword, "UTF-8"), pageNum + 1);
	}
}
