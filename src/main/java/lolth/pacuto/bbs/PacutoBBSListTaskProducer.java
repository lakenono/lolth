package lolth.pacuto.bbs;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 根据首页获获取最大页数，生成TASK
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class PacutoBBSListTaskProducer extends PagingFetchTaskProducer {
	private static final String PACUTO_BBS_POST_LIST_URL_TEMPLATE = "http://bbs.pcauto.com.cn/forum-{0}-{1}.html";
	public static final String PACUTO_BBS_POST_LIST = "pacuto_bbs_post_list";
	private String name;
	private String forumId;

	public static void main(String[] args) {
		String[] forumIds = { "20095", "20200", "18795", "18665", "19865", "20106" };
		String name = "chevrolet";
		for (String id : forumIds) {
			log.info("Handler http://bbs.pcauto.com.cn/forum-{}.html Start ! ", id);
			PacutoBBSListTaskProducer producer = new PacutoBBSListTaskProducer(name, id);
			log.info("Handler http://bbs.pcauto.com.cn/forum-{}.html Finish ! ", id);
			producer.setSleep(1000);
			producer.run();
		}

	}

	public PacutoBBSListTaskProducer(String name, String forumId) {
		super(PACUTO_BBS_POST_LIST);
		this.name = name;
		this.forumId = forumId;
		// TODO Auto-generated constructor stub
	}

	// public PacutoBBSListTaskProducer(){
	// this(BITAUTO_BBS_POST_LIST_URL_TEMPLATE,name,forumId);
	// }

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));

			Elements pageEs = doc.select("div.pager a");

			// 没有分页标签
			if (pageEs.isEmpty()) {
				if (!doc.select("span.checkbox_title a.topicurl").isEmpty()) {
					return 1;
				}
			}

			// 有分页标签
			if (pageEs.size() >= 3) {
				String pages = pageEs.get(pageEs.size() - 2).text();
				if (StringUtils.isNumeric(pages)) {
					return Integer.parseInt(pages);
				} else {
					return Integer.parseInt(StringUtils.remove(pages, "..."));
				}
			}
		} catch (Exception e) {
			log.warn("Get max page error : ", e);
		}

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(PACUTO_BBS_POST_LIST_URL_TEMPLATE, forumId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		// 任务名称：一般以任务标号如：oppo，雪佛兰
		task.setName(name);
		// 任务批次名称：一般以任务阶段，定义成对列名即可
		task.setBatchName(PACUTO_BBS_POST_LIST);
		// 任务要抓取的url
		task.setUrl(url);
		// 任务扩展信息，用于在任务之间传递附加数据
		// task.setExtra(forumId);
		return task;
	}
}
