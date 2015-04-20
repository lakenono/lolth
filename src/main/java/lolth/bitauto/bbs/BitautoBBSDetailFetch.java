package lolth.bitauto.bbs;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;

import org.jsoup.nodes.Document;

public class BitautoBBSDetailFetch extends PageParseFetchTaskHandler {

	public BitautoBBSDetailFetch() {
		super(BitautoBBSDetailTaskProducer.BITAUTO_BBS_POST_DETAIL);
	}

	public static void main(String[] args) {
		BitautoBBSDetailFetch fetch = new BitautoBBSDetailFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {

	}

}
