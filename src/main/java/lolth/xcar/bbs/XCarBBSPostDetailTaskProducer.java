package lolth.xcar.bbs;

import java.util.Set;
import java.util.TreeSet;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 解析详情页的url推送详情页解析
 * @author shi.lei
 *
 */
public class XCarBBSPostDetailTaskProducer extends PageParseFetchTaskHandler {
	public static final String XCAR_BBS_POST_DETAIL = "xcar_bbs_post_detail";

	private FetchTaskProducer postDetailProducer = new FetchTaskProducer(XCAR_BBS_POST_DETAIL);

	public static void main(String[] args) {
		String listQueue = XCarBBSPostListTaskProducer.XCAR_BBS_POST_LIST;

		XCarBBSPostDetailTaskProducer fetchProducer = new XCarBBSPostDetailTaskProducer(listQueue);
		fetchProducer.setSleep(1000);
		fetchProducer.run();

	}

	public XCarBBSPostDetailTaskProducer(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Set<String> detailUrls = new TreeSet<>();

		// 获得所有的detail链接地址
		Elements elements = doc.select("#F_box_1 table.row");

		for (Element element : elements) {
			String url = element.select("a.open_view").first().absUrl("href");
			detailUrls.add(url);
		}

		// 推送
		for (String url : detailUrls) {
			postDetailProducer.saveAndPushTask(buildTask(url, task));
		}
	}

	private FetchTask buildTask(String url, FetchTask oldListTask) {
		if (StringUtils.isBlank(url)) {
			return null;
		}

		FetchTask listTask = oldListTask.clone();
		listTask.setBatchName(XCAR_BBS_POST_DETAIL);
		listTask.setUrl(url);
		return listTask;
	}

}
