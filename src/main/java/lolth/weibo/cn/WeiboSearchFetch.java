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

public class WeiboSearchFetch
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

	// TODO 需要COOKIE池
	public Document fetch(String keyword, String beginDate, String endDate, int page) throws IOException, InterruptedException
	{
		Connection connect = Jsoup.connect("http://weibo.cn/search/");

		// post 
		connect.data("vt", "1");
		connect.data("advancedfilter", "1");
		connect.data("starttime", beginDate);
		connect.data("endtime", endDate);
		connect.data("keyword", keyword);
		connect.data("smblog", "搜索");
		connect.data("sort", "time");
		connect.data("page", page + "");

		// cookie
		connect.cookie("_T_WM", "0f0602cfd6ce7a1ae8dd3020d31aafdc");
		connect.cookie("SUB", "_2A254D5dsDeThGeVN7lIV9CnEzziIHXVYnK2krDV6PUJbrdAKLWHSkWpuSrt8a-1-y2RIOi8PCrqHJ_fNKw..");
		connect.cookie("gsid_CTandWM", "4un3ca761seUts0bLF56je3BtdC");
		connect.cookie("M_WEIBOCN_PARAMS", "rl%3D1");

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

	public List<WeiboBean> parse(Document document) throws IOException, ParseException
	{
		Elements elements = document.select("div.c[id]");

		List<WeiboBean> weiboBeans = new LinkedList<WeiboBean>();

		for (Element element : elements)
		{
			String html = element.html();

			WeiboBean bean = new WeiboBean();

			// mid
			String mid = StringUtils.substringAfter(element.attr("id"), "M_");
			bean.setMid(mid);

			// id
			bean.setId(WeiboIdUtils.toId(mid));

			// 发布时间
			String postTimeText = element.select("span.ct").text();
			postTimeText = StringUtils.substringBefore(postTimeText, "来自");
			bean.setPostTime(postTimeText);

			// username
			String username = element.select("a.nk[href]").first().text();
			bean.setUsername(username);

			// userurl
			String userurl = element.select("a.nk[href]").first().attr("href");
			bean.setUserurl(userurl);

			// userid
			String userid = StringUtils.substringAfterLast(userurl, "/");
			bean.setUserid(userid);

			// weibourl
			bean.setWeibourl("http://weibo.cn/comment/" + mid);

			// source
			String source = element.select("span.ct").text();
			source = StringUtils.substringAfter(source, "来自");
			bean.setSource(source);

			// 赞
			Element likesElement = element.getElementsMatchingOwnText("赞\\[").last();
			String likes = StringUtils.substringBetween(likesElement.text(), "赞[", "]");
			bean.setLikes(likes);

			// 转发
			String forwards = StringUtils.substringBetween(html, ">转发[", "]");
			bean.setReposts(forwards);

			// 评论
			String comments = StringUtils.substringBetween(html, ">评论[", "]");
			bean.setComments(comments);

			// 原创
			if (!StringUtils.contains(html, "原文转发"))
			{
				// text
				String text = element.select("span.ctt").text();
				bean.setText(StringUtils.substringAfter(text, ":"));
			}
			else
			{
				String pweibourl = element.select("a.cc").first().attr("href");
				bean.setPweibourl(pweibourl);

				String pmid = StringUtils.substringBetween(pweibourl, "comment/", "?");
				bean.setPmid(pmid);

				String pid = WeiboIdUtils.toId(pmid);
				bean.setPid(pid);

				String text = StringUtils.substringBetween(element.select("div").last().text(), "转发理由:", "赞[");
				bean.setText(text);
			}

			weiboBeans.add(bean);
			this.log.info(bean.toString());
		}
		return weiboBeans;
	}

	public static void main(String[] args) throws IOException, ParseException, InterruptedException, IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		String[] brands = new String[] { "美素佳儿" };

		for (String keyword : brands)
		{
			new WeiboSearchFetch().process(keyword, "20141228", "20141228");
		}
	}
}