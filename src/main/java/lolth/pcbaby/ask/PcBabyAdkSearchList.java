package lolth.pcbaby.ask;

import java.text.MessageFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

public class PcBabyAdkSearchList extends Producer {

	private static final String PCBABY_ASK_URL = "http://ks.pcbaby.com.cn/kids_kuaiwen.shtml?q={0}&pageNo={1}";

	private String keyword;

	public PcBabyAdkSearchList(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "pcbaby_ask_search_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements elements = doc.select("div.main > p > em.red");
		if (!elements.isEmpty()) {
			int page;
			String text = elements.first().text();
			int parseInt = Integer.parseInt(text);
			if (parseInt >= 50 * 15) {
				page = 50;
			} else {
				page = parseInt / 15;
				page = page + (parseInt % 15 != 0 ? 1 : 0);
			}
			return page;
		}
		return 0;
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(PCBABY_ASK_URL, UrlUtils.encode(keyword, "gbk"), String.valueOf(pageNum));
	}

}
