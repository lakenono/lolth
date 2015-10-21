package lolthx.hers.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_hers_bbs_comment")
@Data
public class HersBbsComment extends DBBean {
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	@DBField(type = "text")
	private String comment;
	@DBField(type = "varchar(32)")
	@DBConstraintPK
	private String time;
	@DBField(type = "varchar(32)")
	private String userName;
	@DBField(type = "varchar(32)")
	private String area;
	@DBField(type = "varchar(32)")
	private String lastOnline;
	@DBField(type = "varchar(32)")
	private String onlineTime;
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(HersBbsComment.class);
	}

}
