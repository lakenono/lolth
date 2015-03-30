package lolth.baidu.post.task;

import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lolth.baidu.post.bean.BaiduPostUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.handlers.ArrayListHandler;

/**
 * 用户信息抓取
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class BaiduPostUserTaskProducer extends FetchTaskProducer {
	public static final String BAIDU_POST_USER = "baidu_post_user";

	private String keyword;

	public BaiduPostUserTaskProducer(String taskQueueName, String keyword) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws SQLException {
		BaiduPostUserTaskProducer taskProducer = new BaiduPostUserTaskProducer(BAIDU_POST_USER, "oppo");
		taskProducer.run();
	}

	private void run() throws SQLException {
		super.cleanAllTask();
		List<Object[]> tasks = buildTasks();
		for (Object[] t : tasks) {
			try {
				FetchTask task = buildTask(t[0].toString(), t[1].toString());
				super.saveAndPushTask(task);
			} catch (Exception e) {
				log.error("{}|{} push error : ", t[0].toString(), t[1].toString(), e);
			}
		}
	}

	public FetchTask buildTask(String id, String url) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(keyword);
		fetchTask.setUrl(url);
		fetchTask.setBatchName(BAIDU_POST_USER);
		fetchTask.setExtra(id);
		return fetchTask;
	}

	private List<Object[]> buildTasks() throws SQLException {
		List<Object[]> query = GlobalComponents.db.getRunner().query("select id,url from " + BaseBean.getTableName(BaiduPostUserBean.class) + " where name is null", new ArrayListHandler());
		return query;
	}
}
