package lolthx.yhd.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 一号店商品
 * @author yanghp
 *
 */
@DBTable(name = "data_yhd_goods_bean")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class GoodsBean extends BaseBean{
	@DBConstraintPK
	private String goodsId;
	private String name;
	private String url;
	private String price;
	//店铺
	private String shop;
	//好评率
	private String strong;
	//评论id
	private String parentId;
	private String productid;
	//评论个数
	private String commments;
	private String classify;
	//搜索关键字
	private String keyword;
	@DBConstraintPK
	private String projectName;
	public static void main(String[] args) throws SQLException {
		new GoodsBean().buildTable();
	}
}
