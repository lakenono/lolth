package dmp.ec.taobao;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECTmallProducer extends Producer {

	private static final int PAGE_SIZE = 84;

	private static final String TMALL_FRESH_URL = "http://list.tmall.com/search_product.htm?q={0}&s={1}&sort=s&style=l{2}";

	private String catString;

	private String keyword;

	public ECTmallProducer(String projectName, String keyword, String catString) {
		super(projectName);
		this.catString = catString;
		this.keyword = keyword;

	}

	@Override
	public String getQueueName() {
		return "data_dmp_tmall_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pages = doc.select("div.ui-page div.ui-page-wrap b.ui-page-skip form");
		if (pages.size() == 0) {
			log.error("taobao page is empty: " + pages);
			return 0;
		} else {
			String text = pages.first().ownText();
			text = StringUtils.substringBetween(text, "共", "页");
			if (StringUtils.isNumeric(text)) {
				log.info("taobao max page is " + text);
				return Integer.parseInt(text.trim());
			}
			log.error("taobao page is error: " + pages);
			return 0;
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(TMALL_FRESH_URL, UrlUtils.encode(this.keyword), String.valueOf((pageNum - 1) * PAGE_SIZE), this.catString);
	}

	public static void main(String[] args) {
		String projectName = "";
		String keyword = "";
		String catString = "&cat=55082013";
		try {
			new ECTmallProducer(projectName, keyword, catString).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
