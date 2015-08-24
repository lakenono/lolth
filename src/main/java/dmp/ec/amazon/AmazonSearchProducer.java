package dmp.ec.amazon;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AmazonSearchProducer extends Producer {

	// 模板url
	private final String templetUrl = "http://www.amazon.cn/s/page={0}&keywords={1}";
	// 项目名称
	private String projectName;
	// 关键字
	private String keyword;
	// 队列名称
	public static final String QUEUENAME = "amazon_search_list";

	public AmazonSearchProducer(String projectName, String keyword) {
		// this.projectName = projectName;
		super(projectName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "ceshi";
		String[] keywords = { "手机壳iphone6" };
		for (String keyword : keywords) {
			AmazonSearchProducer gouzao = new AmazonSearchProducer(projectName, keyword);
			gouzao.run();
		}
	}

	public void run() throws Exception {
		int maxpage = this.parse();

		for (int i = 1; i <= maxpage; i++) {
			String url = MessageFormat.format(templetUrl, i, UrlUtils.encode(keyword));
			Task task1 = buildTask(url);
			Queue.push(task1);
		}

	}

	protected Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(QUEUENAME);
		task.setUrl(url);
		task.setExtra(this.keyword);

		return task;
	}

	@Override
	public String getQueueName() {
		return QUEUENAME;
	}

	@Override
	protected int parse() throws Exception {
		// 获取最大页数
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements elements = doc.select("span.pagnDisabled");
		if (!elements.isEmpty()) {
			String page = elements.first().text();
			return Integer.parseInt(page);
		}

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(templetUrl, pageNum, UrlUtils.encode(keyword));
	}

}
