package lolth.weibo.task;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.weibo.bean.WeiboBean;
import lolth.weibo.bean.WeiboUserBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class WeiboUserTaskBuilder {
	private static final int PAGE_SIZE = 100;

	private String keyword;
	private int top;

	public WeiboUserTaskBuilder(String keyword, int top) {
		this.keyword = keyword;
		this.top = top;
	}

	public static void main(String[] args) throws Exception {
		new WeiboUserTaskBuilder("香奈儿 香水", 1000).run();
	}

	private void run() throws Exception {
		int pageSize = PAGE_SIZE;

		for (int start = 0; start <top; start += PAGE_SIZE) {
			if(start+pageSize>top){
				pageSize = top-start;
			}
			
			List<String> uids = GlobalComponents.db
					.getRunner()
					.query("select DISTINCT w.userid from "
							+ BaseBean.getTableName(WeiboBean.class)
							+ " w where w.keyword = ? order by w.reposts+0 desc limit ?,?",
							new ColumnListHandler<>(), keyword, start, pageSize);
			
			for (String u : uids) {
				new WeiboUserBean(u).persist();
			}
		}
	}
}
