package lolthx.weibo.task;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboBean;
import lolthx.weibo.bean.WeiboUserBean;
import lolthx.weibo.bean.WeiboUserConcernRefBean;

public class BuildTable {
	
	public static void main(String[] args) throws SQLException {
		String projectName = "guanzhi_Relaunch";
		DBBean.createTable(WeiboBean.class, projectName);
		DBBean.createTable(WeiboUserBean.class, projectName);
		DBBean.createTable(WeiboUserConcernRefBean.class, projectName);
		
	}

}
