package lolthx.taobao.item.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_taobao_item")
@Data
public class TaobaoItemBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new TaobaoItemBean().buildTable();
	}

	@DBConstraintPK
	private String itemId;
	
	private String title;
	
	private String url;
	
	private String price;
	
	private String userId;
	
	private String comments;
	
	private String monthSales;
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;
	
	@DBField(type = "varchar(32)")
	private String keyword;
}
