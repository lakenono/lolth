package lolth.weibo.com.fetch.comment;

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
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class CommentTask extends FetchTaskProducer {

	private static final String WEIBO_TEXT_COMMENT_URL = "http://weibo.com/aj/v6/comment/big?ajwvr=6&id={0}{1}{2}&__rnd=";

	public static final String WEIBO_TEXT_COMMENT = "weibo_text_comment";

	public static final String COOKIS = "SINAGLOBAL=3323087713215.5.1447989241864; UOR=hexiaoqiao.sinaapp.com,widget.weibo.com,ent.ifeng.com; YF-Page-G0=602506db2d7072c030a3784f887e1d83; _s_tentry=-; Apache=8965875681024.045.1448500392432; ULV=1448500392519:6:6:5:8965875681024.045.1448500392432:1448446090747; YF-V5-G0=9717632f62066ddd544bf04f733ad50a; YF-Ugrow-G0=5b31332af1361e117ff29bb32e4d8439; WBtopGlobal_register_version=0b6ec8a06b61dd96; SUS=SID-2126405771-1448503147-GZ-kh2d9-0c387eea25d8c98aaa649bae08c9ac70; SUE=es%3D16953a1d24df6b98bcace54b401fc06e%26ev%3Dv1%26es2%3D6eece7ca5d5a65fe927c36e988b54271%26rs0%3D0gCWT%252BnMasScJPFFBBR%252F8y2w%252BdnzLwrXfD0j68t9GnIPA6hf3Ni2VPClSiL9L1i7ArI4j%252BFVLxYMlMTe8YO1fZVUYz4QposILdtco59v2s4F7d9clYMV%252Ba%252FAzFfONUQgIFvkLCl0HhnkuS6glGw5683JNC2iyV1BZ3HR%252FkcTLs8%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1448503147%26et%3D1448589547%26d%3Dc909%26i%3Dac70%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D2126405771%26name%3Dgengbushuang%2540163.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B72126405771%26fmp%3D%26lcp%3D2015-02-19%252023%253A51%253A10; SUB=_2A257Uhc7DeTxGeRP6VQV8CvLzD2IHXVYJg_zrDV8PUNbuNBeLUbAkW8cRSoBZiGPf3cTX7q6Jmslj8LRWg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFA8sHHd-q.5Iqfxrnyskqo5JpX5K2t; SUHB=0laUGB3PsNxLsJ; ALF=1449107963; SSOLoginState=1448503147; un=gengbushuang@163.com";

	private String subject;
	private String id;
	private String max_id = "";

	HttpFetcher fetcher = new HttpClientFetcher();

	public CommentTask(String id, String subject) {
		super(WEIBO_TEXT_COMMENT);
		this.id = id;
		this.subject = subject;
		// TODO Auto-generated constructor stub
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
		String url = buildUrl(0) + System.currentTimeMillis();
		HttpRequest req = new HttpRequest();
		req.setNeedContent(true);
		req.setUrl(url);
		req.setCookies(CookiesUtils.getCookies(COOKIS));

		String json;
		try {
			json = new String(fetcher.run(req).getContent());
		} catch (Exception e) {
			log.error("解析最大页数出错了！", e);
			return 0;
		}

		try {
			String page = parseJson(json);
			if (StringUtils.isNumeric(page)) {
				return Integer.parseInt(page);
			}
		} catch (Exception e) {
			log.error("解析json出错了！", e);
		}

		return 0;
	}

	private String parseJson(String json) {
		String substringBetween = StringUtils.substringBetween(json, "&max_id=", "&page=1");
		if(StringUtils.isNumeric(substringBetween)){
			max_id = "&max_id="+substringBetween;
		}else{
			max_id="";
		}
		Object obj = JSON.parseObject(json).getJSONObject("data").getJSONObject("page").get("totalpage");
		String page = obj.toString();
		return page;
	}

	public String buildUrl(int page) {

		return MessageFormat.format(WEIBO_TEXT_COMMENT_URL, id, max_id, (page != 0 ? "&page=" + page : ""));
	}

	public FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(subject);
		task.setBatchName(WEIBO_TEXT_COMMENT);
		task.setUrl(url);
		task.setExtra(id);
		return task;
	}

	public static void main(String[] args) {
		String id = "3909481824199797";
		new CommentTask(id, "测试").run();
	}

}
