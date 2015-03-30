package lolth.official.oppo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

/**
 * 过去所有版块的机型列表
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class OppoForumPostListTaskProducer extends PagingFetchTaskProducer {
	public static final String OPPO_FORUM_POST_LIST = "oppo_forum_post_list";

	private static final String OPPO_FORUM_POST_LIST_URL_TEMPLAGE = "http://bbs.oppo.cn/forum-{0}-{1}.html";

	private String forumId;

	public OppoForumPostListTaskProducer(String taskQueueName, String forumId) {
		super(taskQueueName);
		if(!StringUtils.isNumeric(forumId)){
			throw new IllegalArgumentException("forumId is not numric");
		}
		this.forumId = forumId;
	}

	public static void main(String[] args) throws Exception {
		// 关注的机型论坛id
		List<String> forumIds = Lists.newArrayList("319", "433", "432", "457", "397", "100");
		// 添加跟多中的机型论坛id
		forumIds.addAll(getMoreForumUrls());

		log.debug("produce task forum size : " + forumIds.size());
		for (String id : forumIds) {
			OppoForumPostListTaskProducer producer = new OppoForumPostListTaskProducer(OPPO_FORUM_POST_LIST,id );
			log.info("{} task produce start ...", producer.buildUrl(1));
			producer.run();
			log.info("{} task produce finish !", producer.buildUrl(1));
		}

	}

	private static List<String> getMoreForumUrls() throws IOException, InterruptedException {
		String morePageUrl = "http://bbs.oppo.cn/forum.php?mod=forumdisplay&fid=326";

		List<String> aList = new ArrayList<>();

		Document doc = GlobalComponents.fetcher.document(morePageUrl);
		Elements aHref = doc.select("li.tit a");
		if (aHref.size() > 0) {
			for (Element a : aHref) {
				aList.add(StringUtils.substringBetween(a.attr("href"), "-", "-"));
			}
		}
		return aList;
	}

	@Override
	protected int getMaxPage() {
		String url = buildUrl(1);
		try {
			Document doc = GlobalComponents.fetcher.document(url);
			Elements pages = doc.select("span.pageunit");
			if (pages.size() > 0) {
				String maxPage = pages.first().text();
				maxPage = StringUtils.substringBetween(maxPage, "/", "页");
				maxPage = StringUtils.trim(maxPage);
				if (StringUtils.isNumeric(maxPage)) {
					return Integer.parseInt(maxPage);
				}
			}
		} catch (Exception e) {
			log.error("{} get max page error : ", url, e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(OPPO_FORUM_POST_LIST_URL_TEMPLAGE, forumId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName("oppo");
		task.setBatchName(OPPO_FORUM_POST_LIST);
		task.setUrl(url);
		return task;
	}

}
