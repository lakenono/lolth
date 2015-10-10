package lolthx.weibo.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_sina_weibo_city")
@Data
public class WeiboCity extends DBBean{
	private String city;
	private String weiboUser;
	private int numbers;
	private String fans;
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(WeiboCity.class);
	}
}
