package lolthx.taobao.tmall.search;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TmallSearchListProducer extends Producer {

	private static final int PAGE_SIZE = 84;
	private static final String TMALL_SEARCH_URL = "http://list.tmall.com/search_product.htm?s={0}&q={1}&sort=s&style=l";
	
	private String keyword;
	public TmallSearchListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "tmall_item_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pages = document.select("div.ui-page div.ui-page-wrap b.ui-page-skip form");
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
		int start = (pageNum-1) * PAGE_SIZE;
		return MessageFormat.format(TMALL_SEARCH_URL, start + "", UrlUtils.encode(keyword,"GBK"));
	}
	
	public static void main(String[] args) {
		String projectName = "淘宝";
		String keyword  = "汽车";
		try {
			new TmallSearchListProducer(projectName,keyword).run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
