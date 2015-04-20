package lolth.bitauto.bbs;

import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitautoBBSListTaskProducer extends PagingFetchTaskProducer {
	public static final String BITAUTO_BBS_POST_LIST = "bitauto_bbs_post_list";

	private static final String BITAUTO_BBS_POST_LIST_URL_TEMPLATE = "http://baa.bitauto.com/{0}/index-all-all-{1}-0.html";

	private String name;

	private String forumId;

	public static void main(String[] args) {
		String name = "chevrolet";
		/*
		 * 创酷 http://baa.bitauto.com/trax/ 北京现代ix25 http://baa.bitauto.com/ix25/
		 * 别克昂科拉 http://baa.bitauto.com/angkela/ 福特翼搏
		 * http://baa.bitauto.com/yibo/ 标致2008 http://baa.bitauto.com/2008/ 缤智
		 * http://baa.bitauto.com/vezel/
		 */
		String[] forumIds = { "trax", "ix25", "angkela", "yibo", "2008", "vezel" };

		for (String id : forumIds) {
			log.info("Handler http://baa.bitauto.com/{}/ Start ! ", id);
			BitautoBBSListTaskProducer producer = new BitautoBBSListTaskProducer(name, id);
			log.info("Handler http://baa.bitauto.com/{}/ Finish ! ", id);
			producer.setSleep(1000);
			producer.run();
		}
	}

	public BitautoBBSListTaskProducer(String name, String forumId) {
		super(BITAUTO_BBS_POST_LIST);
		this.name = name;
		this.forumId = forumId;
	}

	@Override
	protected int getMaxPage() {
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(BITAUTO_BBS_POST_LIST_URL_TEMPLATE, forumId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(name);
		task.setBatchName(BITAUTO_BBS_POST_LIST);
		task.setUrl(url);
		task.setExtra(forumId);
		return task;
	}
}
