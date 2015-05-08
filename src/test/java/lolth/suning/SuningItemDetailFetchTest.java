package lolth.suning;

import lakenono.task.FetchTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuningItemDetailFetchTest {
	public static SuningItemDetailFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new SuningItemDetailFetch();
	}

	@Test
	public void test() throws Exception{
		String url = "http://product.suning.com/103282849.html";
		
		FetchTask task = new FetchTask();
		task.setName("task_name_惠氏");
		task.setBatchName("task_name_惠氏");
		task.setUrl(url);
		task.setExtra("惠氏");
		
		
		fetch.handleTask(task);
	}

}
