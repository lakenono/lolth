package lolthx.bitauto.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.Column;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_bitauto_user")
@Data
public class BitautoBBSUserBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(BitautoBBSUserBean.class);
	}

	@DBConstraintPK
	@Column(selectColumn=true,columnAs="ID")
	private String id;
	
	@Column(selectColumn=true,columnAs="用户名称")
	private String name;
	
	@Column(selectColumn=true,columnAs="用户url")
	private String url;

	// 省
	@Column(selectColumn=true,columnAs="盛丰")
	private String province;
	
	// 市
	@Column(selectColumn=true,columnAs="城市")
	private String city;
	
	//注册时间
	@Column(selectColumn=true,columnAs="注册时间")
	private String regTime;
	
	//-------------可变数据-----------------
	//等级
	@Column(selectColumn=true,columnAs="等级")
	private String level;
	
	//帖子数
	@Column(selectColumn=true,columnAs="帖子数")
	private String posts;
	
	//精华数
	@Column(selectColumn=true,columnAs="精华数")
	private String elites;
	
	//车型
	@Column(selectColumn=true,columnAs="车型")
	private String car;
}
