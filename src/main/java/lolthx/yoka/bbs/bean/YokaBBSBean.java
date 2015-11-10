package lolthx.yoka.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_bbs")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaBBSBean extends DBBean{

	@DBConstraintPK
	private String id;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String keyword;

	private String title;

	private String url;

	private String authorId;
	
	private String authorName;

	@DBField(type = "varchar(32)")
	private String postTime;

	@DBField(type = "varchar(32)")
	private String views;

	@DBField(type = "varchar(32)")
	private String replys;

	@DBField(type = "text")
	private String text;

	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(YokaBBSBean.class);
	}

	
}
