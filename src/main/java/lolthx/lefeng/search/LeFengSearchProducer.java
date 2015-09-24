package lolthx.lefeng.search;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class LeFengSearchProducer extends Producer {
	private String lefeng_search_url = "http://search.lefeng.com/search/search?key={0}&wt.s_pg=Isearch&wt.s_pf=public&pageNo={1}#clothList";

	private String keyword;

	public LeFengSearchProducer(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "lefeng_item_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pages = document.select("div.pages.ClearFix > span > a");
		int page = 0;
		if (pages.isEmpty()) {
			Elements divs = document.select("div.pruwrap");
			if (divs.isEmpty()) {
				log.info("lefeng max page is empty : " + pages);
			} else {
				log.info("lefeng max page is : 1");
				page = 1;
			}
		} else {
			String pageStr = pages.get(pages.size() - 2).text();
			if (StringUtils.isNumeric(pageStr)) {
				log.info("lefeng max page is : " + pageStr);
				page = Integer.parseInt(pageStr);
			} else {
				log.info("lefeng max page is error : " + pages);
			}
		}
		return page;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(lefeng_search_url, UrlUtils.encode(keyword), String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

}
