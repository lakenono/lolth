package lolth.bitauto.bbs;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BitautoBBSDetailTaskProducerTest {
	private static BitautoBBSDetailTaskProducer producer;

	@BeforeClass
	public static void init() {
		producer = new BitautoBBSDetailTaskProducer();
	}

	@Test
	public void test() throws Exception {
		String url = "http://baa.bitauto.com/trax/index-all-all-1-0.html";

		FetchTask task = new FetchTask();
		task.setExtra("trax");

		Document doc = GlobalComponents.fetcher.document(url);

		producer.parsePage(doc, task);
	}

}
