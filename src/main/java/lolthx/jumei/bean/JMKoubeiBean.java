package lolthx.jumei.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_jumei_koubei")
@Data
public class JMKoubeiBean extends DBBean {
	@DBConstraintPK
	private String id;
	private String about_user;
	@DBField(type = "varchar(4)")
	private String star;
	@DBConstraintPK
	private String title;
	@DBField(type = "text")
	private String text;
	private String time;
	@DBField(type = "varchar(4)")
	private String readers;
	@DBField(type = "varchar(4)")
	private String replys;
	@DBField(type = "varchar(4)")
	private String usings;

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(JMKoubeiBean.class);
	}
}
