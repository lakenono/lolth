package lolth.pcbaby.bbs;

import java.text.MessageFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

public class PcBabyBBSSearchList extends Producer {

	private static final String PCBABY_BBS_URL = "http://ks.pcbaby.com.cn/kids_bbs.shtml?q={0}&menu&pageNo={1}";

	private String keyword;

	public PcBabyBBSSearchList(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "pcbaby_bbs_search_list";
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
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(PCBABY_BBS_URL, UrlUtils.encode(keyword,"gbk"), String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	public static void main(String[] args) {
		String projectName = "测试奶粉";
		String keyword = "多美滋致粹";
		
		try {
			new PcBabyBBSSearchList(projectName,keyword).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
