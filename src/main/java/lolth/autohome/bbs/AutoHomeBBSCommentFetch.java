package lolth.autohome.bbs;

import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.autohome.bbs.bean.AutoHomeBBSPostBean;
import lolth.autohome.bbs.bean.AutoHomeBBSCommentBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoHomeBBSCommentFetch
{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws Exception
	{
		while (true)
		{
			String todoSql = "select url from " + BaseBean.getTableName(AutoHomeBBSPostBean.class) + " where postTime >= '2014-12-01' and views is not null and comment_status is null limit 1000";
			this.log.info(todoSql);
			List<String> todo = GlobalComponents.db.getRunner().query(todoSql, new ColumnListHandler<String>());

			this.log.info("待爬取帖子{}...", todo.size());

			for (String url : todo)
			{
				int maxPage = 0;
				try
				{
					maxPage = this.getMaxPage(url);
				}
				catch (Exception e)
				{
					System.out.println(e);
				}

				for (int i = 0; i < maxPage; i++)
				{
					try
					{
						String buildUrl = this.buildUrl(url, i);
						String html = GlobalComponents.fetcher.fetch(buildUrl);
						List<AutoHomeBBSCommentBean> parse = this.parse(html);

						for (AutoHomeBBSCommentBean bean : parse)
						{
							bean.setUrl(buildUrl);
							bean.persist();
						}
					}
					catch (Exception e)
					{
						System.out.println(e);
					}
				}

				GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(AutoHomeBBSPostBean.class) + " set comment_status = 'success' where url=?", url);
			}
		}
	}

	private List<AutoHomeBBSCommentBean> parse(String html)
	{
		List<AutoHomeBBSCommentBean> beans = new LinkedList<AutoHomeBBSCommentBean>();

		Document document = Jsoup.parse(html);
		Elements elements = document.select("div#maxwrap-reply div.clearfix");

		for (Element element : elements)
		{
			AutoHomeBBSCommentBean bean = new AutoHomeBBSCommentBean();

			// 楼层
			String floor = element.attr("id");
			bean.setFloor(floor);

			// text
			String text = element.select("div[xname=content]").first().text();
			{
				Elements imgs = document.select("div[xname=content] img");
				for (Element img : imgs)
				{
					String attr = img.attr("src");
					text = text + " " + attr;
				}
			}
			bean.setText(text);

			// 作者
			String author = element.select("li.txtcenter a").first().text();
			bean.setAuthor(author);

			// 发布时间
			String postTime = element.select("span[xname=date]").first().ownText();
			bean.setPostTime(postTime);

			beans.add(bean);
		}

		return beans;
	}

	private int getMaxPage(String url) throws Exception
	{
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);
		String attr = document.select("span.gopage span.fs").first().attr("title");
		String page = StringUtils.substringBetween(attr, "共", "页");

		return Integer.parseInt(page.trim());
	}

	private String buildUrl(String url, int page)
	{
		return StringUtils.replace(url, "-1.html", "-" + (page + 1) + ".html");
	}

	public static void main(String[] args) throws Exception
	{
		new AutoHomeBBSCommentFetch().run();
	}
}
