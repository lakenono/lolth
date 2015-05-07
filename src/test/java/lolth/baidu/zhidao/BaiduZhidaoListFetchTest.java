package lolth.baidu.zhidao;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaiduZhidaoListFetchTest {

	private static BaiduZhidaoListFetch fetch;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fetch = new BaiduZhidaoListFetch();
	}

	@Test
	public void test() throws Exception {
		String url = "http://zhidao.baidu.com/search?lm=0&rn=10&pn=0&fr=search&ie=gbk&word=%BB%DD%CA%CF";

		String name = "惠氏";
		String keyword = "惠氏";

		FetchTask fetchTask = new FetchTask();
		fetchTask.setName("test_case_" + name);
		fetchTask.setUrl(url);
		fetchTask.setBatchName("test_case_" + keyword);

		Document doc = GlobalComponents.fetcher.document(url);
		fetch.parsePage(doc, fetchTask);
	}

}
