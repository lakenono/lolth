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
	
	public static final String COOKIS = "ALC=ac%3D2%26bt%3D1432092864%26cv%3D5.0%26et%3D1463628864%26uid%3D1846456555%26vf%3D0%26vt%3D0%26es%3D7f7d66fac95dcdb05afebe0bebf25311;ALF=1463628864;ALF=1463628864;LT=1432092864;SUB=_2A254WHCQDeTxGedG71QV9SjJzjmIHXVbLOVYrDV_PUNbuNBeLXHZkW8eIMkpxLCsN2RzqjoWSj86f0C74w..;SUB=_2A254WHCQDeTxGedG71QV9SjJzjmIHXVbLOVYrDV8PUNbuNANLXbnkW-e4ysLYyq3WHizVPdPclh793VR3A..;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5Y3Mx2vjD-oHkbBkq65g-R;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5Y3Mx2vjD-oHkbBkq65g-R5JpX5K2t;SUE=es%3D4347aa4ba5dfd2a29590653d97e6cb7d%26ev%3Dv1%26es2%3D80f683bf6ec80874a05e8e56378b652a%26rs0%3D5kw88xncZ8a%252FjcK9vkbV7uUm1dpuJ5F1e82wbt4SBiJnt5bK8N8KaJHrB85xQmXPoTnn2UnLEUZ81z%252BlR9PKdzwlxdZSk%252FVH6k0Q07q%252FbIoUjmZy9x6J5wnzFl75ZQ9NaCf9UEHb%252B5TpOV%252B4MqSEYqApGbWa%252BPDaol05KKepgEk%253D%26rv%3D0;SUE=es%3D9fa60833a08eb4c031323f7f2d1270eb%26ev%3Dv1%26es2%3D795576d21757647b3483abd808e45170%26rs0%3DIXxf2iYgwWEUa8Rq1AwvANd3MCO7dv15kH4etgbvaOylwmX8d%252BYZDkGNvcHMTycTC2TSGWP7nHWsdNbbYifvksJM6xOFT%252FQfFrwG2f7sibUHwIqkSRbw%252FxHt6to933NRlioj98SU3KGBPr1%252BtqHacaJWe25kjJgrgi7WdTXjdJE%253D%26rv%3D0;SUHB=0h1InAcdy9WAEC;SUP=cv%3D1%26bt%3D1432092864%26et%3D1432179264%26d%3D40c3%26i%3Dac70%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26lt%3D1%26uid%3D1846456555%26user%3D174997195%2540qq.com%26ag%3D4%26name%3D174997195%2540qq.com%26nick%3D174997195%26sex%3D%26ps%3D0%26email%3D%26dob%3D%26ln%3D15131955110%26os%3D%26fmp%3D%26lcp%3D2014-12-25%252018%253A47%253A43;SUP=cv%3D1%26bt%3D1432092864%26et%3D1432179264%26d%3Dc909%26i%3Dbc51%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D1846456555%26name%3D174997195%2540qq.com%26nick%3D174997195%26fmp%3D%26lcp%3D2014-12-25%252018%253A47%253A43;SUS=SID-1846456555-1432092864-GZ-z4rst-41e96cc1f12d9122b70ebb647be0ac70;SUS=SID-1846456555-1432092864-JA-u2enw-382236f9f3207906d2f3eff07012ac70;YF-Ugrow-G0=98bd50ad2e44607f8f0afd849e67c645;YF-V5-G0=e6f12d86f222067e0079d729f0a701bc;sso_info=v02m6alo5qztLGNs5C5jpOcsY6TlKadlqWkj5OEuI2DmLSNk5i1jZOUwA==;tgc=TGT-MTg0NjQ1NjU1NQ==-1432092864-gz-7FFA276DE7FF437167A40CB16679778D";
	
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
