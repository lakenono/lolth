package lolth.baidu.zhidao;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaiduZhidaoUserFetchTest {
	private static BaiduZhidaoUserFetch fetch ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new BaiduZhidaoUserFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://www.baidu.com/p/%E5%86%B0%E4%B9%8B_%E6%97%A0%E9%99%90/detail";
		
		FetchTask  task = new FetchTask();
		task.setName("test_case_惠氏");
		task.setBatchName("test_case_惠氏");
		task.setUrl(url);
		task.setExtra("陈陈囧贞子");
		
		Document doc = GlobalComponents.fetcher.document(url);
		fetch.parsePage(doc, task);;
	}

}
