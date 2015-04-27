package lolth.pacuto.bbs;

import lakenono.task.FetchTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class PacutoBBSDetailTaskFetchTest {

	private static PacutoBBSDetailTaskFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@Test
	public void test() throws Exception {

		fetch = new PacutoBBSDetailTaskFetch();

		String url = "http://bbs.pcauto.com.cn/topic-7019494.html";

		FetchTask task = new FetchTask();
		task.setUrl(url);
		task.setExtra("trax,img");

//		Document doc = GlobalComponents.fetcher.document(url);

		fetch.handleTask(task);
	}

}
