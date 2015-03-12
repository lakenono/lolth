package lolth.autohome.bbs;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.autohome.bbs.bean.AutoHomeBBSPostBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoHomeBBSPostListFetch
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private String id;
	private String jobId;

	public AutoHomeBBSPostListFetch(String id, String jobId)
	{
		this.id = id;
		this.jobId = jobId;
	}

	public void run() throws Exception
	{
		int maxPage = this.getMaxPage();

		for (int i = 0; i < maxPage; i++)
		{
			String url = this.buildUrl(id, i);
			String html = GlobalComponents.fetcher.fetch(url);
			List<AutoHomeBBSPostBean> beans = this.parse(html);

			for (AutoHomeBBSPostBean bean : beans)
			{
				try
				{
					bean.setJobId(this.jobId);
					bean.persist();
				}
				catch (Exception e)
				{
					this.log.error("存储失败", e);
				}
			}
		}
	}

	private List<AutoHomeBBSPostBean> parse(String html)
	{
		List<AutoHomeBBSPostBean> autoHomeBBSBeans = new LinkedList<AutoHomeBBSPostBean>();

		Document document = Jsoup.parse(html);
		Elements elements = document.select("dl.list_dl[lang]");

		for (Element element : elements)
		{
			AutoHomeBBSPostBean bean = new AutoHomeBBSPostBean();

			// title
			String title = element.select("dt a").first().text();
			bean.setTitle(title);

			// url
			String url = element.select("dt a").first().attr("href");
			bean.setUrl("http://club.autohome.com.cn" + url);

			// 作者
			String author = element.select("dd").first().select("a").first().text();
			bean.setAuthor(author);

			// 作者url
			String authorUrl = element.select("dd").first().select("a").first().attr("href");
			bean.setAuthorUrl(authorUrl);

			// 发帖时间
			String postTime = element.select("dd").first().select("span").text();
			bean.setPostTime(postTime);

			autoHomeBBSBeans.add(bean);
		}

		return autoHomeBBSBeans;
	}

	public int getMaxPage() throws Exception
	{
		String url = this.buildUrl(id, 1);
		Document document = GlobalComponents.fetcher.document(url);
		String text = document.select("div.pagearea span.fr").text();
		String page = StringUtils.substringBetween(text, "共", "页");
		return Integer.parseInt(page);
	}

	public String buildUrl(String id, int page)
	{
		String baseUrl = "http://club.autohome.com.cn/bbs/forum-c-{0}-{1}.html";
		return MessageFormat.format(baseUrl, id, page + 1 + "");
	}

	public static void main(String[] args) throws Exception
	{
		// 东风风度MX6 3637 http://club.autohome.com.cn/bbs/forum-c-3637-1.html
		new AutoHomeBBSPostListFetch("3637", "东风风度MX6").run();

		// 哈弗H6 2123 http://club.autohome.com.cn/bbs/forum-c-2123-1.html
		new AutoHomeBBSPostListFetch("2123", "哈弗H6").run();

		// 奔腾X80 3000 http://club.autohome.com.cn/bbs/forum-c-3000-1.html
		new AutoHomeBBSPostListFetch("3000", "奔驰C级").run();

		// 长安CS75 3204 http://club.autohome.com.cn/bbs/forum-c-3204-1.html
		new AutoHomeBBSPostListFetch("3204", "长安CS75").run();

		// 传祺GS5 2560 http://club.autohome.com.cn/bbs/forum-c-2560-1.html
		new AutoHomeBBSPostListFetch("2560", "传祺GS5").run();
	}
}
