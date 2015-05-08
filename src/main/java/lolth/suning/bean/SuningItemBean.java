package lolth.suning.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_suning_item")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper=false)
public class SuningItemBean extends BaseBean{
	@DBConstraintPK
	private String id;
	private String title;
	private String url;
	@DBConstraintPK
	private String taskName;
	private String keyword;
	
	private String price;
	private String shop;
	
	//品牌
	private String brand;
	
	private String produceArea;
	
	//包装
	private String packing;
	//段位
	private String  growthStage;
	
	public static void main(String[] args) throws Exception {
		new SuningItemBean().buildTable();
	}
}
