package lolth.weibo.com.fetch.repost;

import java.text.MessageFormat;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lakenono.fetch.adv.utils.CookiesUtils;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

@Slf4j
public class CommentRepostTask extends FetchTaskProducer{

	private static final String WEIBO_TEXT_COMMENT_REPOST_URL = "http://weibo.com/aj/v6/mblog/info/big?ajwvr=6&id=3841253424934874&max_id=3844256127253387&page={0}&__rnd=";

	public static final String WEIBO_TEXT_COMMENT_REPOST = "weibo_text_comment_repost";
	
	public static final String COOKIS = "ALC=ac%3D0%26bt%3D1432200497%26cv%3D5.0%26et%3D1463736497%26uid%3D5613159407%26vf%3D0%26vt%3D0%26es%3Dd2297ec0cb9ac79cade279f607a487ed;ALF=1463736497;ALF=1463736497;LT=1432200497;SUB=_2A254WdVgDeTxGeNI6lEQ9SfIyzuIHXVbL0GorDV_PUNbuNBeLWHykW-YIshgHQ_Rh8_DFoT6L2RbBeBSGg..;SUB=_2A254WdVhDeTxGeNI6lEQ9SfIyzuIHXVbL0GprDV8PUNbuNAKLRStkW-VpaTMkhiVT3v9GxHoJK2Q8iE9wA..;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhTBqawkffjcjxOeG2n7TIS;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhTBqawkffjcjxOeG2n7TIS5JpX5K2t;SUE=es%3D889261c1f2ab1477158337738ca1e732%26ev%3Dv1%26es2%3D6979aebb5517ca7a31892771c5ca16b4%26rs0%3DPq0tqJZDgmxEx3luhcbk44i8F8DctTVLEipYjKbjtXpzRJF9d16uQewSxGSDUkwLXf32HDrXCGadHVdLZWK%252BRtAU1xZX8gJrsHCAKJpPmWxRbWi6o%252BIPOXaB5pQ2qjBLt6c9%252FA7daN3KQXtS2sR7PP4gfXRch1tjdyqKhj97EOQ%253D%26rv%3D0;SUE=es%3D8b5123829515cd8e4c2e3685d3c794d9%26ev%3Dv1%26es2%3De3fd418aa7091a0a28b8977e3efe4e87%26rs0%3DFOguxYhQBzF6KKk0KHWZVJSD%252FEilROOhDo8biNM1S2403n8KYfP6HHkEZwHT5sp3WAEIQcGYf022F8oNVdTlsvKglMZ47aj9rkLcqcDHvzZ2%252BwqjcfPw3e2ESDgT71fMuzz1%252BaoX5QaJqXFhDRiQZAq5VCskCsN6spI5D4VOKKU%253D%26rv%3D0;SUHB=0z1Qd63GGQCXXR;SUP=cv%3D1%26bt%3D1432200496%26et%3D1432286896%26d%3D40c3%26i%3Dac70%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D2%26lt%3D1%26uid%3D5613159407%26user%3D1537405174%2540qq.com%26ag%3D4%26name%3D1537405174%2540qq.com%26nick%3D%25E6%2588%2591%25E6%2598%25AF%25E5%25B0%258F%25E5%259D%258F%25E8%259B%258B%25E4%25B9%258B%25E6%25AD%258C%26sex%3D1%26ps%3D0%26email%3D%26dob%3D%26ln%3D1537405174%2540qq.com%26os%3D%26fmp%3D%26lcp%3D;SUP=cv%3D1%26bt%3D1432200497%26et%3D1432286897%26d%3Dc909%26i%3D6ca9%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D2%26uid%3D5613159407%26name%3D1537405174%2540qq.com%26nick%3D%25E6%2588%2591%25E6%2598%25AF%25E5%25B0%258F%25E5%259D%258F%25E8%259B%258B%25E4%25B9%258B%25E6%25AD%258C%26fmp%3D%26lcp%3D;SUS=SID-5613159407-1432200497-GZ-e5l7p-a7111e8f68d31d66600ac93bb4f5ac70;SUS=SID-5613159407-1432200497-XD-0tvoh-f2ec527bd80ccd4d9e801ba56cecac70;YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df;YF-V5-G0=9717632f62066ddd544bf04f733ad50a;sso_info=v02m6alo5qztKWRk5iljoOgpY6ThKWRk5iljpOgpZCUmKWRk5SlkKOApY6EmKWRk5SljpSQpY6EmKWRk6CljpSIpY6EiKWRk5ClkKOkpY6EiKWRk5ilkJSQpY6EjKadlqWkj5OUtoyTjLGNk6S0jIOcwA==;tgc=TGT-NTYxMzE1OTQwNw==-1432200496-gz-643D34D88A449A389DE0BD8C2FE03392";
	
	private static String subject = "cmcc";
	
	HttpFetcher fetcher = new HttpClientFetcher();
	
	public CommentRepostTask() {
		super(WEIBO_TEXT_COMMENT_REPOST);
	}

	public void run() {
		log.info("FetchTask Producer start ...");
		int pages = getMaxPage();
		// 提取最大页失败
		if (pages == 0) {
			log.error("Get max page fail ! ");
			return;
		}
		log.info("Get max page : " + pages);
		// 迭代
		for (int i = 1; i <= pages; i++) {
			// 创建url
			String url = buildUrl(i);
			// 创建抓取任务
			FetchTask task = buildTask(url);
			// 推送任务
			try {
				super.saveAndPushTask(task);
			} catch (Exception e) {
				log.error("{} push error!", task, e);
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
	

	public int getMaxPage() {
		String url = buildUrl(1)+System.currentTimeMillis();
		HttpRequest req = new HttpRequest();
		req.setNeedContent(true);
		req.setUrl(url);
		req.setCookies(CookiesUtils.getCookies(COOKIS));

		String json;
		try {
			json = new String(fetcher.run(req).getContent());
		} catch (Exception e) {
			log.error("解析最大页数出错了！",e);
			return 1;
		}
		System.out.println(json);
		Object obj = JSON.parseObject(json).getJSONObject("data").getJSONObject("page").get("totalpage");
		String page = obj.toString();
		if(StringUtils.isNumeric(page)){
			return Integer.parseInt(page);
		}
		return 1;
	}

	public String buildUrl(int page) {
		return MessageFormat.format(WEIBO_TEXT_COMMENT_REPOST_URL, page);
	}

	public FetchTask buildTask(String url){
		return null;
//		FetchTask task = new FetchTask();
//		task.setName(subject);
//		task.setBatchName(WEIBO_TEXT_COMMENT_REPOST);
//		task.setUrl(url);
//		return task;
	}
	public static void main(String[] args) {
		new CommentRepostTask().run();
	}
}
