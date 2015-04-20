package lolth.bitauto.k;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BitautoWordOfMouthDetialFetchTest {
	private static BitautoWordOfMouthDetialFetch fetch;

	@BeforeClass
	public static void init() {
		fetch = new BitautoWordOfMouthDetialFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://baa.bitauto.com/ix25/koubei-7047813.html";

		FetchTask task = new FetchTask();
		task.setUrl(url);
		task.setExtra("ix25");

		Document doc = GlobalComponents.fetcher.document(url);

		fetch.parsePage(doc, task);
		;
	}

}
