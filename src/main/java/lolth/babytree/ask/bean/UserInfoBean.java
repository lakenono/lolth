package lolth.babytree.ask.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

@DBTable(name = "data_babytree_ask_userinfo")
public class UserInfoBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new UserInfoBean().buildTable();
	}
	// user url
	private String userUrl;

	// username
	private String username;

	// 地域
	private String area;

	// 宝宝性别
	private String babyGender;

	// babyStatus 宝宝状态
	private String babyStatus;

	@Override
	public String toString() {
		return "UserInfoBean [userUrl=" + userUrl + ", username=" + username + ", area=" + area + ", babyGender=" + babyGender + ", babyStatus=" + babyStatus + "]";
	}

	public void persist() throws SQLException {
		GlobalComponents.db.getRunner().update("insert into data_babytree_ask_userinfo(userUrl,username,area,babyGender,babyStatus) values(?,?,?,?,?)", this.userUrl, this.username, this.area, this.babyGender, this.babyStatus);
	}

	public static boolean isExist(String userUrl) throws SQLException {
		long count = GlobalComponents.db.getRunner().query("select count(*) from data_babytree_ask_userinfo where userUrl=?", new ColumnListHandler<Long>(), userUrl).get(0);

		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBabyGender() {
		return babyGender;
	}

	public void setBabyGender(String babyGender) {
		this.babyGender = babyGender;
	}

	public String getBabyStatus() {
		return babyStatus;
	}

	public void setBabyStatus(String babyStatus) {
		this.babyStatus = babyStatus;
	}
}
