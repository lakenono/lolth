package lolth.xcar.bbs;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class XCarBBSPostDetailFetchTest {
	private XCarBBSPostDetailFetch fetch = null;
	@Before
	public void init(){
		fetch = new XCarBBSPostDetailFetch("test");
	}

	@Test
	public void test() throws Exception {
		String url = "http://www.xcar.com.cn/bbs/viewthread.php?tid=21310550";
		Document doc = GlobalComponents.fetcher.document(url);

		FetchTask task = new FetchTask();
		task.setName("chevrolet");
		task.setExtra("1234");
		task.setUrl(url);
		task.setExtra("extra");

		fetch.parsePage(doc, task);
	}

}
