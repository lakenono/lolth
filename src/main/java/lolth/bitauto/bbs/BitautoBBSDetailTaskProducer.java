package lolth.bitauto.bbs;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 易车网详情页url解析，推送
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class BitautoBBSDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String BITAUTO_BBS_POST_DETAIL = "bitauto_bbs_post_detail";
	public static final String BITAUTO_K_POST_DETAIL = "bitauto_K_post_detail";

	private FetchTaskProducer postDetailProducer;
	private FetchTaskProducer koubeiDetailProducer;

	private FetchTaskProducer producer = null;

	public BitautoBBSDetailTaskProducer() {
		super(BitautoBBSListTaskProducer.BITAUTO_BBS_POST_LIST);
		postDetailProducer = new FetchTaskProducer(BITAUTO_BBS_POST_DETAIL);
		koubeiDetailProducer = new FetchTaskProducer(BITAUTO_K_POST_DETAIL);
	}

	public static void main(String[] args) {
		BitautoBBSDetailTaskProducer producer = new BitautoBBSDetailTaskProducer();
		producer.setSleep(1000);
		producer.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements items = doc.select("div.postscontent>div.postslist_xh");
		for (Element item : items) {
			String batchName = null;
			String type = "";
			String url = "";
			// 类型
			Elements typeEs = item.select("li.tu a");
			if (!typeEs.isEmpty()) {

				type = typeEs.first().attr("class");
			}
			// 地址
			Elements urlEs = item.select("li.bt a");
			if (!urlEs.isEmpty()) {
				url = urlEs.first().absUrl("href");
			}

			log.debug("Parse result type={},url={}", type, url);

			if (!"kb_ping".equals(type)) {
				// 其他类型
				producer = postDetailProducer;
				batchName = BITAUTO_BBS_POST_DETAIL;
			} else {
				// 口碑类型
				producer = koubeiDetailProducer;
				batchName = BITAUTO_K_POST_DETAIL;
			}

			FetchTask newTask = buildTask(task, batchName, url, type);
			producer.saveAndPushTask(newTask);
		}
	}

	protected FetchTask buildTask(FetchTask task, String batchName, String url, String type) {
		FetchTask detailTask = new FetchTask();
		detailTask.setName(task.getName());
		detailTask.setBatchName(batchName);
		detailTask.setUrl(url);
		detailTask.setExtra(task.getExtra() + "," + type);
		return detailTask;
	}

}
