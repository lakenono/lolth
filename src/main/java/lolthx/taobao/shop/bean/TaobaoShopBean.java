package lolthx.taobao.shop.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_taobao_shop")
@Data
public class TaobaoShopBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new TaobaoShopBean().buildTable();
	}

	@DBConstraintPK
	private String userId;
	
	private String url;
	
	private String shopId;
	
	private String name;
	
	private String company;
	
	private String area;
	
	private String type;
	
	private String descScore;

	private String serviceScore;

	private String deliverScore;

	private String descLevel;

	private String serviceLevel;

	private String deliverLevel;
	
}
