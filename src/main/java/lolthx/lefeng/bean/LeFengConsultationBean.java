package lolthx.lefeng.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_lefeng_consultation")
@Data
public class LeFengConsultationBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(LeFengConsultationBean.class);
	}

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String itemId;

	@DBField(type = "varchar(32)")
	private String wenNick;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String wenTime;

	private String wenText;

	private String daText;

	@DBField(type = "varchar(32)")
	private String daTime;

	private String url;
}
