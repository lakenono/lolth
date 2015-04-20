package lolth.bitauto.k;

import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitautoWordOfMouthListTaskProducer extends PagingFetchTaskProducer {

	public static final String BITAUTO_K_LIST = "bitauto_k_list";

	private static final String BITAUTO_K_LIST_URL_TEMPLATE = "http://baa.bitauto.com/{0}/index-all-1-{1}-0.html";

	private String name;
	private String brandId;

	public static void main(String[] args) {
		String name = "chevrolet";

		/*
		 * 北京现代ix25 http://baa.bitauto.com/ix25/index-all-1-1-0.html 别克昂科拉
		 * http://baa.bitauto.com/angkela/index-all-1-1-0.html 福特翼搏
		 * http://baa.bitauto.com/yibo/index-all-1-1-0.html 标致2008
		 * http://baa.bitauto.com/2008/index-all-1-1-0.html 缤智
		 * http://baa.bitauto.com/vezel/index-all-1-1-0.html
		 */
		String[] brandIds = {"ix25","angkela","yibo","2008","vezel"};
		for(String id : brandIds){
			log.info("Handler http://baa.bitauto.com/{}/index-all-1-1-0.html Start ! ", id);
			BitautoWordOfMouthListTaskProducer producer = new BitautoWordOfMouthListTaskProducer(name,id);
			log.info("Handler http://baa.bitauto.com/{}/index-all-1-1-0.html Finish ! ", id);
			producer.setSleep(1000);
			producer.run();
		}
		
	}

	public BitautoWordOfMouthListTaskProducer(String name, String brandId) {
		super(BITAUTO_K_LIST);
		this.name = name;
		this.brandId = brandId;
	}

	@Override
	protected int getMaxPage() {
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(BITAUTO_K_LIST_URL_TEMPLATE, brandId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(name);
		task.setBatchName(BITAUTO_K_LIST);
		task.setUrl(url);
		task.setExtra(brandId);
		return task;
	}
}
