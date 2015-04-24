package lolth.pacuto.k.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_pacuto_koubei")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PacutoWordOfMouthPostBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String url;
	private String postTime;
	
	@DBConstraintPK
	private String forumId;
	
	//用户信息
	private String authorId;
	private String authorName;
	private String authorUrl;
	
	//附加信息
	private String car;
	private String buyTime;
	private String buyProvince;
	private String buyCity;
	
	// 卖家
	private String seller;
	// 裸车价格
	private String price;
	// 平均油耗
	private String averageOilCost;
	// 行使里程
	private String runMiles;

	// 点评-------------------------------------------
	// 车主印象
	private String impression;

	// 优点
	private String advantage;

	// 缺点
	private String shortcoming;

	// 外观
	private String exteriorScores;
	private String exteriorComment;

	// 内饰
	private String interiorScores;
	private String interiorComment;

	// 空间
	private String spaceScores;
	private String spaceComment;

	// 配置
	private String configScores;
	private String configComment;

	// 动力
	private String powerScores;
	private String powerComment;

	// 越野
	private String offRoadScores;
	private String offRoadComment;

	// 油耗
	private String oilCostScores;
	private String oilCostComment;

	// 舒适度
	private String comfortScores;
	private String comfortComment;

	// 追加部分-----------------------------
	// 油耗
	private String oilCostAppend;

	// 保养
	private String maintenanceAppend;

	// 售后
	private String customerServiceAppend;

	// 质量
	private String qualityAppend;

	// 其他
	private String otherAppend;

	public static void main(String[] args) throws Exception {
		new PacutoWordOfMouthPostBean().buildTable();
	}
}
