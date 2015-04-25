package lolth.autohome.buy;

import lakenono.task.FetchTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class AutohomeBuyInfoListTaskFetchTest {
	private static AutohomeBuyInfoListTaskFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new AutohomeBuyInfoListTaskFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://jiage.autohome.com.cn/price/carlist/s-3335-0-0-0-0-0-1";

		FetchTask task = new FetchTask();
		task.setUrl(url);
		task.setExtra("3335");

		fetch.handleTask(task);
	}

}
