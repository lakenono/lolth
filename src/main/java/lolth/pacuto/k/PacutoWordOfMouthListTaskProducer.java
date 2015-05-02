package lolth.pacuto.k;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 解析列表页第一页，获取最大页数，生成TASK
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class PacutoWordOfMouthListTaskProducer extends PagingFetchTaskProducer {

	public static final String PACUTO_K_POST_LIST = "pacuto_k_post_list";

	private static final String PACUTO_K_POST_LIST_URL_TEMPLATE = "http://price.pcauto.com.cn/comment/{0}/p{1}.html";

	private String name;
	private String forumId;

	public PacutoWordOfMouthListTaskProducer(String name, String forumId) {
		super(PACUTO_K_POST_LIST);
		this.name = name;
		this.forumId = forumId;
	}

	public static void main(String[] args) {
		String name = "chevrolet";

		/*
		 * 创酷 http://price.pcauto.com.cn/comment/sg10400/ 北京现代ix25
		 * http://price.pcauto.com.cn/comment/sg9917/ 别克昂科拉
		 * http://price.pcauto.com.cn/comment/sg7891/ 福特翼搏
		 * http://price.pcauto.com.cn/comment/sg8029/ 标致2008
		 * http://price.pcauto.com.cn/comment/sg10059/ 缤智
		 * http://price.pcauto.com.cn/comment/sg10791/
		 */
		String[] forumIds = { "sg10400", "sg9917", "sg7891", "sg8029", "sg10059", "sg10791" };

		for (String id : forumIds) {
			log.info("Handler http://price.pcauto.com.cn/comment/{}/ start ! ");
			PacutoWordOfMouthListTaskProducer producer = new PacutoWordOfMouthListTaskProducer(name, id);
			producer.setSleep(1000);
			producer.run();

			log.info("Handler http://price.pcauto.com.cn/comment/{}/ finish ! ");
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			
			Elements pageEs = doc.select("div#pcauto_page a");

			// 没有分页标签
			if (pageEs.isEmpty()) {
				if (!doc.select("div.main_table.clearfix").isEmpty()) {
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
			log.error("Get max pages error: ", e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(PACUTO_K_POST_LIST_URL_TEMPLATE, forumId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(name);
		task.setBatchName(PACUTO_K_POST_LIST);
		task.setUrl(url);
		task.setExtra(forumId);
		return task;
	}

}
