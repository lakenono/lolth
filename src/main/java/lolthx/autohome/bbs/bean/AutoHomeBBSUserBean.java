package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_user")
@Data
public class AutoHomeBBSUserBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(AutoHomeBBSUserBean.class);
	}

	@DBConstraintPK
	private String id;

	private String name;

	// 作者地址
	private String authorUrl;
	// 区域
	private String area;

	private String car;
	// 关注
	private String concern;
}
