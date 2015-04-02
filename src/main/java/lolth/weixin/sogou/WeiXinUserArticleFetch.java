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
	public static void main(String[] args) throws Exception
	{
		WeiXinUserArticleFetch fetch = new WeiXinUserArticleFetch("奧迪", "oIWsFtw87yg5Yjhh6vFO7lr9zf_Y");
		fetch.process(0);
	}

	private String username;
	private String openid;

	@Override
	public void run() throws IOException, InterruptedException, SQLException, Exception
	{

	}

	@Override
	public void process(int i) throws Exception
	{
		String html = GlobalComponents.dynamicFetch.fetch(this.buildUrl(i));

		html = Jsoup.parse(html).text();

		html = StringUtils.substringBetween(html, "items\":[\"", "\"],\"totalItems");
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
	}

	@Override
	public int getMaxPage() throws Exception
	{
		String html = GlobalComponents.dynamicFetch.fetch(this.buildUrl(0));
		html = StringUtils.substringBetween(html, "totalPages\":", "}");
		return Integer.parseInt(html);
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		String url = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid={0}&page={1}";
		return MessageFormat.format(url, this.openid, pageNum + 1);
	}

}
