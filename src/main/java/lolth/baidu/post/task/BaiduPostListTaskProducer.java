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
		String[] keywords = { "英大泰和财产","英大车险","英大财险","平安车险","平安财险","阳光车险","阳光财险","大地车险","大地财险","电动车","电动汽车"};
		for (String key : keywords) {
			log.info("{} start!", key);
			BaiduPostListTaskProducer producer = new BaiduPostListTaskProducer(BAIDU_POST_LIST, key);
			producer.run();
			log.info("{} finish!", key);
		}
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

			log.info("getMapPage: {}", pageSize);

			if (!StringUtils.isNumeric(pageSize)) {
				return 0;
			}

			int maxpage = Integer.parseInt(pageSize) / PAGE_SIZE + 1;
			if(maxpage>500){
				maxpage = 500;
			}
			return maxpage;
		} catch (Exception e) {
			log.error("Get Max Page Error : {}", url, e);
		}
		return 0;
	}
}
