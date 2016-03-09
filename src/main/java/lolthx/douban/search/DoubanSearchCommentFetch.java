package lolthx.douban.search;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.douban.search.bean.DoubanSearchCommentBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DoubanSearchCommentFetch extends DistributedParser {
	
	@Override
	public String getQueueName() {
		return "douban_search_comment";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		
		String projectName = task.getProjectName();
		String[] extras = task.getExtra().split(":");
		String keyword = extras[0];
		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "topic/","/?start");
		String title = doc.select("div#content h1").first().text();
		
		DoubanSearchCommentBean bean = null;
		
		Elements els = doc.select("ul#comments li.clearfix.comment-item");
		
		String start = StringUtils.substringAfter(url, "start=");
		int i = Integer.valueOf(start);
		System.out.println(">>>start " + start + "  >>> size : " + els.size());
		for(Element el : els){
			bean = new DoubanSearchCommentBean();
			i = i + 1;
			String authorUrl = el.select("div.reply-doc.content  h4  a").attr("href");
			String authorName = el.select("div.reply-doc.content  h4  a").text();
			String authorId = StringUtils.substringBetween(authorUrl, "people/", "/");
			String content = el.select("div.reply-doc.content p").text();
			String postTime = el.select("div.reply-doc.content span.pubtime").text();
			
			bean.setId(id);
			bean.setProjectName(projectName);
			bean.setKeyword(keyword);
			bean.setFloor(String.valueOf(i));
			bean.setUrl(url);
			bean.setAuthorId(authorId);
			bean.setAuthorName(authorName);
			bean.setTitle(title);
			bean.setComment(content);
			bean.setPostTime(postTime);
			
			bean.saveOnNotExist();
			
		}
	}
	
	@Override
	protected String getCookieDomain() {
		return "douban.com";
	}
	
	public static void main(String[] args){
		for(int i = 1 ; i <= 100 ;i++){
			new DoubanSearchCommentFetch().run();
		}

	}

}
