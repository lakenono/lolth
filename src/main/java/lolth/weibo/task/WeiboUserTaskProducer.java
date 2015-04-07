package lolth.weibo.task;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.bean.WeiboUserBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboUserTaskProducer extends ListFetchTaskProducer<String> {
	private static final String WEIBO_USER_URL_TEMPLATE = "http://weibo.cn/u/{0}";
	private static final String WEIBO_USER_INFO_URL_TEMPLAGE = "http://weibo.cn/{0}/info";

	public static final String WEIBO_USER = "weibo_user";

	public long sleep = 15000;

	private String keyword;

	public WeiboUserTaskProducer(String taskQueueName, String keyword) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		WeiboUserTaskProducer producer = new WeiboUserTaskProducer(WEIBO_USER, "oppo");
		producer.setWaitTaskTime(60 * 60000);
		producer.run();
	}

	@Override
	protected FetchTask buildTask(String id) {
		// 需要根据string类型的id获取uid
		FetchTask task = null;

		try {
			String uid = id;
			if (!StringUtils.isNumeric(uid)) {
				uid = getUid(id);
				Thread.sleep(sleep);
			}

			task = new FetchTask();
			task.setName(keyword);
			task.setBatchName(WEIBO_USER);
			task.setUrl(buildUserInfoUrl(uid));

			task.setExtra(id + "," + uid);
		} catch (Exception e) {
			log.error("{} get uid error :", id, e);
		}

		return task;
	}

	private String buildUserUrl(String id) {
		return MessageFormat.format(WEIBO_USER_URL_TEMPLATE, id);
	}

	private String buildUserInfoUrl(String uid) {
		return MessageFormat.format(WEIBO_USER_INFO_URL_TEMPLAGE, uid);
	}

	private String getUid(String nickname) throws IOException, InterruptedException {
		String uid = null;

		Document doc = WeiboFetcher.cnFetcher.fetch(buildUserUrl(nickname));
		Elements imgElements = doc.select("img.por");
		if (imgElements.size() > 0) {
			uid = imgElements.first().parent().attr("href");
			uid = StringUtils.substringBetween(uid, "/", "/");
		}
		return uid;
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		List<String> uids = GlobalComponents.db.getRunner().query("SELECT DISTINCT userid FROM " + BaseBean.getTableName(WeiboBean.class) + " WHERE keyword=? and userid NOT IN (SELECT id FROM " + BaseBean.getTableName(WeiboUserBean.class) + ")", new ColumnListHandler<String>(), keyword);
		return uids;
	}

}
