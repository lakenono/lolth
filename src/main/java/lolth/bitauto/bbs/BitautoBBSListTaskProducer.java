package lolth.bitauto.bbs;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class BitautoBBSListTaskProducer extends PagingFetchTaskProducer {
	public static final String BITAUTO_BBS_POST_LIST = "bitauto_bbs_post_list";

	private static final String BITAUTO_BBS_POST_LIST_URL_TEMPLATE = "http://baa.bitauto.com/{0}/index-all-all-{1}-0.html";

	protected String name;

	protected String forumId;

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
		this(BITAUTO_BBS_POST_LIST, name, forumId);
	}

	public BitautoBBSListTaskProducer(String queueName, String name, String forumId) {
		super(queueName);
		this.name = name;
		this.forumId = forumId;
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));

			Elements pageEs = doc.select("div.the_pages a");

			// 没有分页标签
			if (pageEs.isEmpty()) {
				if (!doc.select("div.postslist_xh").isEmpty()) {
					return 1;
				}
			}

			// 有分页标签
			if (pageEs.size() >= 3) {
				String pages = pageEs.get(pageEs.size() - 2).text();
				if (StringUtils.isNumeric(pages)) {
					return Integer.parseInt(pages);
				}
			}
		} catch (Exception e) {
			log.warn("Get max page error : ", e);
		}

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
