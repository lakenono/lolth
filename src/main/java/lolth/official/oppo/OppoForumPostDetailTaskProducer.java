package lolth.official.oppo;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.official.oppo.bean.OppoPostBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class OppoForumPostDetailTaskProducer extends ListFetchTaskProducer<String> {
	public static final String OPPO_FORUM_POST_DETAIL = "oppo_forum_post_detail";

	public OppoForumPostDetailTaskProducer(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		new OppoForumPostDetailTaskProducer(OPPO_FORUM_POST_DETAIL).run();
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName("oppo");
		task.setBatchName(OPPO_FORUM_POST_DETAIL);
		task.setUrl(url);
		return task;
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		List<String> urls = GlobalComponents.db.getRunner().query("SELECT url FROM " + BaseBean.getTableName(OppoPostBean.class) + " WHERE url IS NOT NULL AND content IS NULL", new ColumnListHandler<String>());
		return urls;
	}

}
