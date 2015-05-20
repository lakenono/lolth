package lolth.weibo.task;

import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lolth.weibo.fetcher.WeiboFetcher;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WeiboUserConcernListTaskProducer extends PagingFetchTaskProducer {

	public static final String WEIBO_USER_CONCERN_LIST = "weibo_user_concern_list";

	private String taskName;

	private String uid;

	public WeiboUserConcernListTaskProducer(String uid, String taskName) {
		super(WEIBO_USER_CONCERN_LIST);
		this.uid = uid;
		this.taskName = taskName;
	}

	private static final String WEIBO_USER_FOLLOWS_URL_TEMPLATE = "http://weibo.cn/{0}/follow?page={1}";

	@Override
	protected int getMaxPage() {
		try {
			Document doc = WeiboFetcher.cnFetcher.fetch(buildUrl(1));
			
			
			Elements pageList = doc.select("div#pagelist div");
			if (pageList.size() > 0) {
				String pageStr = pageList.first().ownText();
				pageStr = StringUtils.substringBetween(pageStr, "/", "页");
				pageStr = StringUtils.trim(pageStr);

				if (StringUtils.isNumeric(pageStr)) {
					return Integer.parseInt(pageStr);
				}
			}else{
				Elements concerns = doc.select("table");
				if(!concerns.isEmpty()){
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(WEIBO_USER_FOLLOWS_URL_TEMPLATE, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(taskName);
		task.setBatchName(WEIBO_USER_CONCERN_LIST);
		task.setUrl(url);
		task.setExtra(uid);
		return task;
	}
}
