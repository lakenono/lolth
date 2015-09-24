package dmp.ec;

import java.sql.SQLException;

import lakenono.db.DBBean;

public class ECTable {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(ECBean.class, "yhd");
		DBBean.createTable(ECBean.class, "jd");
		DBBean.createTable(ECBean.class, "taobao");
		DBBean.createTable(ECBean.class, "tmall");
		DBBean.createTable(ECBean.class, "amazon");
	}
}
