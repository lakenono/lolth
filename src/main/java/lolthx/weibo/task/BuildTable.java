package lolthx.weibo.task;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboBean;

public class BuildTable {
	
	public static void main(String[] args) throws SQLException {
		String projectName = "ceshi";
		DBBean.createTable(WeiboBean.class, projectName);
	}

}
