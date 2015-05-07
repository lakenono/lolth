package lolth.baidu.zhidao;

import lakenono.task.FetchTask;
import lolth.baidu.fetch.BaiduFetcher;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaiduZhidaoDetailFetchTest {
	private static BaiduZhidaoDetailFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new BaiduZhidaoDetailFetch();
	}

	@Test
	public void testZhidao() throws Exception {
		String url = "http://zhidao.baidu.com/question/1174802513797007179.html?fr=iks&word=%BB%DD%CA%CF%C6%F4%B8%B3+%B8%DB%B0%E6+%C4%DA%B5%D8&ie=gbk";
		Document doc = BaiduFetcher.fetcher.document(url);

		fetch.parsePage(doc, getTask(url));
	}

	@Test
	public void testZuoye() throws Exception {
		String url = "http://zuoye.baidu.com/question/a0a50de360400197bfbcc988881379b6.html?fr=iks&word=%BB%DD%CA%CF&ie=gbk";
		Document doc = BaiduFetcher.fetcher.document(url);

		fetch.parsePage(doc, getTask(url));
	}

	private FetchTask getTask(String url) {

		FetchTask task = new FetchTask();
		task.setName("task_case_惠氏");
		task.setUrl(url);
		task.setExtra("task_case_惠氏");
		return task;
	}

}
