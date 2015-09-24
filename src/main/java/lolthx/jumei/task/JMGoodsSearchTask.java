package lolthx.jumei.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

public class JMGoodsSearchTask extends Producer {
	private String keyword;
	public static final String QUEUENAME = "jm_goods_search";
	private String base_url = "http://search.jumei.com/?filter=0-11-{0}&search={1}&site=bj";

	public JMGoodsSearchTask(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return QUEUENAME;
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		String pages = doc.select("div.head_pageInfo").text();
		String nums = StringUtils.substringBetween(pages, "/", "页");
		if (StringUtils.isBlank(nums)) {
			return 0;
		} else {
			return Integer.parseInt(nums);
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(base_url, String.valueOf(pageNum), UrlUtils.encode(keyword));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) {
		new JMGoodsSearchTask("cehi", "面膜").run();
	}
}
