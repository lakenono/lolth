package lolth.mamanet;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class MamaNetTest {
	
	private MamaNetSearchFetch fetch;
	@Before
	public void initBefore(){
		fetch = new MamaNetSearchFetch(MamaNetSearchListProducer.MAMANET_SEARCH_LIST);
	}
	

	@Test
	public void testFetch() throws Exception {
		String url = "http://zhannei.baidu.com/cse/search?q=%E6%83%A0%E6%B0%8F%E5%90%AF%E8%B5%8B&p=6&s=8134803871385444951&nsid=1";
		Document document = GlobalComponents.fetcher.document(url);
		FetchTask task = new FetchTask();
		task.setName("惠氏");
		task.setExtra("惠氏启赋");
		fetch.parsePage(document, task);
	}
}
