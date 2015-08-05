package lolthx.bitauto.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
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
	private String id;
	private String name;
	private String url;

	// 省
	private String province;
	// 市
	private String city;
	
	//注册时间
	private String regTime;
	
	//-------------可变数据-----------------
	//等级
	private String level;
	//帖子数
	private String posts;
	//精华数
	private String elites;
	//车型
	private String car;
}
