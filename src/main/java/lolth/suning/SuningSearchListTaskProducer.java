package lolth.suning;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class SuningSearchListTaskProducer extends PagingFetchTaskProducer {

	public static final String SUNING_SEARCH_LIST = "suning_search_list";

	private static final String SUNING_SEARCH_LIST_URL_TEMPLATE = "http://search.suning.com/{0}/cp={1}";

	private String name;
	private String keyword;
	private String keywordEncode;

	public SuningSearchListTaskProducer(String name, String keyword) throws UnsupportedEncodingException {
		super(SUNING_SEARCH_LIST);
		this.name = name;
		this.keyword = keyword;
		this.keywordEncode = URLEncoder.encode(keyword, "utf-8");
	}

	public static void main(String[] args) throws Exception {
		String name = "惠氏";
		String[] keywords = { "惠氏" };

		for (String k : keywords) {
			log.info("{} start! ", k);
			SuningSearchListTaskProducer producer = new SuningSearchListTaskProducer(name, k);
			producer.run();
			log.info("{} fniish! ", k);
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			// 最后页
			Element lastPage = doc.getElementById("pageLast");
			if (lastPage != null) {
				String maxPage = lastPage.text();
				if (StringUtils.isNumeric(maxPage)) {
					return Integer.parseInt(maxPage);
				} else {
					return 0;
				}
			}

			// 1 页
			Elements items = doc.select("ul.container li.item");
			if (!items.isEmpty()) {
				return 1;
			}
		} catch (Exception e) {
			log.warn("Get max page fail!");
		}

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(SUNING_SEARCH_LIST_URL_TEMPLATE, keywordEncode, String.valueOf(pageNum - 1));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(name);
		task.setBatchName(SUNING_SEARCH_LIST);
		task.setUrl(url);
		task.setExtra(keyword);
		return task;
	}

}
