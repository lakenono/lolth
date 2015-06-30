package lolth.weixin.sogou;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.fetch.handlers.PageFetchHandler;
import lakenono.log.BaseLog;
import lolth.weixin.sogou.bean.WeiXinBean;
import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AllArgsConstructor
public class WeiXinUserArticleFetch extends BaseLog implements PageFetchHandler

{
	private static String taskName = "buick_0629";
	
	public static void main(String[] args) throws Exception
	{
		while (true)
		{
			try
			{
				
				new WeiXinUserArticleFetch("wenshucheyun","oIWsFt8LFQXZTcEIGXDfnwLdLBEQ","AwsBo%2B0gljwEoGMvGpH6CuEnEZGktCUJ0F5M2NfjTscenKg5gOIgo9FpkoZXvY1SxmXrq").run();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	private String username;
	private String openid;
	private String eqs;

	@Override
	public void run() throws IOException, InterruptedException, SQLException, Exception
	{
		try {
			int maxPage = this.getMaxPage();

			for (int i =1; i <= maxPage; i++)
			{
				String taskname = MessageFormat.format("weixin_user-{0}-{1}-{2}", taskName,this.username, i);

				if (GlobalComponents.taskService.isCompleted(taskname))
				{
					//这里应该不需要++
//				i++;
					this.log.info("task {} is completed", taskname);
					continue;
				}

				this.log.info("keyword[{}] {}/{}...", this.username, i, maxPage);

				try {
					this.process(i);
				} catch (Exception e) {
					e.printStackTrace();
				}

				GlobalComponents.taskService.success(taskname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(int i) throws Exception
	{
		String html = GlobalComponents.dynamicFetch.fetch(this.buildUrl(i));

		if (StringUtils.contains(html, "您的访问出现了一个错误"))
		{
			throw new RuntimeException("爬取失败页面.. sleep");
		}

		html = Jsoup.parse(html).text();

		html = StringUtils.substringBetween(html, "items\":[\"", "\"]");
		html = StringUtils.remove(html, "\\");
		html = StringUtils.remove(html, "");

		Document document = Jsoup.parse(html);

		Elements elements = document.select("document");

		for (Element element : elements)
		{
			WeiXinBean bean = new WeiXinBean();

			String title = element.select("title1").first().text();
			bean.setTitle(title);

			String url = element.select("url").first().text();
			bean.setUrl(url);

			String postTime = element.select("date").first().text();
			bean.setPostTime(postTime);

			String authorname = element.select("sourcename").first().text();
			bean.setAuthorname(authorname);

			String openid = element.select("openid").first().text();
			bean.setAuthorurl("http://weixin.sogou.com/gzh?openid=" + openid);

			bean.setAuthorid(openid);

			bean.setKeyword(this.username);

			bean.persist();
			this.log.info(bean.toString());
		}
		Thread.sleep(15000);
	}

	@Override
	public int getMaxPage() throws Exception
	{
		String url = this.buildUrl(1);
		String html = GlobalComponents.dynamicFetch.fetch(url);
		html = StringUtils.substringBetween(html, "totalPages\":", ",");
		Thread.sleep(15000);
		return Integer.parseInt(html);
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		String url = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid={0}&eqs={1}&ekv=7&page={2}";
		return MessageFormat.format(url, this.openid,this.eqs, pageNum);
	}

}
