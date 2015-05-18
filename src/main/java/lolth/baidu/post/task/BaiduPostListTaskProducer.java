package lolth.baidu.post.task;

import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 百度贴吧列表抓取
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class BaiduPostListTaskProducer extends PagingFetchTaskProducer {
	
	public static final String BAIDU_POST_NAME = "育儿";

	private static final String BAIDU_POST_SEARCH_URL_TEMPLATE = "http://tieba.baidu.com/f?kw={0}&ie=utf-8&pn={1}";

	private static final int PAGE_SIZE = 50;

	// 任务名称
	private String keyword;

	// 批次名称
	public static final String BAIDU_POST_LIST = "baidu_post_list";

	public BaiduPostListTaskProducer(String taskQueueName, String keyword) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws SQLException {
		new BaiduPostListTaskProducer(BAIDU_POST_LIST, BAIDU_POST_NAME).run();
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(BAIDU_POST_SEARCH_URL_TEMPLATE, keyword, String.valueOf((pageNum - 1) * PAGE_SIZE));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(BAIDU_POST_LIST);
		task.setUrl(url);
		return task;
	}

	@Override
	protected int getMaxPage() {
		String url = buildUrl(FIRST_PAGE);
		try {
			Document doc = GlobalComponents.fetcher.document(url);

			Elements div = doc.select("div.forum_foot div.th_footer_2 div.th_footer_bright div.th_footer_l");
			if (div.size() == 0) {
				return 0;
			}

			String pageSize = div.first().text();
			pageSize = StringUtils.substringBetween(pageSize, "共有主题数", "个");

			log.info("getMapPage: {}",pageSize);

			if (!StringUtils.isNumeric(pageSize)) {
				return 0;
			}

			return Integer.parseInt(pageSize) / PAGE_SIZE;
		} catch (Exception e) {
			log.error("Get Max Page Error : {}", url, e);
		}
		return 0;
	}
}
