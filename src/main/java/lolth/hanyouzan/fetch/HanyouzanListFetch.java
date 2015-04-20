package lolth.hanyouzan.fetch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.fetch.handlers.PageFetchHandler;
import lakenono.log.BaseLog;
import lolth.hanyouzan.bean.HanyouzanGoods;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HanyouzanListFetch extends BaseLog implements PageFetchHandler
{
	private String categoryCode;

	public static void main(String[] args) throws UnsupportedEncodingException, IOException, InterruptedException, Exception
	{
		new HanyouzanListFetch("10000006").run();
		new HanyouzanListFetch("10000007").run();
		new HanyouzanListFetch("10000009").run();
		new HanyouzanListFetch("10000119").run();
		new HanyouzanListFetch("10000112").run();
	}

	@Override
	public void run() throws IOException, InterruptedException, SQLException, Exception
	{
		int maxPage = this.getMaxPage();

		for (int i = 0; i < maxPage; i++)
		{
			this.process(i);
		}
	}

	@Override
	public void process(int i) throws Exception
	{
		Document document = this.fetch(i);

		Elements elements = document.select("ul.productList li");

		for (Element element : elements)
		{
			HanyouzanGoods bean = new HanyouzanGoods();

			String url = element.select("a").first().attr("href");
			bean.setUrl(url);

			String picUrl = element.select("a dl dt span.thumbImg img").first().attr("src");
			bean.setPicUrl(picUrl);

			String name = element.select("a dl dt span.thumbTit").first().text();
			bean.setName(name);

			String price = element.select("a dl dd span.salePrice").first().text();
			bean.setPrice(price);

			bean.persistOnNotExist();
			this.log.info(bean.toString());
		}
	}

	@Override
	public int getMaxPage() throws UnsupportedEncodingException, IOException, InterruptedException, Exception
	{
		Document document = this.fetch(0);

		Element element = document.select("ul.ul_list li.li_page").last();

		String pagejs = element.select("a").first().attr("onclick");

		String page = StringUtils.substringBetween(pagejs, "'");

		this.log.info("{} 频道 最大页数:{}", this.categoryCode, page);

		return Integer.parseInt(page);
	}

	public Document fetch(int page) throws InterruptedException, IOException
	{
		Connection connect = Jsoup.connect("http://www.hanyouzan.com/category/category.do");

		// post 
		connect.data("categoryCode", this.categoryCode);
		connect.data("currentPage", page + 1 + "");
		connect.data("orderType", "3");
		connect.data("rowsPerPage", "100");
		connect.data("view_type", "");

		// cookie
		connect.cookie("JSESSIONID", "56196C41A82A1E4BEEC1881FC736B0F9.server1");
		connect.cookie("saveId", "\"lakenono@126.com\"");
		connect.cookie("_ga", "GA1.2.475344705.1429170126");
		connect.cookie("_gat", "1");
		connect.cookie("__xsptplus225", "225.1.1429170131.1429171120.6%234%7C%7C%7C%7C%7C%23%23T_Iwr9CfP2m0fD_mAl5-lSim3f3Y8N0_%23");

		// ua
		connect.userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.timeout(1000 * 5);

		int retry = 5;

		for (int i = 1; i <= retry; i++)
		{
			try
			{
				// 休眠时间
				Thread.sleep(15 * 1000);
				return connect.post();
			}
			catch (java.net.SocketTimeoutException e)
			{
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			}
			catch (java.net.ConnectException e)
			{
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			}

		}
		throw new RuntimeException("fetcher重试[" + retry + "]次后无法成功.");
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
