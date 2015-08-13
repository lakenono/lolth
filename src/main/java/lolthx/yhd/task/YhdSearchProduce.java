package lolthx.yhd.task;

import java.text.MessageFormat;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 一号店搜索爬取任务生成 一个页面两个请求，特殊produce
 * 
 * @author yanghp
 *
 */
@Slf4j
public class YhdSearchProduce {

	private final String BASE_URL = "http://search.yhd.com/c0-0-0/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k{1}/";
	private final String BASE_URL1 = "http://search.yhd.com/searchPage/c0-0-0/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k{1}/?isLargeImg=0";
	private final String BASE_URL2 = "http://search.yhd.com/searchPage/c0-0-0/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k{1}/?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";
	private String projectName;
	private String keyword;
	public static final String QUEUENAME = "yhd_search_list";

	public YhdSearchProduce(String projectName,String keyword) {
		this.projectName = projectName;
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "ceshi";
		String[] keywords = { "洗发露", "酸奶" };
		for (String keyword : keywords) {
			YhdSearchProduce a = new YhdSearchProduce(projectName,keyword);
			a.run();
		}

	}

	private void run() throws Exception {
		log.info("{} yi hao dian Producer start ...", this.projectName);
		int pagenum = this.parse();
		log.info("{} Get max page : {}", this.projectName, pagenum);

		// 发送第两个任务
		for (int i = 1; i <= pagenum; i++) {
			// 创建url
			String url1 = buildUrl(i, BASE_URL1);
			String url2 = buildUrl(i, BASE_URL2);

			// 创建抓取任务
			Task task1 = buildTask(url1);
			Task task2 = buildTask(url2);
			// 推送任务
			Queue.push(task1);
			Queue.push(task2);
		}

	}


	private int parse() throws Exception {
		// 获取最大页数
		Document doc = GlobalComponents.fetcher.document(MessageFormat.format(
				BASE_URL, 1, UrlUtils.encode(projectName)));
		Elements elements = doc.select("a#lastPage");
		if (!elements.isEmpty()) {
			String page = elements.first().text();
			return Integer.parseInt(page);
		}

		return 0;
	}

	private String buildUrl(int pageNum, String url) throws Exception {
		return MessageFormat.format(url, pageNum, UrlUtils.encode(projectName));
	}

	private Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(QUEUENAME);
		task.setUrl(url);
		task.setExtra(this.keyword);

		return task;
	}

}
