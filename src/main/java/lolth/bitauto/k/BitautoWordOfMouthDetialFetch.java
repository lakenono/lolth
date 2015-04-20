package lolth.bitauto.k;

import org.jsoup.nodes.Document;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;

public class BitautoWordOfMouthDetialFetch extends PageParseFetchTaskHandler {

	public BitautoWordOfMouthDetialFetch() {
		super(BitautoWordOfMouthDetailTaskProducer.BITAUTO_K_DETAIL);
	}

	public static void main(String[] args) {
		BitautoWordOfMouthDetialFetch fetch = new BitautoWordOfMouthDetialFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {

	}

}
