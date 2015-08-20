package lolthx.weixin.sogou;

import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetcher.Fetcher;
import lolthx.weixin.bean.WeiXinBean;

public class WeiXinUserArtListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "weixin_user_art_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		result = StringUtils.substringBetween(result, "items\":[\"", "\"]})");
		result = StringUtils.remove(result, "\\");
		result = StringUtils.remove(result, "");

		Document document = Jsoup.parse(result);
		Elements elements = document.select("document");
		WeiXinBean bean = null;
		for (Element element : elements) {

			try {
				bean = new WeiXinBean();

				String url = element.select("url").first().text();
				bean.setUrl(url);

				String id = StringUtils.substringBetween(url, "&sn=", "&3rd=");
				bean.setId(id);

				String title = element.select("title1").first().text();
				bean.setTitle(title);

				String postTime = element.select("date").first().text();
				bean.setPostTime(postTime);

				String authorname = element.select("sourcename").first().text();
				bean.setAuthorname(authorname);

				String openid = element.select("openid").first().text();
				bean.setAuthorurl("http://weixin.sogou.com/gzh?openid=" + openid);

				bean.setAuthorid(openid);
				
				bean.setKeyword(task.getExtra());
				bean.setProjectName(task.getProjectName());
				
				Thread.sleep(2000);
				
				String cookieDomain = getCookieDomain();
				String cookies = GlobalComponents.authService.getCookies(cookieDomain);
				
				String texthtml = GlobalComponents.jsoupFetcher.fetch(url, cookies, "utf-8");
				Document textdoc = Jsoup.parse(texthtml);

				String text = textdoc.text();
				bean.setText(text);
				
				bean.saveOnNotExist();

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

	}
	
	public static void main(String args[]) throws TException{
		new WeiXinUserArtListFetch().run();
	}

	@Override
	protected String getCookieDomain() {
		return "weixin.sogou.com";
	}

	
}
