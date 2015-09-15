package dmp.ec.amazon;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AmazonSearchProducer extends Producer {

	// 模板url
	private final String templetUrl = "http://www.amazon.cn/s/page={0}&keywords={1}";
	// 关键字
	private String keyword;
	// 队列名称
	public static final String QUEUENAME = "ec_dmp_amazon";

	public AmazonSearchProducer(String projectName, String keyword) {
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

	@Override
	protected Task buildTask(String url) {
		Task task = super.buildTask(url);
//		task.setProjectName(this.projectName);
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
