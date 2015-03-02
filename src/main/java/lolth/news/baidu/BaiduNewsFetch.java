package lolth.news.baidu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import lakenono.core.GlobalComponents;
import lakenono.db.DB;
import lolth.news.baidu.bean.BaiduNewsBean;
import lolth.news.baidu.bean.BaiduNewsJobStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

public class BaiduNewsFetch
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private ArticleTextExtractor extractor = new ArticleTextExtractor();

	public void process(String keyword, String start, String end) throws Exception
	{
		this.process(keyword, 0, 0, start, end);
	}

	public void process(String keyword, int sort, int type, String start, String end) throws Exception
	{
		// getMaxPage
		int maxPage = this.getMaxPage(keyword, start, end);

		this.log.info("{} {} -- {} begin 0/{}", keyword, start, end, maxPage);

		for (int i = 0; i < maxPage; i++)
		{
			this.log.info("{} {} -- {} begin {}/{}", keyword, start, end, i + 1, maxPage);
			this.run(keyword, sort, type, start, end, i);
		}
	}

	public void run(String keyword, int sort, int type, String start, String end, int page) throws Exception
	{
		String baseUrl = this.buildUrl(keyword, sort, type, start, end, page);
		String html = GlobalComponents.fetcher.fetch(baseUrl);
		Document document = Jsoup.parse(html);

		Elements elements = document.select("li.result[id]");
		for (Element element : elements)
		{
			try
			{
				BaiduNewsBean bean = new BaiduNewsBean();

				// title
				String title = element.select("h3.c-title a").first().text();
				bean.setTitle(title);

				// url
				String url = element.select("h3.c-title a").attr("href");
				bean.setUrl(url);

				// author
				String author = element.select("p.c-author").first().text();
				author = StringUtils.substringBefore(author, "  ");
				bean.setAuthor(author);

				// post time
				String postTime = element.select("p.c-author").first().text();
				postTime = StringUtils.substringAfter(postTime, "  ");
				bean.setPostTime(postTime);

				if (element.select("a.c-more_link").size() != 0)
				{
					// more
					String more = element.select("a.c-more_link").first().text();
					more = StringUtils.substringBefore(more, "条相同新闻");
					bean.setMore(more);

					// more link
					String moreLink = element.select("a.c-more_link").first().attr("href");
					bean.setMoreLink("http://news.baidu.com" + moreLink);
				}

				// baidu cache url
				String baiduCacheUrl = element.select("a.c-cache").first().attr("href");
				bean.setBaiduCacheUrl(baiduCacheUrl);

				String cacheHtml = GlobalComponents.fetcher.fetch(baiduCacheUrl);
				JResult res = this.extractor.extractContent(cacheHtml);
				bean.setText(res.getText());

				// keyword
				bean.setKeyword(keyword);

				// status
				bean.setStatus("success");

				this.log.info(bean.toString());
				bean.persist();
			}
			catch (Exception e)
			{
				this.log.error("", e);
			}
		}
	}

	public int getMaxPage(String keyword, String start, String end) throws Exception
	{
		String url = this.buildUrl(keyword, 0, 0, start, end, 0);
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		Elements pageElements = document.select("html body div#wrapper.wrapper_l p#page a");

		if (pageElements.size() != 0)
		{
			String maxPage = pageElements.get(pageElements.size() - 2).text();
			return Integer.parseInt(maxPage);
		}
		else
		{
			return 1;
		}
	}

	/**
	 * 
	 * @param keyword
	 * @param sort 1表示按焦点排序，0表示按时间排序。
	 * @param type 0全文 1标题
	 * @param startD
	 * @param endD
	 * @return
	 * @throws UnsupportedEncodingException 关键字url编码错误.
	 * @throws ParseException 日期转换报错.
	 */
	public String buildUrl(String keyword, int sort, int type, String start, String end, int page) throws ParseException, UnsupportedEncodingException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("http://news.baidu.com/ns?from=news&cl=2&bt=");

		Date startD = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endD = DateUtils.parseDate(end + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });

		// 开始时间
		sb.append(startD.getTime() / 1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startD);
		sb.append("&y0=").append(cal.get(Calendar.YEAR));
		sb.append("&m0=").append(cal.get(Calendar.MONTH) + 1);
		sb.append("&d0=").append(cal.get(Calendar.DAY_OF_MONTH));
		// 结束时间
		cal.setTime(endD);
		sb.append("&y1=").append(cal.get(Calendar.YEAR));
		sb.append("&m1=").append(cal.get(Calendar.MONTH) + 1);
		sb.append("&d1=").append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append("&et=").append(endD.getTime() / 1000);
		// 全部包含关键词
		sb.append("&q1=").append(URLEncoder.encode(keyword, "GB2312"));
		// &submit=提交
		sb.append("&submit=%B0%D9%B6%C8%D2%BB%CF%C2");
		sb.append("&mt=0&lm=&s=2&begin_date=").append(start);
		sb.append("&end_date=").append(end);

		if (type == 0)
		{
			sb.append("&tn=newsdy");
		}
		else
		{
			sb.append("&tn=newstitledy");
		}

		// 排序方式，1表示按焦点排序，0表示按时间排序。
		if (0 == sort)
		{
			sb.append("&ct1=0");
			sb.append("&ct=0");
		}
		// sb.append("&clk=sortbyrel");
		else if (1 == sort)
		{
			sb.append("&ct1=1");
			sb.append("&ct=1");
		}

		// 每页条数
		sb.append("&rn=100");

		// 页数
		sb.append("&pn=").append(page * 100);

		return sb.toString();
	}

	public static void main(String[] args) throws Exception
	{
		Date date = DateUtils.parseDate("20140101", new String[] { "yyyyMMdd" });

		String[] keywords = new String[] { "中国第一汽车集团公司", "中国一汽", "一汽集团", "东风汽车", "东风公司", "原名二汽", "上汽", "上海汽车", "上海汽车集团", "长安汽车", "长安集团" };

		while (true)
		{
			for (String keyword : keywords)
			{
				for (int i = 0; i < 365; i++)
				{
					Date targetDate = DateUtils.addDays(date, i);
					String format = DateFormatUtils.format(targetDate, "yyyyMMdd");

					@SuppressWarnings("unchecked")
					long count = (long) GlobalComponents.db.getRunner().query("select count(*) from meta_search_news_baidu_status where date=? and keyword=?", DB.scaleHandler, format, keyword);
					if (count > 0)
					{
						System.out.println(format + " is success.. so continue..");
						continue;
					}

					new BaiduNewsFetch().process(keyword, format, format);

					BaiduNewsJobStatus jobStatus = new BaiduNewsJobStatus();
					jobStatus.setDate(format);
					jobStatus.setStatus("success");
					jobStatus.setKeyword(keyword);
					jobStatus.persist();
				}
			}
		}
	}
}
