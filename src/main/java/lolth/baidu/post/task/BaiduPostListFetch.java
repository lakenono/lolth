package lolth.baidu.post.task;

import java.util.LinkedHashSet;
import java.util.Set;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 百度贴吧列表页抓取
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class BaiduPostListFetch extends PageParseFetchTaskHandler {
	private static final String BAIDU_POST_DETAIL_URL_PREFIX = "http://tieba.baidu.com";

	public static final String BAIDU_POST_DETAIL = "baidu_post_detail";

	// 任务生成器
	private FetchTaskProducer taskProducer = null;

	public BaiduPostListFetch(String queueName) {
		super(queueName);
		taskProducer = new FetchTaskProducer(BAIDU_POST_DETAIL);
	}

	public static void main(String[] args) throws Exception {
		//找到生产者提交的队列
		String taskQueueName = BaiduPostListTaskProducer.BAIDU_POST_LIST;
		
		//执行抓取
		new BaiduPostListFetch(taskQueueName).run();

	}

	@Override
	protected void parsePage(Document doc, FetchTask task) {
		Set<String> detailUrls = getDetailUrls(doc);
		pushDetailPageTask(detailUrls, task);
	}

	//解析页面获得所有详情页链接
	private Set<String> getDetailUrls(Document doc) {
		// 获取所有的详情页链接
		Set<String> detailUrls = new LinkedHashSet<>();

		Elements topicElements = doc.select("a.th_tit");
		if (topicElements.size() > 0) {
			for (Element href : topicElements) {
				detailUrls.add(href.attr("href"));
			}
		}

		Elements hrefElements = doc.select("a.j_th_tit");
		if (hrefElements.size() > 0) {
			for (Element href : hrefElements) {
				detailUrls.add(BAIDU_POST_DETAIL_URL_PREFIX + href.attr("href"));
			}
		}
		return detailUrls;
	}

	//创建详情页任务
	private void pushDetailPageTask(Set<String> detailUrls, FetchTask task) {
		for (String url : detailUrls) {
			try {
				FetchTask detailPageTask = new FetchTask();
				detailPageTask.setName(task.getName());
				detailPageTask.setBatchName(BAIDU_POST_DETAIL);
				detailPageTask.setUrl(url);

				taskProducer.saveAndPushTask(detailPageTask);
			} catch (Exception e) {
				log.error("{} build task error", url, e);
			}
		}
	}

}
