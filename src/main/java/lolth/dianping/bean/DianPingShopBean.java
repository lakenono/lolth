package lolth.dianping.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@Data
@DBTable(name = "data_dianping_shop")
public class DianPingShopBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new DianPingShopBean().buildTable();
	}

	// 店名
	private String shopname;

	// 店铺url
	private String shopurl;

	// 店铺id
	private String shopid;

	// 分店列表
	private String branchUrl;

	// 评分
	private String rank;

	// 点评数
	@DBField(type = "int")
	private int comments;

	// 人均消费
	@DBField(type = "float")
	private double meanPrice;

	// 菜系
	private String cuisines;

	// 地址
	private String addr;

	// 城市id
	@DBField(type = "int")
	private int cityid;

	// keyword
	private String keyword;
}
