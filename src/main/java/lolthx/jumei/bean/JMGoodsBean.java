package lolthx.jumei.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_jumei_goods")
@Data
public class JMGoodsBean extends DBBean {
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	private String name;
	@DBField(type = "varchar(10)")
	private String price;
	private String url;
	@DBField(type = "varchar(10)")
	private String deals;
	@DBField(type = "varchar(10)")
	private String star;
	@DBField(type = "varchar(10)")
	private String koubei;
	@DBField(type = "varchar(10)")
	private String duanping;
	private String function;

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(JMGoodsBean.class);
	}
}
