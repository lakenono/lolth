package lolth.suning;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SuningItemCommentListTaskProducer extends FetchTaskHandler {

	public SuningItemCommentListTaskProducer() {
		super(SuningSearchListFetch.SUNING_ITEM_COMMENT_MAXPAGE);
	}

	public static void main(String[] args) {
		SuningItemCommentListTaskProducer producer = new SuningItemCommentListTaskProducer();
		producer.setSleep(1000);
		producer.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		new Producer(task).run();
	}

	@Slf4j
	public static class Producer extends PagingFetchTaskProducer {

		public static final String SUNING_ITEM_COMMENT_LIST = "suning_item_comment_list";

		private static final String SUNING_ITEM_COMMENT_LIST_URL_TEMPLATE = "http://zone.suning.com/review/json/product_reviews/{0}--total-g-0---10-{1}-getItem.html";

		private FetchTask task = null;

		public Producer(FetchTask task) {
			super(SUNING_ITEM_COMMENT_LIST);
			this.task = task;
		}

		@Override
		protected int getMaxPage() {
			try {
				Document doc = GlobalComponents.fetcher.document(buildMaxPageUrl());

				Elements pageDivs = doc.select("div.snPages");
				if (pageDivs.isEmpty()) {
					return 0;
				}

				Element pageDiv = pageDivs.first();
				if (pageDiv.children().isEmpty()) {
					return 0;
				}

				Element maxPage = pageDiv.select(".next").first().previousElementSibling();
				if (maxPage != null) {
					String pageStr = maxPage.text();
					if (StringUtils.isNumeric(pageStr)) {
						int pages = Integer.parseInt(pageStr);
						if (pages > 50) {
							pages = 50;
						}
						return pages;
					}
				}
			} catch (Exception e) {
				log.warn("Get max page error : ", e);
			}
			return 0;
		}

		private String buildMaxPageUrl() {
			return MessageFormat.format("http://zone.suning.com/review/pro_review/{0}-0-1--.html", task.getExtra());
		}

		@Override
		protected String buildUrl(int pageNum) {
			String id = task.getExtra();
			id = StringUtils.leftPad(id, 18, '0');
			return MessageFormat.format(SUNING_ITEM_COMMENT_LIST_URL_TEMPLATE, id, String.valueOf(pageNum));
		}

		@Override
		protected FetchTask buildTask(String url) {
			task.setUrl(url);
			return task;
		}

	}
}
