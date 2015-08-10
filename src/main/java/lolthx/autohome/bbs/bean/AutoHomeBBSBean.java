package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_bbs")
@Data
public class AutoHomeBBSBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(AutoHomeBBSBean.class);
	}

	@DBConstraintPK
	private String id;

	private String title;

	private String url;

	private String type;

	private String author;

	private String authorId;

	@DBField(type = "varchar(32)")
	private String postTime;

	@DBField(type = "varchar(32)")
	private String views;

	@DBField(type = "varchar(32)")
	private String replys;

	@DBField(type = "text")
	private String text;

	@DBConstraintPK
	private String forumId;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

	@DBField(type = "varchar(32)")
	private String keyword;

}
