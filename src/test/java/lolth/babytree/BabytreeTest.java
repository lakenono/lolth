package lolth.babytree;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lolth.babytree.bbs.BabytreeBBSSearchDetailFetch;
import lolth.babytree.bbs.BabytreeBBSSearchDetailTaskProducer;
import lolth.babytree.bbs.BabytreeBBSSearchList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class BabytreeTest {

	
	@Test
	public void testPushListTask() throws Exception{
		String taskQueueName = BabytreeBBSSearchList.BABYTREE_BBS_LIST;
		String url = "http://www.babytree.com/s.php?q=%E6%83%A0%E6%B0%8F%E5%90%AF%E8%B5%8B&c=community&cid=0&range=&pg=6";
		FetchTask task = new FetchTask();
		task.setUrl(url);
		BabytreeBBSSearchDetailTaskProducer searchDetailTaskProducer = new BabytreeBBSSearchDetailTaskProducer(taskQueueName);
		searchDetailTaskProducer.handleTask(task);
	}
	
	@Test
	public void testDetailTask() throws Exception{
		String keyword = "惠氏启赋";
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String taskQueueName = BabytreeBBSSearchDetailTaskProducer.BABYTREE_BBS_LIST_DETAIL;
		String url = "http://www.babytree.com/community/hospital37876/topic_40202315.html";
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setUrl(url);
		BabytreeBBSSearchDetailFetch bbsSearchDetailFetch = new BabytreeBBSSearchDetailFetch(taskQueueName);
		bbsSearchDetailFetch.handleTask(task);
		
	}
	
	@Test
	public void testSex() throws IOException, InterruptedException{
		String url = "http://home.babytree.com/u858687441586";
		Document document = GlobalComponents.fetcher.document(url);
		Elements select = document.select("#mytree-basic-info > ul > li:nth-child(2) > span");
		System.out.println(select.attr("class"));
	}
}
