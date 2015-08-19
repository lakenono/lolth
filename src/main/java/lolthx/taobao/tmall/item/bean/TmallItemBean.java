package lolthx.taobao.tmall.item.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_tmall_item")
@Data
public class TmallItemBean extends BaseBean {

	@DBConstraintPK
	private String itemId;
	private String title;
	private String url;
	private String price;
	private String monthSales;
	private String comments;
	private String userId;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

	@DBField(type = "varchar(32)")
	private String keyword;
	
	public static void main(String[] args) throws SQLException {
		new TmallItemBean().buildTable();
	}
}
