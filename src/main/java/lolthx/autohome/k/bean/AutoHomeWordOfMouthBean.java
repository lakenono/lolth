package lolthx.autohome.k.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_autohome_kb")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class AutoHomeWordOfMouthBean extends BaseBean {
	public static void main(String[] args) throws SQLException {
		new AutoHomeWordOfMouthBean().buildTable();
	}

	// 用户名
	private String username;

	// 用户url
	private String userUrl;

	// 点评帖子url
	private String sourceUrl;

	// 点评帖子title
	private String sourceTitle;

	// 用户认证车型
	private String authCar;

	// 购买车辆
	private String carType;

	// 购买地点
	private String purchasedFrom;

	// 购车经销商
	private String dealer;

	// 购买时间
	private String buyTime;

	// 裸车购买价
	private String price;

	// 油耗
	private String fuelConsumption;

	// 目前行驶
	private String kilometre;

	// 评分-空间
	private String interspaceGrade;

	// 评分-动力
	private String powerGrade;

	// 评分-操控
	private String manipulationGrade;

	// 评分-油耗
	private String fuelConsumptionGrade;

	// 评分-舒适性
	private String comfortGrade;

	// 评分-外观
	private String appearanceGrade;

	// 评分-内饰
	private String innerDecorationGrade;

	// 评分-性价比
	private String performancePriceGrade;

	// 购车目的
	private String aims;

	// 发帖时间
	private String publishTime;

	// --------------------------口碑信息
	// 最满意
	@DBField(type="varchar(1500)")
	private String satisfactoryComment;
	
	// 最不满意
	@DBField(type="varchar(1500)")
	private String unsatisfactoryComment;

	// 空间
	@DBField(type="varchar(1000)")
	private String interspaceComment;

	// 动力
	@DBField(type="varchar(1000)")
	private String powerComment;

	// 操控
	@DBField(type="varchar(1000)")
	private String manipulationComment;

	// 油耗
	@DBField(type="varchar(1000)")
	private String fuelConsumptionComment;

	// 舒适性
	@DBField(type="varchar(1000)")
	private String comfortComment;

	// 外观
	@DBField(type="varchar(1000)")
	private String appearanceComment;

	// 内饰
	@DBField(type="varchar(1000)")
	private String innerDecorationComment;

	// 性价比
	@DBField(type="varchar(1000)")
	private String performancePriceComment;
	
	// 其他
	@DBField(type="varchar(1000)")
	private String otherComment;
	
	//购买原因
	@DBField(type="text")
	private String buyReasonComment;
	
	//-----------------------------------追加评价
	//油耗
	@DBField(type="varchar(1000)")
	private String fuelConsumptionAppend;
	//保养
	@DBField(type="varchar(1000)")
	private String maintenanceAppand;
	//故障
	@DBField(type="varchar(1000)")
	private String faultAppend;
	//吐槽
	@DBField(type="varchar(1000)")
	private String tuCaoAppend;
	
	// 帖子内容
	@DBField(type = "text")
	private String text;

	// 浏览次数
	private String views;

	// 点赞 25
	private String likes;

}
