package lolth.bitauto.k;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

import org.jsoup.nodes.Document;

public class BitautoWordOfMouthDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String BITAUTO_K_DETAIL = "bitauto_k_detail";

	private FetchTaskProducer producer;

	public BitautoWordOfMouthDetailTaskProducer() {
		super(BitautoWordOfMouthListTaskProducer.BITAUTO_K_LIST);
		producer = new FetchTaskProducer(BITAUTO_K_DETAIL);
	}

	public static void main(String[] args) {
		BitautoWordOfMouthDetailTaskProducer producer = new BitautoWordOfMouthDetailTaskProducer();
		producer.setSleep(1000);
		producer.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {

	}

}
