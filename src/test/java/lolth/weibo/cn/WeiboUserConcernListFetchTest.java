package lolth.weibo.cn;

import lakenono.task.FetchTask;
import lolth.weibo.fetcher.WeiboFetcher;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class WeiboUserConcernListFetchTest {
	private static WeiboUserConcernListFetch fetch ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new WeiboUserConcernListFetch();
	}

	@Test
	public void test() throws Exception{
		String url = "http://weibo.cn/1784740817/follow?vt=4";
		
		Document doc = WeiboFetcher.cnFetcher.fetch(url);
		
		FetchTask task = new  FetchTask();
		task.setName("test");
		task.setBatchName("test");
		task.setUrl(url);
		task.setExtra("1784740817");
		
		fetch.parsePage(doc, task);
	}

}
