package lolth.official.oppo;

import java.util.List;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.official.oppo.bean.OppoPostBean;
import lolth.official.oppo.bean.OppoUserBean;

public class OppoForumPostUserTaskProducer extends ListFetchTaskProducer<String> {

	private static final String OPPO_FORUM_POST_USER_URL_PREFIX = "http://www.oppo.cn/home.php?uid=";

	public static final String OPPO_FORUM_POST_USER = "oppo_forum_post_user";

	public OppoForumPostUserTaskProducer(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		OppoForumPostUserTaskProducer producer = new OppoForumPostUserTaskProducer(OPPO_FORUM_POST_USER);
		producer.run();
	}

	@Override
	protected FetchTask buildTask(String uid) {
		FetchTask task = new FetchTask();
		task.setName("oppo");
		task.setBatchName(OPPO_FORUM_POST_USER);
		task.setUrl(OPPO_FORUM_POST_USER_URL_PREFIX + uid);
		task.setExtra(uid);
		return task;
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		List<String> uids = GlobalComponents.db.getRunner().query("SELECT DISTINCT autherId from " + BaseBean.getTableName(OppoPostBean.class) + " WHERE autherId IS NOT NULL AND autherId NOT IN (SELECT autherId FROM "+ BaseBean.getTableName(OppoUserBean.class) +") ", new ColumnListHandler<String>());
		return uids;
	}

}
