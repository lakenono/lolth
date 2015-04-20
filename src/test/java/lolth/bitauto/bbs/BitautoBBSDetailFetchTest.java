package lolth.bitauto.bbs;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BitautoBBSDetailFetchTest {
	private static BitautoBBSDetailFetch fetch;

	@BeforeClass
	public static void init() {
		fetch = new BitautoBBSDetailFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://baa.bitauto.com/trax/thread-6681711.html";

		FetchTask task = new FetchTask();
		task.setUrl(url);
		task.setExtra("trax");

		Document doc = GlobalComponents.fetcher.document(url);

		fetch.parsePage(doc, task);
	}

}
