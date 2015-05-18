package lolth.weibo.task;

import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lolth.weibo.fetcher.WeiboFetcher;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboUserMainPageTaskProducer extends PagingFetchTaskProducer {
	private static final String USER_MAIN_PAGE_URL_TEMPLATE = "http://weibo.cn/{0}?page={1}";
	
	public static final String USER_MAIN_PAGE = "user_main_page";
	private String user = null;
	private String keyword = null;

	public static void main(String[] args) {
		String taskname = "惠氏";
		
		String[] users = {
				"iaudi"
				,"bmwchina"
				,"mymb"
				,"yifuyun"
				,"fashiontyy"
				,"2783989451"
				,"sixinying"
				,"weed9999647"
				,"1500268464"
				,"bosspat"
				,"jsta"
				,"liuzhanshu"
				,"eric7777"
				,"zuima"
				,"sundance945"
				,"muyuan1989"
				,"206784449"
				,"1955949053"
				,"zylove118"
				,"831020777"
				,"h1101y0906"
				,"207171792"
				,"sdjsfee"
				,"527303123"
				,"2924219253"
				,"3272922127"
				,"534275677 "
				,"bruceliuzhen"
				,"3224211994"
				,"1568607772"
				,"3154810532"
				,"wltgxx"
				,"312914666"
				};
		WeiboUserMainPageTaskProducer.cleanAllTask(USER_MAIN_PAGE);
		for(String u : users){
			try {
				WeiboUserMainPageTaskProducer producer = new WeiboUserMainPageTaskProducer(USER_MAIN_PAGE,u,taskname);
				producer.setSleep(15000);
				producer.run();
			} catch (Exception e) {
				log.error("{} process error : ",u,e);
			}
		}
		
	}

	public WeiboUserMainPageTaskProducer(String taskQueueName, String user, String keyword) {
		super(taskQueueName);
		this.user = user;
		this.keyword = keyword;
	}

	@Override
	protected int getMaxPage() {
		String url = buildUrl(1);
		try {
			Document doc = WeiboFetcher.cnFetcher.fetch(url);

			if (doc.select("div#pagelist").size() == 0) {
				Elements elements = doc.select("div.c[id]");
				if(elements.isEmpty()){
					return 0;
				}
				return 1;
			} else {
				String html = doc.select("div#pagelist").first().text();
				String page = StringUtils.substringBetween(html, "/", "页");
				return Integer.parseInt(page);
			}
		} catch (Exception e) {
			log.error("{} get maxPage error : ", url, e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(USER_MAIN_PAGE_URL_TEMPLATE, user, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(USER_MAIN_PAGE);
		task.setUrl(url);
		task.setExtra(user);
		
		return task;
	}

}
