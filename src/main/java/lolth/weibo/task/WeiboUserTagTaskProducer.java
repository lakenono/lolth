package lolth.weibo.task;

import java.text.MessageFormat;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.weibo.bean.WeiboUserBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class WeiboUserTagTaskProducer extends ListFetchTaskProducer<String> {
	public static final String WEIBO_USER_TAG = "weibo_user_tag";
	private static final String WEIBO_USER_TAG_URL_TEMPLATE = "http://weibo.cn/account/privacy/tags/?uid={0}";

	private String keyword;

	public WeiboUserTagTaskProducer(String taskQueueName, String keyword) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		WeiboUserTagTaskProducer producer = new WeiboUserTagTaskProducer(WEIBO_USER_TAG, "oppo");
		producer.setWaitTaskTime(60 * 60000);
		producer.run();
	}

	@Override
	protected FetchTask buildTask(String uid) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(WEIBO_USER_TAG);
		task.setUrl(buildUrl(uid));
		task.setExtra(uid);
		return task;
	}

	private String buildUrl(String uid) {
		return MessageFormat.format(WEIBO_USER_TAG_URL_TEMPLATE, uid);
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		List<String> uids = GlobalComponents.db.getRunner().query("SELECT uid FROM " + BaseBean.getTableName(WeiboUserBean.class) + " WHERE tags IS NULL", new ColumnListHandler<String>());
		return uids;
	}

}
