package lolth.xcar.k;

import java.text.MessageFormat;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.xcar.bbs.XCarBBSPostDetailProducer;
import lolth.xcar.bbs.bean.XCarBBSPostBean;
import lolth.xcar.k.bean.XCarWordOfMouthBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class XCarWordOfMouthPostProducer extends ListFetchTaskProducer<String> {
	private static final String XCAR_BBS_POST_DETAIL_URL_TEMPLATE = "http://www.xcar.com.cn/bbs/viewthread.php?tid={0}";
	private String keyword;

	public XCarWordOfMouthPostProducer(String keyword) {
		super(XCarBBSPostDetailProducer.XCAR_BBS_POST_DETAIL);
		this.keyword = keyword;
	}

	@Override
	protected FetchTask buildTask(String postId) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(taskQueueName);
		task.setUrl(buildUrl(postId));
		return task;
	}

	private String buildUrl(String postId) {
		return MessageFormat.format(XCAR_BBS_POST_DETAIL_URL_TEMPLATE, postId);
	}

	@Override
	protected List<String> getTaskArgs() throws Exception {
		String sql = "SELECT DISTINCT postId from " + BaseBean.getTableName(XCarWordOfMouthBean.class) + " where postId NOT IN ( SELECT id FROM " + BaseBean.getTableName(XCarBBSPostBean.class) + ")";
		List<String> postIds = GlobalComponents.db.getRunner().query(sql, new ColumnListHandler<String>());
		return postIds;
	}

	public static void main(String[] args) throws Exception {
		String keyword = "chevrolet";
		XCarWordOfMouthPostProducer producer = new XCarWordOfMouthPostProducer(keyword);
		producer.run();
	}

}
