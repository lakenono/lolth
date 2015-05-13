package lolth.muying.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_muying_commodity")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class CommodityBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new CommodityBean().buildTable();
	}
	
	/*
	 * 商品ID
	 */
	@DBConstraintPK
	private String commodityId;
	/*
	 * 商品名称
	 */
	private String commodityName="";
	/*
	 * 商品url
	 */
	private String commodityUrl="";
	/*
	 * 商品价格
	 */
	private String commodityPrice="";
	/*
	 * 商品单位
	 */
	private String commodityUnit = "";
	/*
	 * 商品品牌
	 */
	private String commodityBrand="";
	/*
	 * 商品阶段
	 */
	private String commodityStage="";
	/*
	 * 商品产地
	 */
	private String commodityPlace="";
	/*
	 * 搜索关键字
	 */
	private String keyword="";

	private String subjectTask="";
}
