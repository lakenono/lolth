package lolthx.word.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lolthx.weixin.bean.WeiXinBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_positive_negative_word")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PositiveAndNegativeWord extends DBBean{
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(PositiveAndNegativeWord.class);
	}
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String keyword;
	
	@DBConstraintPK
	private String postiveOrNegative;
	
	@DBField(type = "text")
	private String text;

	private String str0;
	private String str1;
	private String str2;
	private String str3;
	private String str4;
	private String str5;
	private String str6;
	private String str7;
	private String str8;
	private String str9;
	private String str10;
	
	

	
}
