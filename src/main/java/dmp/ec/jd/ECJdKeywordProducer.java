package dmp.ec.jd;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECJdKeywordProducer extends Producer {

	private String search_url = "http://search.jd.com/Search?keyword={0}&enc=utf-8&qrst=1&rt=1&stop=1{1}";

	private String page_url = "http://search.jd.com/Search?keyword={0}&enc=utf-8&qrst=1&rt=1&stop=1&vt=2{1}&psort=&page={2}";

	private String page_end_url = "http://search.jd.com/s.php?keyword={0}&enc=utf-8&qrst=1&rt=1&stop=1&vt=2{1}&psort=&page={2}&scrolling=y&start=30";

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	private String catString;
	private String keyword;

	public ECJdKeywordProducer(String projectName, String keyword, String catString) {
		super(projectName);
		this.catString = catString;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "ec_dmp_jd_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(getPageUrl());
		Elements elements = doc.select("#pagin-btm > span.page-skip > em:nth-child(1)");
		if (elements.isEmpty()) {
			log.error("jd keyword page is empty:" + elements);
			return 0;
		} else {
			String pageStr = elements.text();
			Matcher matcher = pattern.matcher(pageStr);
			pageStr = matcher.replaceAll("");
			if (!StringUtils.isNumeric(pageStr)) {
				log.error("jd keyword page is error:" + elements);
				return 0;
			}
			log.info("jd keyword max page:" + pageStr);
			return Integer.parseInt(pageStr);
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String url = "";
		if (pageNum % 2 == 0) {
			url = page_end_url;
		} else {
			url = page_url;
		}
		return MessageFormat.format(url, UrlUtils.encode(this.keyword, "utf-8"), this.catString, pageNum);
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra("0");
		return buildTask;
	}

	private String getPageUrl() {
		return MessageFormat.format(search_url, UrlUtils.encode(this.keyword, "utf-8"), this.catString);
	}

	public static void main(String[] args) {
		String projectName = "瑜伽垫";
		String keyword = "";
		String catString = "&cid3=6266&click=3-6266";
		try {
			new ECJdKeywordProducer(projectName, keyword, catString).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
