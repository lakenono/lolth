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
	
	public static final String COOKIS = "ALC=ac%3D0%26bt%3D1432175160%26cv%3D5.0%26et%3D1463711160%26uid%3D5613102598%26vf%3D0%26vt%3D0%26es%3Dac6db93d92b98ff9afe4ccfce286257b;ALF=1463711160;ALF=1463711160;LT=1432175160;SUB=_2A254WTJoDeTxGeNI6lEQ8CzJwjSIHXVbLySgrDV_PUNbuNBeLUOgkW8znUqNSz6aS_e14hSW-Kf5RYQrsw..;SUB=_2A254WTJoDeTxGeNI6lEQ8CzJwjSIHXVbLySgrDV8PUNbuNANLVnnkW8n6Aw6_KfUDLhMlR0IMpyqWgaLUg..;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5F6UR9bw47v.XEvjC.OYLa;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5F6UR9bw47v.XEvjC.OYLa5JpX5K2t;SUE=es%3D597617fec61f527f0f0411e940ae11f3%26ev%3Dv1%26es2%3D561595029705519946719be30fb5923b%26rs0%3DBL26fJi%252BKYekPOpw21AbBmLpU5cKgir1%252B8FgmNnbR15snrTEl7amLPZ9sUmBirmpM9MxmssV%252Fzct6XeRubnm52HlSf65%252BglhfjVYmQKsdscss7Ai%252B1a9fW2snpqNwwnT7JC2QiB%252BVUa2UZue65crjjrDCSKUiLb593p60LaUvgs%253D%26rv%3D0;SUE=es%3D49ab121bd4b0d782b1dcd78806dbfe3a%26ev%3Dv1%26es2%3D0ce9186f57c893f6e9d77b84ff33efed%26rs0%3DWLibBTqgIP%252B2XBo0fKwHHcfU9R9X00BCISSEoMnamOGeOABiWTIiUX13GwNUkrfIU4kqzq0proiVL%252Batm0dGOgzxb40DyO7mDPd3vDvWwKk0DEPWBVg8OiQNma%252BAQ8ivzWMCrQeODNOzIU1HobqwtUZWZzBPmRqUO56Ph2hN6Rk%253D%26rv%3D0;SUHB=00xpCXbKu0KlRg;SUP=cv%3D1%26bt%3D1432175160%26et%3D1432261560%26d%3D40c3%26i%3Dac70%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26lt%3D1%26uid%3D5613102598%26user%3D15120039685%26ag%3D1%26name%3D15120039685%26nick%3D%25E5%25B0%258F%25E9%25B9%258F%25E5%25B0%258F%25E8%25B4%25BA%26sex%3D1%26ps%3D0%26email%3D%26dob%3D%26ln%3D15120039685%26os%3D%26fmp%3D%26lcp%3D;SUP=cv%3D1%26bt%3D1432175160%26et%3D1432261560%26d%3Dc909%26i%3D990d%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D5613102598%26name%3D15120039685%26nick%3D%25E5%25B0%258F%25E9%25B9%258F%25E5%25B0%258F%25E8%25B4%25BA%26fmp%3D%26lcp%3D;SUS=SID-5613102598-1432175160-GZ-2u1ol-7d5033ca0833de8b1a5acdd95411ac70;SUS=SID-5613102598-1432175160-JA-820y4-e5365f11466818db4229bde471dbac70;YF-Ugrow-G0=69bfe9ce876ec10bd6de7dcebfb6883e;YF-V5-G0=4955da6a9f369238c2a1bc4f70789871;sso_info=v02m6alo5qztKWRk5SlkKOApY6EmKWRk6SlkKOkpY6EmKWRk5SlkKOApY6EmKWRk6ClkKOQpZCkhKadlqWkj5OUtoyTjLGMg4i1jpOgwA==;tgc=TGT-NTYxMzEwMjU5OA==-1432175160-gz-8CC87F81F25B51AF7742DEAFD9795662";
	
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
		FetchTask task = new FetchTask();
		task.setName(subject);
		task.setBatchName(WEIBO_TEXT_COMMENT_REPOST);
		task.setUrl(url);
		return task;
	}
	public static void main(String[] args) {
		new CommentRepostTask().run();
	}
}
