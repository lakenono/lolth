package lolth.weibo.cn;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import lolth.weibo.bean.WeiboBean;
import lolth.weibo.utils.WeiboIdUtils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Search
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void process(String keyword, String begin, String end) throws IOException, ParseException, InterruptedException, IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		int maxPage = this.getMaxPage(keyword, begin, end);

		this.log.info("begin {} 0/{} ...", keyword, maxPage);

		for (int i = 1; i <= maxPage; i++)
		{
			this.log.info("begin {} {}/{} ...", keyword, i, maxPage);

			Document document = this.fetch(keyword, begin, end, i);
			List<WeiboBean> beans = this.parse(document);

			for (WeiboBean bean : beans)
			{
				bean.setKeyword(keyword);
				try
				{
					bean.persist();
				}
				catch (SQLException e)
				{
					if (StringUtils.contains(e.getMessage(), "for key 'PRIMARY'"))
					{
						this.log.info("重复数据");
					}
					else
					{
						this.log.error("", e);
					}
				}
			}
		}
	}

	private int getMaxPage(String keyword, String begin, String end) throws IOException, InterruptedException
	{
		Document document = this.fetch(keyword, begin, end, 1);

		if (document.select("div#pagelist").size() == 0)
		{
			return 0;
		}
		else
		{
			String html = document.select("div#pagelist").first().text();
			String page = StringUtils.substringBetween(html, "/", "页");
			return Integer.parseInt(page);
		}
	}

	// TODO 修改
	public Document fetch(String keyword, String beginDate, String endDate, int page) throws IOException, InterruptedException
	{
		Connection connect = Jsoup.connect("http://weibo.cn/search/");
		connect.data("vt", "1");
		connect.data("advancedfilter", "1");
		connect.data("starttime", beginDate);
		connect.data("endtime", endDate);
		connect.data("keyword", keyword);
		connect.data("smblog", "搜索");
		connect.data("sort", "time");
		connect.data("page", page + "");
		connect.cookie("_T_WM", "0f0602cfd6ce7a1ae8dd3020d31aafdc");
		connect.cookie("SUB", "_2A2551RSYDeTxGeVO71QT8SbFyTmIHXVbObzQrDV6PUJbrdANLVXhkW0Gk7s86reO8Lb7VV8jQvjftR9KlQ..");
		connect.cookie("gsid_CTandWM", "4uFQ731b1WNb44bDzIUsjcMsyeV");
		connect.cookie("M_WEIBOCN_PARAMS", "rl%3D1");
		connect.userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.timeout(1000 * 5);

		int retry = 5;

		for (int i = 1; i <= retry; i++)
		{
			try
			{
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

	public List<WeiboBean> parse(Document document) throws IOException, ParseException
	{
		Elements elements = document.select("div.c[id]");

		List<WeiboBean> weiboBeans = new LinkedList<WeiboBean>();

		for (Element element : elements)
		{
			WeiboBean bean = new WeiboBean();

			// mid
			String mid = StringUtils.substringAfter(element.attr("id"), "M_");
			bean.setMid(mid);

			// id
			bean.setId(WeiboIdUtils.toId(mid));

			// text
			String text = element.select("span.ctt").text();
			bean.setText(StringUtils.substringAfter(text, ":"));

			// 发布时间
			String postTimeText = element.select("span.ct").text();
			postTimeText = StringUtils.substringBefore(postTimeText, "来自");
			bean.setPostTime(postTimeText);

			// source
			String source = element.select("span.ct").text();
			postTimeText = StringUtils.substringAfter(postTimeText, "来自");
			bean.setSource(source);

			// username
			String username = element.select("a.nk[href]").first().text();
			bean.setUsername(username);

			// userurl
			String userurl = element.select("a.nk[href]").first().attr("href");
			bean.setUserurl(userurl);

			String html = element.html();

			// 赞
			String likes = StringUtils.substringBetween(html, "赞[", "]");
			bean.setLikes(likes);

			// 转发
			String forwards = StringUtils.substringBetween(html, "转发[", "]");
			bean.setReposts(forwards);

			// 评论
			String comments = StringUtils.substringBetween(html, "评论[", "]");
			bean.setComments(comments);

			weiboBeans.add(bean);
			this.log.info(bean.toString());
		}

		return weiboBeans;
	}

	public static void main(String[] args) throws IOException, ParseException, InterruptedException, IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		String[] brands = new String[] { "美素佳儿" };

		for (String brand : brands)
		{
			new Search().process(brand, "20141228", "20141231");
		}
	}
}
