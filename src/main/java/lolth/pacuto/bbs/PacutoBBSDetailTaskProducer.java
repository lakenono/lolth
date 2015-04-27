package lolth.pacuto.bbs;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 解析列表页，获取详情页URL及类型，生成TASK
 * 
 * @author shi.lei
 *
 */
public class PacutoBBSDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String PACUTO_BBS_POST_DETAIL = "pacuto_bbs_post_detail";
	private FetchTaskProducer postDetailProducer;

	public PacutoBBSDetailTaskProducer() {
		super(PacutoBBSListTaskProducer.PACUTO_BBS_POST_LIST);
		postDetailProducer = new FetchTaskProducer(PACUTO_BBS_POST_DETAIL);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PacutoBBSDetailTaskProducer producer = new PacutoBBSDetailTaskProducer();
		producer.setSleep(1000);
		producer.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		// TODO Auto-generated method stub
		Elements items = doc.select("td.folder a");

		for (Element item : items) {
			// url
			String url = "";
			url = item.absUrl("href");
			// type
			String type = "";
			type = item.attr("title");

			FetchTask newTask = buildTask(task, url, type);
			postDetailProducer.saveAndPushTask(newTask);

		}

	}

	protected FetchTask buildTask(FetchTask task, String url, String type) {

		FetchTask detailTask = new FetchTask();
		detailTask.setName(task.getName());
		detailTask.setBatchName(PACUTO_BBS_POST_DETAIL);
		detailTask.setUrl(url);
		detailTask.setExtra(task.getExtra() + "," + type);
		return detailTask;
	}

}
