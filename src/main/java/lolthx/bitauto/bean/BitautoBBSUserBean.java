package lolthx.bitauto.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_bitauto_user")
@Data
public class BitautoBBSUserBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new BitautoBBSUserBean().buildTable();
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
