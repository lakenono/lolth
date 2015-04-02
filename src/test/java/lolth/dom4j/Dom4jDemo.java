package lolth.dom4j;

import java.io.File;
import java.io.IOException;

import lolth.weixin.sogou.bean.WeiXinBean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Dom4jDemo
{

	public static void main(String[] args) throws DocumentException, IOException
	{
		File file = new File(Dom4jDemo.class.getResource("demo.xml").getFile());
		String html = FileUtils.readFileToString(file);

		html = StringUtils.substringBetween(html, "items\":[\"", "\"],\"totalItems");
		html = StringUtils.remove(html, "\\");
		html = StringUtils.remove(html, "");

		Document document = Jsoup.parse(html);

		Elements elements = document.select("document");

		for (Element element : elements)
		{
			System.out.println(element.html());

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

			System.out.println(bean.toString());

			break;

		}

		System.out.println(elements.size());
	}
}
