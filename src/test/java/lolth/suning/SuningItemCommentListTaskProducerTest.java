package lolth.suning;

import lakenono.task.FetchTask;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuningItemCommentListTaskProducerTest {
	
	private static SuningItemCommentListTaskProducer producer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		producer = new SuningItemCommentListTaskProducer();
	}

	@Test
	public void test() throws Exception {
		String id = "127617538";
		FetchTask task = new FetchTask();
		
		task.setName("task_case_惠氏");
		task.setBatchName("task_case_惠氏");
		task.setUrl(id);
		task.setExtra(id);
		
		producer.handleTask(task);
	}

}
