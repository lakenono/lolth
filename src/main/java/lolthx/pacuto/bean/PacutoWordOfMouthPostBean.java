package lolthx.pacuto.bean;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_pacuto_koubei")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PacutoWordOfMouthPostBean extends DBBean {
	@DBConstraintPK
	private String id;
	private String url;
	private String postTime;
	
	@DBConstraintPK
	private String forumId;
	
	private String projectName;
	private String keyword;
	
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
	@DBField(type="varchar(400)")
	private String impression;

	// 优点
	@DBField(type="varchar(400)")
	private String advantage;

	// 缺点
	@DBField(type="varchar(400)")
	private String shortcoming;

	// 外观
	private String exteriorScores;
	@DBField(type="varchar(400)")
	private String exteriorComment;

	// 内饰
	private String interiorScores;
	@DBField(type="varchar(400)")
	private String interiorComment;

	// 空间
	private String spaceScores;
	@DBField(type="varchar(400)")
	private String spaceComment;

	// 配置
	private String configScores;
	@DBField(type="varchar(400)")
	private String configComment;

	// 动力
	private String powerScores;
	@DBField(type="varchar(400)")
	private String powerComment;

	// 越野
	private String offRoadScores;
	@DBField(type="varchar(400)")
	private String offRoadComment;

	// 油耗
	private String oilCostScores;
	@DBField(type="varchar(400)")
	private String oilCostComment;

	// 舒适度
	private String comfortScores;
	@DBField(type="varchar(400)")
	private String comfortComment;

	// 追加部分-----------------------------
	// 油耗
	@DBField(type="varchar(400)")
	private String oilCostAppend;

	// 保养
	@DBField(type="varchar(400)")
	private String maintenanceAppend;

	// 售后
	@DBField(type="varchar(400)")
	private String customerServiceAppend;

	// 质量
	@DBField(type="varchar(400)")
	private String qualityAppend;

	// 其他
	@DBField(type="text")
	private String otherAppend;

	public static void main(String[] args) throws Exception {
		DBBean.createTable(PacutoWordOfMouthPostBean.class);
	}
}
