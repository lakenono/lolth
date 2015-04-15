package lolth.baidu.post.task;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.baidu.post.bean.BaiduPostUserBean;

import org.apache.commons.dbutils.handlers.ArrayListHandler;

/**
 * 用户信息抓取
 * 
 * @author shi.lei
 *
 */
public class BaiduPostUserTaskProducer extends ListFetchTaskProducer<Object[]> {
	public static final String BAIDU_POST_USER = "baidu_post_user";

	private String keyword;

	public BaiduPostUserTaskProducer(String taskQueueName, String keyword) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		BaiduPostUserTaskProducer taskProducer = new BaiduPostUserTaskProducer(BAIDU_POST_USER, "oppo");
		taskProducer.run();
	}

	@Override
	protected FetchTask buildTask(Object[] t) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(keyword);
		fetchTask.setUrl(t[1].toString());
		fetchTask.setBatchName(BAIDU_POST_USER);
		fetchTask.setExtra(t[0].toString());
		return fetchTask;
	}

	@Override
	protected List<Object[]> getTaskArgs() throws Exception {
		List<Object[]> query = GlobalComponents.db.getRunner().query("select id,url from " + BaseBean.getTableName(BaiduPostUserBean.class) + " where name is null", new ArrayListHandler());
		return query;
	}
}
