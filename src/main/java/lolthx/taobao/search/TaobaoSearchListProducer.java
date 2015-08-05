package lolthx.taobao.search;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TaobaoSearchListProducer extends Producer{
	
	private static final String TAOBAO_SEARCH_URL = "https://s.taobao.com/search?q={0}&js=1&stats_click=search_radio_all%3A1&ie=utf8&bcoffset=1&s={1}&tab={2}";
	private int count = 44;
	private String tab = "all";
	
	private String keyword;
	
	public TaobaoSearchListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "taobao_item_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.dynamicFetch.document(buildUrl(1));
		Elements pages = document.select("div#mainsrp-pager div.total");
		if (pages.size() == 0) {
			return 0;
		} else {
			String text = pages.first().ownText();
			text = StringUtils.substringBetween(text, "共", "页");
			return Integer.parseInt(text.trim());
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		int size = count*(pageNum-1);
		return MessageFormat.format(TAOBAO_SEARCH_URL, UrlUtils.encode(keyword), String.valueOf(size),tab);
	}
	
	
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) {
		String projectName = "淘宝";
		String keyword  = "汽车";
		try {
			new TaobaoSearchListProducer(projectName, keyword).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
