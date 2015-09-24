package lolthx.lefeng.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_lefeng_comment")
@Data
public class LeFengCommentBean extends DBBean {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(LeFengCommentBean.class);
	}

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String itemId;

	@DBField(type = "varchar(32)")
	private String nick;

	@DBField(type = "varchar(32)")
	private String score;

	@DBField(type = "varchar(500)")
	private String text;

	@DBField(type = "varchar(32)")
	private String time;

	@DBField(type = "varchar(8)")
	private String type;
	
	private String useful;
	
	private String url;
}
