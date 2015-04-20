package lolth.bitauto.k;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BitautoWordOfMouthDetailTaskProducerTest {

	private BitautoWordOfMouthDetailTaskProducer producer;

	@BeforeClass
	public void init() {
		producer = new BitautoWordOfMouthDetailTaskProducer();
	}

	@Test
	public void test() throws Exception {
		String url = "http://baa.bitauto.com/ix25/index-all-1-1-0.html";

		FetchTask task = new FetchTask();
		task.setBatchName("chevrolet");
		task.setUrl(url);
		task.setExtra("ix25");

		Document doc = GlobalComponents.fetcher.document(url);

		producer.parsePage(doc, task);
	}
}
