package lolth.jd.search.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_jd_commodity")
@Data
public class CommodityBean extends BaseBean {
	
	public static void main(String[] args) throws SQLException {
		new CommodityBean().buildTable();
	}

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;

	private String url;
	
	@DBField(type = "varchar(160)")
	private String title;

	@DBField(type = "varchar(32)")
	private String reply;

	@DBField(type = "varchar(32)")
	private String category;

	@DBField(type = "varchar(32)")
	private String features;

	@DBField(type = "varchar(32)")
	private String keyword;
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;
}
