package lolth.suning;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuningSearchListFetchTest {
	private static SuningSearchListFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new SuningSearchListFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://search.suning.com/%E6%83%A0%E6%B0%8F/cp=1";

		FetchTask task = new FetchTask();
		task.setName("test_case_惠氏");
		task.setBatchName("test_case_惠氏");
		task.setUrl(url);
		task.setExtra("惠氏");

		Document doc = GlobalComponents.fetcher.document(url);

		fetch.parsePage(doc, task);
	}

}
