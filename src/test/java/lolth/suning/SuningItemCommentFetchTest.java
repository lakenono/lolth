package lolth.suning;

import lakenono.task.FetchTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuningItemCommentFetchTest {
	private static SuningItemCommentFetch fetch ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new SuningItemCommentFetch();
	}

	@Test
	public void test() throws Exception{
		String url = "http://zone.suning.com/review/json/product_reviews/000000000124078627--total-g-0---10-1-getItem.html";
		
		FetchTask task = new FetchTask();
		task.setName("task_case_惠氏");
		task.setBatchName("task_case_惠氏");
		task.setUrl( url);
		task.setExtra("124078627");
		
		
		fetch.handleTask(task);
	}

}
