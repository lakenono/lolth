package lolth.autohome.buy.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_autohome_buy_info")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper=false)
public class AutohomeBuyInfoBean extends BaseBean{
	@DBConstraintPK
	private String id;
	private String url;
	@DBConstraintPK
	private String forumId;
	
	private String postTime;
	
	//用户
	private String userId;
	private String userUrl;
	private String userName;
	
	private String car;
	//裸车价格
	private String price;
	//指导价格
	private String guidePrice;
	//合计价格
	private String totalPrice;
	
	//购置税
	private String purchaseTax;
	//商业保险
	private String commercialInsurance;
	//车船使用税
	private String vehicleUseTax;
	//交强险
	private String compulsoryInsurance;
	//上牌
	private String licenseFee;
	//推广套餐
	private String promotion;
	
	//商家
	private String sellerId;
	private String sellerName;
	private String sellerUrl;
	private String sellerPhone;
	//商家评价
	private String sellerComment;
	
	//购买
	private String buyTime;
	private String buyProvince;
	private String buyCity;
	private String buyFeeling;
	
	public static void main(String[] args) throws Exception{
		new AutohomeBuyInfoBean().buildTable();
	}
}
