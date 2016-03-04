package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.Column;
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
	@Column(selectColumn=true,columnAs="ID")
	private String id;
	
	@Column(selectColumn=true,columnAs="用户名")
	private String name;

	// 作者地址
	@Column(selectColumn=true,columnAs="用户url")
	private String authorUrl;
	
	// 区域
	@Column(selectColumn=true,columnAs="区域")
	private String area;
	
	@Column(selectColumn=true,columnAs="汽车")
	private String car;
	
	// 关注
	@Column(selectColumn=true,columnAs="关注")
	private String concern;
}
