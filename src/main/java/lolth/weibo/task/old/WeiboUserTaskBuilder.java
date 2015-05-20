package lolth.weibo.task.old;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.bean.WeiboUserBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class WeiboUserTaskBuilder {
	private static final int PAGE_SIZE = 100;

	private String keyword;

	public WeiboUserTaskBuilder(String keyword) {
		this.keyword = keyword;
	}

	public static void main(String[] args) throws Exception {
//		new WeiboUserTaskBuilder("香水").runTop(1000);
		new WeiboUserTaskBuilder("香水").run();
	}

	public void runTop(int top) throws Exception {
		int pageSize = PAGE_SIZE;

		for (int start = 0; start < top; start += PAGE_SIZE) {
			if (start + pageSize > top) {
				pageSize = top - start;
			}

			List<String> uids = GlobalComponents.db.getRunner().query("select DISTINCT w.userid from " + BaseBean.getTableName(WeiboBean.class) + " w where w.keyword = ? order by w.reposts+0 desc limit ?,?", new ColumnListHandler<>(), keyword, start, pageSize);

			for (String u : uids) {
				new WeiboUserBean(u).persistOnNotExist();
			}
			Thread.sleep(60000);
		}
	}

	public void run() throws Exception {
		while (true) {
			List<String> uids = GlobalComponents.db.getRunner().query("select DISTINCT w.userid from " + BaseBean.getTableName(WeiboBean.class) + " w where w.keyword=? and w.userid not in (select DISTINCT u.userId from " + BaseBean.getTableName(WeiboUserBean.class) + " u ) limit ?", new ColumnListHandler<>(), keyword, 100);
			if(!uids.isEmpty()){
				for (String u : uids) {
					new WeiboUserBean(u).persistOnNotExist();
				}
			}else{
				Thread.sleep(60000);
			}
			
		}
	}

}
