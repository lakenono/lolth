package lolthx.onlylady.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_onlylady_topic")
@Data
public class OnlyladyBean extends DBBean{
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(OnlyladyBean.class);
	}
	@DBConstraintPK
	private String id;

	private String url;
	
	private String uid;
	
	private String userName;
	
	private String title;
	
	private String views;
	
	private String reply;
	@DBField(type = "text")
	private String text;
	
	private String datetime;
	
	private String keyword1;
	
	private String keyword2;
	
	@DBField(type = "varchar(32)")
	private String keyword;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

}
