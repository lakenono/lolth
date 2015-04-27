package lolth.pacuto.bbs;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class PacutoBBSDetailTaskProducerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() throws Exception {
		FetchTask task = new FetchTask();
		//task.setExtra("20095");

		String url = "http://bbs.pcauto.com.cn/forum-20095.html";
		Document doc = GlobalComponents.fetcher.document(url);

		PacutoBBSDetailTaskProducer producer = new PacutoBBSDetailTaskProducer();
		producer.parsePage(doc, task);
	}

}
