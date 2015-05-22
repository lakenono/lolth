package lolth.zol.bbs.old;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.zol.bbs.bean.ZolBBSPostBean;
import lolth.zol.bbs.bean.ZolBBSUserBean;

public class ZolBBSUserTaskProducer extends ListFetchTaskProducer<String> {
	private static final String ZOL_BBS_USER_URL_TEMPLATE = "http://my.zol.com.cn/{0}/settings/";
	public static final String ZOL_BBS_POST_USER = "zol_bbs_post_user";

	private String keyword;

	public ZolBBSUserTaskProducer(String keyword, String taskQueueName) {
		super(taskQueueName);
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
		String keyword = "oppo";
		ZolBBSUserTaskProducer producer = new ZolBBSUserTaskProducer(keyword, ZOL_BBS_POST_USER);
		producer.run();
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		List<String> users = GlobalComponents.db.getRunner().query("SELECT DISTINCT userId FROM " + BaseBean.getTableName(ZolBBSPostBean.class) + " WHERE userId is not null AND userId NOT IN (SELECT id FROM "+BaseBean.getTableName(ZolBBSUserBean.class)+")", new ColumnListHandler<String>());
		return users;
	}

	@Override
	protected FetchTask buildTask(String user) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(ZOL_BBS_POST_USER);
		task.setUrl(buildUrl(user));
		task.setExtra(user);
		return task;
	}

	private String buildUrl(String user) {
		return MessageFormat.format(ZOL_BBS_USER_URL_TEMPLATE, user);
	}

}
