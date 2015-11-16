package lolthx.hers.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_hers_bbs_main")
@Data
public class HersBbsMainText extends DBBean{
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	private String title;
	@DBField(type = "text")
	private String text;
	private String time;
	private String tags;
	@DBField(type = "varchar(4)")
	private String readers;
	@DBField(type = "varchar(4)")
	private String replies;
	@DBField(type = "varchar(4)")
	private String store;
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(HersBbsMainText.class);
	}
}
