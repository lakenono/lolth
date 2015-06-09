package lolth.babytree;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lolth.babytree.bbs.BabytreeBBSSearchDetailFetch;
import lolth.babytree.bbs.BabytreeBBSSearchDetailTaskProducer;
import lolth.babytree.bbs.BabytreeBBSSearchList;
import lolth.jd.search.bean.CommodityBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BabytreeTest {

	@Test
	public void testPushListTask() throws Exception {
		String taskQueueName = BabytreeBBSSearchList.BABYTREE_BBS_LIST;
		String url = "http://www.babytree.com/s.php?q=%E6%83%A0%E6%B0%8F%E5%90%AF%E8%B5%8B&c=community&cid=0&range=&pg=6";
		FetchTask task = new FetchTask();
		task.setUrl(url);
		BabytreeBBSSearchDetailTaskProducer searchDetailTaskProducer = new BabytreeBBSSearchDetailTaskProducer(taskQueueName);
		searchDetailTaskProducer.handleTask(task);
	}

	@Test
	public void testDetailTask() throws Exception {
		String keyword = "惠氏启赋";
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String taskQueueName = BabytreeBBSSearchDetailTaskProducer.BABYTREE_BBS_LIST_DETAIL;
		String url = "http://www.babytree.com/community/club201401/topic_39721459.html";
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setUrl(url);
		BabytreeBBSSearchDetailFetch bbsSearchDetailFetch = new BabytreeBBSSearchDetailFetch(taskQueueName);
		bbsSearchDetailFetch.handleTask(task);

	}

	@Test
	public void testSex() throws IOException, InterruptedException {
		String url = "http://home.babytree.com/u858687441586";
		Document document = GlobalComponents.fetcher.document(url);
		Elements select = document.select("#mytree-basic-info > ul > li:nth-child(2) > span");
		System.out.println(select.attr("class"));
	}

	@Test
	public void testDD() throws Exception {
		String url = "http://item.jd.com/1070843.html";
		String id = StringUtils.substringBetween(url, "com/", ".html");
		Document doc = GlobalComponents.dynamicFetch.document(url);
		CommodityBean commodityBean = new CommodityBean();
		commodityBean.setId(id);
		commodityBean.setUrl(url);
		Elements elements = doc.select("#name");
		if (!elements.isEmpty()) {
			String title = elements.text();
			commodityBean.setTitle(title);
		}

		elements = doc.select("#comment-count > a");
		if (!elements.isEmpty()) {
			String reply = elements.text();
			commodityBean.setReply(reply);
		}

		elements = doc.select("#product-detail-2 > table tr");
		System.out.println(elements);
		if (!elements.isEmpty()) {
			String value;
			for (Element element : elements) {
				value = element.text();
				System.out.println(value);
				if (value.indexOf("类别") > -1) {
					commodityBean.setCategory(StringUtils.substring(value, 3));
				} else if (value.indexOf("特性") > -1) {
					commodityBean.setFeatures(StringUtils.substring(value, 3));
				}
			}
		}else{
			//#parameter2
			elements = doc.select("#parameter2 > li");
			if (!elements.isEmpty()) {
				String value;
				for (Element element : elements) {
					value = element.text();
					System.out.println(value);
					if (value.indexOf("类别") > -1) {
						commodityBean.setCategory(StringUtils.substring(value, 3));
					} else if (value.indexOf("特性") > -1) {
						commodityBean.setFeatures(StringUtils.substring(value, 3));
					}
				}
			}
		}
		System.out.println(commodityBean.toString());
	}

	@Test
	public void te() throws IOException, InterruptedException{
		String url = "http://search.jd.com/s.php?keyword=%E5%AE%89%E4%BD%B3&enc=utf-8&qrst=1&rt=1&cid2=1585&click=2-1585&cs=y&vt=2";
		Document doc = GlobalComponents.fetcher.document(url);
		Elements links = doc.select("div.lh-wrap>div.p-name a");
		for(Element link:links){
			System.out.println(link.attr("href"));
			Task newTask = new Task();
			newTask.setUrl(link.attr("href"));
			newTask.setProjectName("威仕高");
			newTask.setQueueName("jd_commodity");
			newTask.setExtra("安佳");
			Queue.push(newTask);
		}
	}
}
