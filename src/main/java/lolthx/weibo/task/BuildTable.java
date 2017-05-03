package lolthx.weibo.task;

import java.sql.SQLException;

import lakenono.base.Task;
import lakenono.db.DBBean;

public class BuildTable {
	
	public static void main(String[] args) throws SQLException {
//		String projectName = "guanzhi_Relaunch";
//		DBBean.createTable(WeiboBean.class);
//		DBBean.createTable(WeiboUserBean.class);
//		DBBean.createTable(WeiboUserConcernRefBean.class);
		DBBean.createTable(Task.class);
		
	}

}
