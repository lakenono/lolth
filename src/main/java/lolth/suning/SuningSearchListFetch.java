package lolth.suning;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SuningSearchListFetch extends PageParseFetchTaskHandler {

	public static final String SUNING_ITEM_DETAIL = "suning_item_detail";

	public static final String SUNING_ITEM_COMMENT_MAXPAGE = "suning_item_comment_page";

	private FetchTaskProducer itemDetailTaskProducer = null;
	private FetchTaskProducer itemCommentListTaskProducer = null;

	public SuningSearchListFetch() {
		super(SuningSearchListTaskProducer.SUNING_SEARCH_LIST);

		itemDetailTaskProducer = new FetchTaskProducer(SUNING_ITEM_DETAIL);
		itemCommentListTaskProducer = new FetchTaskProducer(SUNING_ITEM_COMMENT_MAXPAGE);
	}

	public static void main(String[] args) {
		SuningSearchListFetch fetch = new SuningSearchListFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements itemAs = doc.select("ul.container li.item a.proName");
		for (Element item : itemAs) {
			String url = item.absUrl("href");
			String id = StringUtils.substringAfterLast(url, "/");
			id = StringUtils.substringBefore(id, ".");

			itemDetailTaskProducer.saveAndPushTask(buildDetailTask(url,task));

			itemCommentListTaskProducer.saveAndPushTask(buildCommnetTask(id, task));
		}
	}

	private FetchTask buildDetailTask(String url, FetchTask task) {
		FetchTask detailTask = new FetchTask();
		detailTask.setName(task.getName());
		detailTask.setBatchName(SUNING_ITEM_DETAIL);
		detailTask.setUrl(url);
		detailTask.setExtra(task.getExtra());
		return detailTask;
	}

	private FetchTask buildCommnetTask(String id, FetchTask task) {
		FetchTask commentTask = new FetchTask();
		commentTask.setName(task.getName());
		commentTask.setUrl(id);
		commentTask.setBatchName(SUNING_ITEM_COMMENT_MAXPAGE);
		commentTask.setExtra(id);
		return commentTask;
	}

}
