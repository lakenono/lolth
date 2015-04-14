package lolth.tmall.detail.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DBTable(name="data_tmall_goods_extra")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TmallGoodsExtraBean extends BaseBean{
	@DBConstraintPK
	private String goodsId;
	//品牌
	private String brand;
	//调味料
	private String tiaoliao;
	
	public static void main(String[] args) throws Exception{
		new TmallGoodsExtraBean().buildTable();
	} 
}
