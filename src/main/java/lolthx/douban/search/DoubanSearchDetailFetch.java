package lolthx.douban.search;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.douban.search.bean.DoubanSearchBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DoubanSearchDetailFetch extends DistributedParser{
	
	
	
	@Override
	public String getQueueName() {
		return "douban_search_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		String projectName = task.getProjectName();
		String[] extras = task.getExtra().split(":");
		String keyword = extras[0];
		String reply = extras[1];
		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "topic/","/");
		
		Document doc = Jsoup.parse(result);
		String title = doc.select("div#content h1").first().text();
		Element textEl = doc.select("div#link-report div.topic-content").first();
		String content = textEl.text();
		{
			Elements els = textEl.select("img");
			for (Element el : els) {
				String attr = el.attr("src");
				content = content + " " + attr;
			}
		}
		
		String likeText = doc.select("div.sns-bar-fav span.fav-num").text();
		String likes = StringUtils.substringBefore(likeText, "人");
		String authorUrl = doc.select("div.topic-doc h3 span.from a").attr("href");
		String authorName = doc.select("div.topic-doc h3 span.from a").text();
		String authorId =  StringUtils.substringBetween(authorUrl, "people/","/");
		String postTime = doc.select("div.topic-doc h3 span.color-green").text();
		
		DoubanSearchBean bean = new DoubanSearchBean();
		bean.setId(id);
		bean.setProjectName(projectName);
		bean.setKeyword(keyword);
		bean.setUrl(url);
		bean.setAuthorId(authorId);
		bean.setAuthorName(authorName);
		bean.setTitle(title);
		bean.setContent(content);
		bean.setPostTime(postTime);
		bean.setReply(reply);
		bean.setLikes(likes);
		
		bean.saveOnNotExist();

	}
	
	@Override
	protected String getCookieDomain() {
		return "douban.com";
	}
	
	public static void main(String[] args){
		for(int i = 1 ;i <= 1;i++){
			new DoubanSearchDetailFetch().run();
		}
	}

}
