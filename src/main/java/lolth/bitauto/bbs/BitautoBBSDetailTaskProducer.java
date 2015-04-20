package lolth.bitauto.bbs;

import org.jsoup.nodes.Document;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

/**
 * 易车网详情页url解析，推送
 * @author shi.lei
 *
 */
public class BitautoBBSDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String BITAUTO_BBS_POST_DETAIL = "bitauto_bbs_post_detail";

	private FetchTaskProducer producer;

	public BitautoBBSDetailTaskProducer() {
		super(BitautoBBSListTaskProducer.BITAUTO_BBS_POST_LIST);
		producer = new FetchTaskProducer(BITAUTO_BBS_POST_DETAIL);
	}

	public static void main(String[] args) {
		BitautoBBSDetailTaskProducer producer = new BitautoBBSDetailTaskProducer();
		producer.setSleep(1000);
		producer.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {

	}

}
