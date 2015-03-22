package lolth.dianping.fetch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.fetch.handlers.PageFetchHandler;
import lakenono.log.BaseLog;
import lolth.dianping.bean.DianPingCommentBean;
import lolth.dianping.bean.DianPingShopBean;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DianPingCommentFetch extends BaseLog implements PageFetchHandler
{
	public static void main(String[] args) throws IOException, InterruptedException, SQLException, Exception
	{
		List<Object[]> query = GlobalComponents.db.getRunner().query("select shopid,keyword from " + BaseBean.getTableName(DianPingShopBean.class), new ArrayListHandler());

		for (Object[] objects : query)
		{
			new DianPingCommentFetch(objects[0].toString(), objects[1].toString()).run();
		}
	}

	private String shopid;
	private String keyword;

	public DianPingCommentFetch(String shopid, String keyword)
	{
		super();
		this.shopid = shopid;
		this.keyword = keyword;
	}

	@Override
	public void run() throws IOException, InterruptedException, SQLException, Exception
	{
		int maxPage = this.getMaxPage();
		this.log.info("begin comment.. :[{}][{}] 0/{}", keyword, this.shopid, maxPage);

		for (int i = 0; i < maxPage; i++)
		{
			String taskname = MessageFormat.format("dianping-comment-{0}-{1}-[{2}/{3}]", keyword, this.shopid, i + 1, maxPage);

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

		Elements elements = document.select("div.comment-list ul li[id]");

		for (Element element : elements)
		{
			DianPingCommentBean bean = new DianPingCommentBean();

			String commentid = element.attr("data-id");
			bean.setCommentid(commentid);

			String username = element.select("p.name a").first().text();
			bean.setUsername(username);

			String userurl = element.select("p.name a").first().attr("href");
			bean.setUserurl("http://www.dianping.com" + userurl);

			String userlevel = element.select("p.contribution span.user-rank-rst").first().attr("title");
			bean.setUserlevel(userlevel);

			if (element.select("span.item-rank-rst").size() > 0)
			{
				String rank = element.select("span.item-rank-rst").first().attr("class");
				bean.setRank(StringUtils.substringAfterLast(rank, "irr-star"));
			}

			if (element.select("span.comm-per").size() > 0)
			{
				String per = element.select("span.comm-per").first().text();
				bean.setPer(StringUtils.substringAfterLast(per, "￥"));
			}

			if (element.select("div.comment-rst").size() > 0) //有评分区域
			{
				if (element.select("div.comment-rst").first().getElementsMatchingOwnText("口味").size() > 0)
				{
					String tasteRank = element.select("div.comment-rst").first().getElementsMatchingOwnText("口味").first().text();
					bean.setTasteRank(tasteRank);
				}

				if (element.select("div.comment-rst").first().getElementsMatchingOwnText("环境").size() > 0)
				{
					String environmentRank = element.select("div.comment-rst").first().getElementsMatchingOwnText("环境").first().text();
					bean.setEnvironmentRank(environmentRank);
				}

				if (element.select("div.comment-rst").first().getElementsMatchingOwnText("服务").size() > 0)
				{
					String serviceRank = element.select("div.comment-rst").first().getElementsMatchingOwnText("服务").first().text();
					bean.setServiceRank(serviceRank);
				}
			}

			String text = element.select("div.comment-txt").first().text();
			bean.setText(text);

			String postTime = element.select("span.time").first().text();
			bean.setPostTime(postTime);

			bean.setShopid(this.shopid);
			bean.setKeyword(this.keyword);

			this.log.info(bean.toString());

			bean.persist();
		}
	}

	@Override
	public int getMaxPage() throws UnsupportedEncodingException, IOException, InterruptedException
	{
		String url = this.buildUrl(0);
		Document document = GlobalComponents.fetcher.document(url);

		if (document.select("div.Pages a.PageLink").size() > 0)
		{
			String page = document.select("div.Pages a.PageLink").last().text();

			return Integer.parseInt(page);
		}
		else
		{
			return 0;
		}
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		return MessageFormat.format("http://www.dianping.com/shop/{0}/review_search_{1}?pageno={2}", this.shopid, URLEncoder.encode(this.keyword, "UTF-8"), pageNum + 1);
	}

}
