package lolth.xcar.bbs.old;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lakenono.core.GlobalComponents;
import lolth.xcar.bbs.bean.XCarBBSPostBean;

public class XCarBBSPostListFetch
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private String id;
	private String jobname;

	public XCarBBSPostListFetch(String id, String jobname)
	{
		this.id = id;
		this.jobname = jobname;
	}

	public void run() throws IOException, InterruptedException
	{
		int maxPage = this.getMaxPage();

		this.log.info("start job [{}] maxPage [{}]", this.jobname, maxPage);

		for (int i = 0; i < maxPage; i++)
		{
			String url = this.buildUrl(id, i);
			String html = GlobalComponents.fetcher.fetch(url);
			List<XCarBBSPostBean> beans = this.parse(html);

			for (XCarBBSPostBean bean : beans)
			{
				try
				{
					bean.setBrandId(this.jobname);
					this.log.info(bean.toString());
					bean.persist();
				}
				catch (Exception e)
				{
					this.log.error("存储失败", e);
				}
			}
		}
	}

	private List<XCarBBSPostBean> parse(String html)
	{
		List<XCarBBSPostBean> beans = new ArrayList<XCarBBSPostBean>();

		Document document = Jsoup.parse(html);
		Elements elements = document.select("#F_box_1 table.row");

		for (Element element : elements)
		{
			XCarBBSPostBean bean = new XCarBBSPostBean();

			// title
			String title = element.select("a.open_view").first().text();
			bean.setTitle(title);

			// url
			String url = element.select("a.open_view").first().attr("href");
			bean.setUrl("http://www.xcar.com.cn" + url);

			// 作者
			String author = element.select("td[width=14%] a").first().text();
			bean.setAuthorId(author);

			// 作者url
			// String authorUrl =
			// element.select("td[width=14%] a").first().attr("href");
			// bean.setAuthorUrl(authorUrl);

			// 发帖时间
			String postTime = element.select("span.smalltxt.lighttxt").first().text();
			bean.setPostTime(postTime);

			String vrhtml = element.select("td[width=9%]").first().html();

			// 回复数
			String replys = StringUtils.substringBefore(vrhtml, "<br>");
			bean.setReplys(StringUtils.remove(replys, "\""));

			// 查看数
			String views = StringUtils.substringAfter(vrhtml, "<br>");
			bean.setViews(StringUtils.remove(views, "\""));

			beans.add(bean);
		}

		return beans;
	}

	public String buildUrl(String id, int page)
	{
		String baseUrl = "http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&page={1}";
		return MessageFormat.format(baseUrl, id, page + 1);
	}

	public int getMaxPage() throws IOException, InterruptedException
	{
		String url = this.buildUrl(this.id, 0);
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		Elements elements = document.select("div.fn_0209 a");
		String maxPage = elements.get(elements.size() - 2).html();

		return Integer.parseInt(maxPage);
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{
		new XCarBBSPostListFetch("1543", "东风风度MX6").run();
		new XCarBBSPostListFetch("963", "哈弗H6").run();
		new XCarBBSPostListFetch("1042", "奔腾X80").run();
		new XCarBBSPostListFetch("1218", "长安CS75").run();
		new XCarBBSPostListFetch("948", "传祺GS5").run();
	}
}
