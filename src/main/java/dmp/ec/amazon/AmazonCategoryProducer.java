package dmp.ec.amazon;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AmazonCategoryProducer extends Producer {

	// 模板url
	private final String templetUrl = "http://www.amazon.cn/b/?ie=UTF8&page={0}&node={1}";
	// 关键字
	private String node;
	// 项目名称
	private String projectName;
	// 队列名称
	public static final String QUEUENAME = "amazon_search_list";

	public AmazonCategoryProducer(String projectName, String node) {
		super(projectName);
		this.node = node;
	}

	public static void main(String[] args) throws Exception {

		String projectName = "ceshi";
		String[] nodes = { "665194051" };
		for (String node : nodes) {
			AmazonCategoryProducer gouzao = new AmazonCategoryProducer(projectName, node);
			gouzao.run();

		}
	}

	public void run() throws Exception {
		int maxpage = this.parse();

		for (int i = 1; i <= maxpage; i++) {
			String url = MessageFormat.format(templetUrl, i, node);
			Task task1 = buildTask(url);
			Queue.push(task1);
		}
	}

	protected Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(QUEUENAME);
		task.setUrl(url);
		task.setExtra(this.node);

		return task;
	}

	@Override
	public String getQueueName() {
		return QUEUENAME;
	}

	@Override
	protected int parse() throws Exception {
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
		return MessageFormat.format(templetUrl, 1, node);
	}

}
