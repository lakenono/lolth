package lolth.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_autohome_bbs_user")
public class AutoHomeBBSUserBean extends BaseBean {

	// 作者地址
	@DBConstraintPK
	private String authorUrl;
	// 区域
	private String area;

	private String car;
	// 关注
	private String concern;

	public static void main(String[] args) throws SQLException {
		new AutoHomeBBSUserBean().buildTable();
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getConcern() {
		return concern;
	}

	public void setConcern(String concern) {
		this.concern = concern;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(area).append('|');
		str.append(car).append('|');
		str.append(concern);
		return str.toString();
	}
}
