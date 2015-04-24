package lolth.pacuto.k;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class PacutoWordOfMouthListTaskFetchTest {

	private static PacutoWordOfMouthListTaskFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new PacutoWordOfMouthListTaskFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://price.pcauto.com.cn/comment/sg10400/";

		FetchTask task = new FetchTask();
		task.setName("test");
		task.setBatchName("testBatchName");
		task.setUrl(url);
		task.setExtra("sg10400");

		Document doc = GlobalComponents.fetcher.document(url);

		fetch.parsePage(doc, task);
	}

}
