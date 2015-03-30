package lolth.zol.bbs;

import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.zol.bbs.bean.ZolBBSPostBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class ZolBBSDetailTaskProducer extends ListFetchTaskProducer<String>{
	public static final String  ZOL_DETAIL_PAGE = "zol_bbs_post_detail";
	
	private String keyword ;

	public ZolBBSDetailTaskProducer(String keyword,String taskQueueName) {
		super(taskQueueName);
		this.keyword = keyword;
	}
	
	public static void main(String[] args) throws Exception {
		new ZolBBSDetailTaskProducer("oppo",ZOL_DETAIL_PAGE).run();
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(keyword);
		fetchTask.setBatchName(ZOL_DETAIL_PAGE);
		fetchTask.setUrl(url);
		
		return fetchTask;
	}

	@Override
	protected List<String> getTaskArgs() throws SQLException {
		List<String> urls = GlobalComponents.db.getRunner().query("SELECT url FROM " + BaseBean.getTableName(ZolBBSPostBean.class) + " WHERE content IS NULL",  new ColumnListHandler<String>());
		return urls;
	}

}
