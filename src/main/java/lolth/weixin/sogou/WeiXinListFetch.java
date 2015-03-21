package lolth.weixin.sogou;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

import lakenono.core.GlobalComponents;
import lakenono.log.BaseLog;
import lolth.weixin.sogou.bean.WeiXinBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiXinListFetch extends BaseLog
{
	public String keyword;

	public WeiXinListFetch(String keyword)
	{
		super();
		this.keyword = keyword;
	}

	private void run() throws Exception
	{
		int maxPage = this.getMaxPage();

		for (int i = 0; i < maxPage; i++)
		{
			String taskname = MessageFormat.format("weixin-{0}-{1}", this.keyword, i);

			if (GlobalComponents.taskService.isCompleted(taskname))
			{
				i++;
				this.log.info("task {} is completed", taskname);
				continue;
			}

			this.log.info("keyword[{}] {}/{}...", this.keyword, i, maxPage);

			String url = this.buildUrl(keyword, i);
			Document document = GlobalComponents.dynamicFetch.document(url);
			this.process(document);

			GlobalComponents.taskService.success(taskname);
		}
	}

	private void process(Document document) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException
	{
		Elements elements = document.select("div.results div.wx-rb");

		for (Element element : elements)
		{
			WeiXinBean bean = new WeiXinBean();

			// title
			String title = element.select("div.txt-box h4 a").first().text();
			bean.setTitle(title);

			// url
			String url = element.select("div.txt-box h4 a").first().attr("href");
			bean.setUrl(url);

			// postTime
			String postTimeNum = element.select("div.s-p").first().attr("t");
			Date date = new Date(Long.parseLong(postTimeNum) * 1000);
			String postTime = DateFormatUtils.format(date, "yyyyMMdd HHmmss");
			bean.setPostTime(postTime);

			// author
			String authorname = element.select("a#weixin_account").first().attr("title");
			bean.setAuthorname(authorname);

			// author url
			String authorurl = element.select("a#weixin_account").first().attr("href");
			bean.setAuthorurl("http://weixin.sogou.com" + authorurl);

			// author id
			String authorid = element.select("a#weixin_account").first().attr("i");
			bean.setAuthorid(authorid);

			bean.setKeyword(this.keyword);

			bean.persist();

			this.log.info(bean.toString());
		}
	}

	public String buildUrl(String keyword, int page) throws UnsupportedEncodingException
	{
		String baseUrl = "http://weixin.sogou.com/weixin?query={0}&fr=sgsearch&type=2&page={1}&ie=utf8";
		return MessageFormat.format(baseUrl, URLEncoder.encode(keyword, "UTF-8"), page + 1);
	}

	public int getMaxPage() throws Exception
	{
		String url = this.buildUrl(this.keyword, 0);
		Document document = GlobalComponents.dynamicFetch.document(url);

		// 如果没有搜索结果
		if (document.select("div#noresult_part1_container").size() > 0)
		{
			this.log.info("keyword [{}] count[{}] page[{}]", this.keyword, 0, 0);
			return 0;
		}

		String countStr = document.select("resnum#scd_num").text();
		countStr = StringUtils.remove(countStr, ",");

		int count = Integer.parseInt(countStr);
		int page = count / 10 + 1;
		this.log.info("keyword [{}] count[{}] page[{}]", this.keyword, count, page);
		return page;
	}

	public static void main(String[] args) throws Exception
	{
		while (true)
		{
			try
			{
				new WeiXinListFetch("东风风度MX6").run();
				new WeiXinListFetch("哈弗H6").run();
				new WeiXinListFetch("奔腾X80").run();
				new WeiXinListFetch("长安CS75").run();
				new WeiXinListFetch("传祺GS5").run();

				new WeiXinListFetch("穹顶之下").run();
				new WeiXinListFetch("柴静").run();
				new WeiXinListFetch("12369").run();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}
}
