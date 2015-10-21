package lolthx.hers.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_hers_bbs_weibo")
@Data
public class HersBbsWeibo extends DBBean {
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	private String content;
	@DBField(type = "varchar(32)")
	private String time;
	private String weiboName;
	private String weiboUrl;
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(HersBbsWeibo.class);

	}

}
