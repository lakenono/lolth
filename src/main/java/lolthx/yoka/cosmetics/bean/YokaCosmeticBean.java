package lolthx.yoka.cosmetics.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_cosmetic")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaCosmeticBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String keyword;
	
	private String url;
	
	@DBField(type="varchar(50)")
	private String star_1;
	
	@DBField(type="varchar(50)")
	private String star_2;
	
	@DBField(type="varchar(50)")
	private String star_3;
	
	@DBField(type="varchar(50)")
	private String star_4;
	
	@DBField(type="varchar(50)")
	private String star_5;
	
	@DBField(type="varchar(50)")
	private String allocationScheme;//综合评分

	@DBField(type="varchar(50)")
	private String avgscore;//平均评分
	
	@DBField(type="varchar(50)")
	private String numberPeople;//评分人数
	
	@DBField(type="varchar(50)")
	private String twentyFivePraise;//25岁以下好评率
	
	@DBField(type="varchar(50)")
	private String twentyFivePeople;//25岁以下点评人数
	
	@DBField(type="varchar(50)")
	private String thirtyPraise;//26-30岁好评率
	
	@DBField(type="varchar(50)")
	private String thirtyPeople;//26-30岁点评人数

	@DBField(type="varchar(50)")
	private String thirtyFivePraise;//31-35岁好评率

	@DBField(type="varchar(50)")
	private String thirtyFivePeople;//26-30岁点评人数
	
	@DBField(type="varchar(50)")
	private String thirtySixUpPraise;//36岁以上好评率
	
	@DBField(type="varchar(50)")
	private String thirtySixUpPeople;//36岁以上好评率

	@DBField(type="varchar(50)")
	private String neutralPraise;//中性皮肤
	
	@DBField(type="varchar(50)")
	private String neutralPeople;//中性皮肤点评人数
	
	@DBField(type="varchar(50)")
	private String mixPraise;//混合性
	
	@DBField(type="varchar(50)")
	private String mixPeople;//混合性点评人数

	@DBField(type="varchar(50)")
	private String oilinessPraise;//油性

	@DBField(type="varchar(50)")
	private String oilinessPeople;//油性点评人数
	
	@DBField(type="varchar(50)")
	private String drynessPraise;//干性
	
	@DBField(type="varchar(50)")
	private String drynessPeople;//干性点评人数
	
	@DBField(type="varchar(50)")
	private String irritabilityPraise;//过敏性

	@DBField(type="varchar(50)")
	private String irritabilityPeople;//过敏性点评人数
	
	@DBField(type="varchar(50)")
	private String sensibilityPraise;//先天过敏性
	
	@DBField(type="varchar(50)")
	private String sensibilityPeople;//先天过敏性点评人数

	@DBField(type="varchar(400)")
	private String publicPraise;//网友口碑
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaCosmeticBean.class);
	}
	
}
