package lolth.babytree.bbs;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class BabytreeBBSSearchDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String BABYTREE_BBS_LIST_DETAIL = "babytree_bbs_list_detail";

	private FetchTaskProducer postDetailProducer = new FetchTaskProducer(BABYTREE_BBS_LIST_DETAIL);

	public BabytreeBBSSearchDetailTaskProducer(String taskQueueName) {
		super(taskQueueName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Set<String> detailUrls = new TreeSet<>();
		// 获得所有的detail链接地址
		Elements elements = doc.select("#babytree-body-wrapper > div.search_wrap > div.search_col_area.clearfix > div.search_col_2 > div.search_item_area > div.search_item > div.search_item_tit > a");

		for (Element element : elements) {
			String url = element.attr("href");
			detailUrls.add(url);
		}

		// 推送
		for (String url : detailUrls) {
			if (StringUtils.isBlank(url)) {
				continue;
			}
			task.setBatchName(BABYTREE_BBS_LIST_DETAIL);
			task.setUrl(url);
			postDetailProducer.saveAndPushTask(task);
			log.debug(task.toString());
		}

		//////////////
		detailUrls.clear();
		task=null;
	}

	

	@Override
	public void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		String url = task.getUrl();
		if (StringUtils.isBlank(url)) {
			return;
		}
		Document doc = GlobalComponents.fetcher.document(url);
		this.parsePage(doc, task);
	}

	public static void main(String[] args) {
		String taskQueueName = BabytreeBBSSearchList.BABYTREE_BBS_LIST;
		BabytreeBBSSearchDetailTaskProducer bbsSearchDetailTaskProducer = new BabytreeBBSSearchDetailTaskProducer(taskQueueName);

		bbsSearchDetailTaskProducer.setSleep(5000);
		bbsSearchDetailTaskProducer.run();
	}
}
