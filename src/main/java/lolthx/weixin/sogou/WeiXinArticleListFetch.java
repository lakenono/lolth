package lolthx.weixin.sogou;

import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetcher.Fetcher;
import lolthx.weixin.bean.WeiXinBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WeiXinArticleListFetch extends DistributedParser {

	
	
	@Override
	public String getQueueName() {
		return "weixin_article_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);

		Elements elements = doc.select("div.results div.wx-rb");
		WeiXinBean bean = null;

		for (Element element : elements) {

			try {
				bean = new WeiXinBean();
				// title
				String title = element.select("div.txt-box h4 a").first().text();
				bean.setTitle(title);

				// url
				String url = "http://weixin.sogou.com" + element.select("div.txt-box h4 a").first().attr("href");
				bean.setId(url);
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

				bean.setKeyword(task.getExtra());
				bean.setProjectName(task.getProjectName());
				
				//线程休眠15秒
				Thread.sleep(15000);
				
				//String cookieDomain = getCookieDomain();
				//String texthtml = GlobalComponents.jsoupFetcher.fetch(url, cookies, "utf-8");
				String texthtml = GlobalComponents.jsoupFetcher.fetch(url);
				
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
	

	public static void main(String args[]) {
		for(int i = 1; i <= 5 ; i++){
			new WeiXinArticleListFetch().run();
		}
		
	}
	
}
