package dmp.ec.taobao;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ECTaobaoProducer extends Producer {

	private int count = 44;
	private String tab = "mall";
	private static final String TAOBAO_SEARCH_URL = "https://s.taobao.com/search?q={0}&js=1&stats_click=search_radio_all%3A1&ie=utf8&cps=yes&bcoffset=1&s={1}&tab={2}{3}";

	private String catString;
	private String keyword;

	public ECTaobaoProducer(String projectName, String keyword, String catString) {
		super(projectName);
		this.catString = catString;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "data_dmp_taobao_list";
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
		int size = count * (pageNum - 1);
		return MessageFormat.format(TAOBAO_SEARCH_URL, UrlUtils.encode(this.keyword), String.valueOf(size), tab, catString);
	}

	public static void main(String[] args) {
		String projectName = "";
		String keyword = "";
		String catString = "&cat=50008055";
		try {
			new ECTaobaoProducer(projectName, keyword, catString).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
