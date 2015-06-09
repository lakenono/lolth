package lolth.jd.search;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

public class JDCommoditySearchList extends Producer {

	private static final String JD_SEARCH_COMMODITY_URL = "http://search.jd.com/s.php?keyword={0}&enc=utf-8&qrst=1&rt=1&click=&psort=&page={1}&scrolling=y&start=30&log_id=1433752254.90829&tpl=1_M&vt=2";

	private String keyword;

	public JDCommoditySearchList(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "jd_search_commodity_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(getPageUrl());
		Elements elements = doc.select("#res_count");
		if (elements.isEmpty()) {
			return 0;
		} else {
			String pageStr = elements.text();
			if (!StringUtils.isNumeric(pageStr)) {
				return 0;
			}
			int count = Integer.parseInt(pageStr);
			int page = count / 30;
			page = page + (count % 15 != 0 ? 1 : 0);
			return page;
		}
	}

	private String getPageUrl() {
		String url = "http://search.jd.com/s.php?keyword={0}&enc=utf-8&qrst=1&rt=1&cid2=1585&click=2-1585&cs=y&vt=2";
		return MessageFormat.format(url, UrlUtils.encode(keyword, "utf-8"));
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(JD_SEARCH_COMMODITY_URL, UrlUtils.encode(keyword, "utf-8"), String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) {
		String projectName = "威仕高";
		String[] keywords = { "金典奶", "特仑苏", "安佳" };
		for (String keyword : keywords) {
			try {
				new JDCommoditySearchList(projectName, keyword).run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
