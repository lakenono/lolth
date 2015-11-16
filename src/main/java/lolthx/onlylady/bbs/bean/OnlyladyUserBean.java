package lolthx.onlylady.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_onlylady_user")
@Data
public class OnlyladyUserBean extends DBBean {
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(OnlyladyUserBean.class);
	}

	@DBConstraintPK
	private String id;

	private String url;

	private String userName;

	private String sex;

	private String city;

	private String level;

	private String birthday;

	private String interest;

}
