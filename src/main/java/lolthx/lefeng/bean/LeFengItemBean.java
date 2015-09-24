package lolthx.lefeng.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_lefeng_item")
@Data
public class LeFengItemBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(LeFengItemBean.class);
	}

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;

	private String url;

	private String title;

	@DBField(type = "varchar(32)")
	private String price;

	// 收藏
	@DBField(type = "varchar(32)")
	private String collection;
	
	//好评率
	@DBField(type = "varchar(32)")
	private String goodCommentPercent;

	//评价数
	@DBField(type = "varchar(32)")
	private String comment;

	//好评数
	@DBField(type = "varchar(32)")
	private String goodComment;

	//中评数
	@DBField(type = "varchar(32)")
	private String inComment;

	//差评数
	@DBField(type = "varchar(32)")
	private String badComment;

	@DBField(type = "varchar(32)")
	private String keyword;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;
}
