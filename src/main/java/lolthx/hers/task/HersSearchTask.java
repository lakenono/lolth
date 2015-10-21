package lolthx.hers.task;

import java.net.URLDecoder;
import java.text.MessageFormat;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HersSearchTask{
	private String keyword;
	private String projectName;
	public static final String QUEUE_PAGE = "hers_page";
	public static final String QUEUE_BBS = "hers_bbs";
	private String base_url = "http://bbs.hers.com.cn/search.php?srchtxt={0}&searchsubmit=yes";
	
	public HersSearchTask(String projectName, String keyword) {
		this.projectName = projectName;
		this.keyword = keyword;
	}

	public void run() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl());
		String pages = document.select("span[title]").text();
		String href = document.select("div.pg > a ").first().attr("href");
		if(!StringUtils.isBlank(pages)){
			pages = StringUtils.substringBetween(pages, "/", "页").trim();
		}
		//发送帖子队列
		sendHersBbs(document);
		//发送页面队列
		if(StringUtils.isBlank(href)){
			return;
		}
		int p = Integer.parseInt(pages);
		href = href.substring(0, href.length()-1);
		for(int i = 2;i<=p;i++){
			//发送task
			String url = "http://bbs.hers.com.cn/"+href+i;
			buildTask(url,QUEUE_PAGE,this.keyword);
		}
	}

	public void sendHersBbs(Document document) {
		Elements elements = document.select("li.pbw[id]");
		for (Element element : elements) {
			String id = element.attr("id");
			String url = "http://bbs.hers.com.cn/thread-"+id+"-1-1.html";
			buildTask(url,QUEUE_BBS,id);
		}
	}

	private void buildTask(String url,String queueName,String id) {
		Task task = new Task();
		task.setQueueName(queueName);
		task.setUrl(url);
		task.setProjectName(this.projectName);
		task.setExtra(id);
		Queue.push(task);
	}

	private String buildUrl() throws Exception {
		return MessageFormat.format(base_url, URLDecoder.decode(keyword, "utf-8"));
	}
}
