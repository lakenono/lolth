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
	public void testJson() throws IOException, InterruptedException {
		String url = "http://rate.tmall.com/list_detail_rate.htm?itemId=41323042631&sellerId=2196546755&currentPage=1";
		// String content = GlobalComponents.fetcher.document(url).text();
		String content = "\"rateDetail\":{\"paginator\":{\"items\":5,\"lastPage\":1,\"page\":1},\"rateCount\":{\"picNum\":0,\"shop\":0,\"total\":7,\"used\":0},\"rateDanceInfo\":{\"currentMilles\":1431939597848,\"intervalMilles\":90936872240,\"showChooseTopic\":false,\"storeType\":4},\"rateList\":["
				+ "{\"aliMallSeller\":false,\"anony\":true,\"appendComment\":\"\",\"attributes\":\"\",\"auctionSku\":\"\",\"buyCount\":0,\"carServiceLocation\":\"\",\"cmsSource\":\"天猫\",\"displayRatePic\":\"b_blue_1.gif\",\"displayRateSum\":350,\"displayUserLink\":\"\",\"displayUserNick\":\"走***8\",\"displayUserNumId\":\"\",\"displayUserRateLink\":\"\",\"dsr\":0.0,\"fromMall\":true,\"fromMemory\":0,\"id\":230425519046,\"pics\":\"\",\"position\":\"\",\"rateContent\":\"还行吧\",\"rateDate\":\"2014-12-10 17:53:12\",\"reply\":\"\"感谢您对\"\",\"serviceRateContent\":\"\",\"tamllSweetLevel\":2,\"tmallSweetPic\":\"tmall-grade-t2-18.png\",\"useful\":true,\"userIdEncryption\":\"\",\"userInfo\":\"\",\"userVipLevel\":0,\"userVipPic\":\"\"},"
				+ "{\"aliMallSeller\":false,\"anony\":true,\"appendComment\":\"\",\"attributes\":\"\",\"auctionSku\":\"\",\"buyCount\":0,\"carServiceLocation\":\"\",\"cmsSource\":\"天猫\",\"displayRatePic\":\"b_red_5.gif\",\"displayRateSum\":210,\"displayUserLink\":\"\",\"displayUserNick\":\"纹***4\",\"displayUserNumId\":\"\",\"displayUserRateLink\":\"\",\"dsr\":0.0,\"fromMall\":true,\"fromMemory\":0,\"id\":224571199751,\"pics\":\"\",\"position\":\"\",\"rateContent\":\"假期原！\",\"rateDate\":\"2014-10-08 16:28:57\",\"reply\":\"\",\"serviceRateContent\":\"\",\"tamllSweetLevel\":2,\"tmallSweetPic\":\"tmall-grade-t2-18.png\",\"useful\":true,\"userIdEncryption\":\"\",\"userInfo\":\"\",\"userVipLevel\":0,\"userVipPic\":\"\"},"

				+ "{\"aliMallSeller\":false,\"anony\":true,\"appendComment\":\"\",\"attributes\":\"\",\"auctionSku\":\"\",\"buyCount\":0,\"carServiceLocation\":\"\",\"cmsSource\":\"天猫\",\"displayRatePic\":\"b_blue_2.gif\",\"displayRateSum\":692,\"displayUserLink\":\"\",\"displayUserNick\":\"e***n\",\"displayUserNumId\":\"\",\"displayUserRateLink\":\"\",\"dsr\":0.0,\"fromMall\":true,\"fromMemory\":0,\"id\":224306169887,\"pics\":\"\",\"position\":\"\",\"rateContent\":\"很不错的哦，必须给五分\",\"rateDate\":\"2014-10-03 16:59:14\",\"reply\":\"\",\"serviceRateContent\":\"\",\"tamllSweetLevel\":3,\"tmallSweetPic\":\"tmall-grade-t3-18.png\",\"useful\":true,\"userIdEncryption\":\"\",\"userInfo\":\"\",\"userVipLevel\":0,\"userVipPic\":\"\"}" + "],\"tags\":\"\"}";
		if (content.indexOf("reply\":\"\"") > -1) {
			content.replaceFirst("reply\":\"\"", "reply\":\"").replaceFirst("\"\"", "\"");
		}
		String json = "{" + content + "}";
		// JSONArray parseArray = JSON.parseArray(json);
		JSONObject parseObject = JSON.parseObject(json);
		System.out.println(parseObject);
	}
}
