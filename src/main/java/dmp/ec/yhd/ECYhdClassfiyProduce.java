package dmp.ec.yhd;

import java.text.MessageFormat;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
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
public class ECYhdClassfiyProduce {

	private final String BASE_URL = "http://list.yhd.com/{0}/";
	private final String BASE_URL1 = "http://list.yhd.com/searchPage/{0}/b/a-s1-v4-p{1}-price-d0-f0d-m1-rt0-pid-mid0-k/?isLargeImg=0";
	private final String BASE_URL2 = "http://list.yhd.com/searchPage/{0}/b/a-s1-v4-p{1}-price-d0-f0d-m1-rt0-pid-mid0-k/?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";
	private String projectName;
	//电商频道
	private String classfiy;
	public static String EC_CLASSFIY_QUEUE = "ec_yhd_classfiy_list";
	

	public ECYhdClassfiyProduce(String projectName,String classfiy) {
		this.projectName = projectName;
		this.classfiy = classfiy;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "ceshi";
		String[] keywords = { "c5473-0-81624" };
		for (String keyword : keywords) {
			ECYhdClassfiyProduce a = new ECYhdClassfiyProduce(projectName,keyword);
			a.run();
		}

	}

	public void run() throws Exception {
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
				BASE_URL, classfiy));
		Elements elements = doc.select("a#lastPage");
		if (!elements.isEmpty()) {
			String page = elements.first().text();
			return Integer.parseInt(page);
		}

		return 0;
	}

	private String buildUrl(int pageNum, String url) throws Exception {
		return MessageFormat.format(url,this.classfiy,pageNum);
	}

	private Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(EC_CLASSFIY_QUEUE);
		task.setUrl(url);
		task.setExtra(this.classfiy);

		return task;
	}

}
