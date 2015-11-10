package lolthx.yoka.cosmetics.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_cosmetic_detail")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaCosmeticDetailBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String forumId;
	
	@DBConstraintPK
	private String keyword;
	
	@DBField(type = "varchar(60)")
	private String authorId;
	
	@DBField(type = "varchar(60)")
	private String authorName;
	
	@DBField(type = "varchar(50)")
	private String scores;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(300)")
	private String title;
	
	@DBField(type = "varchar(50)")
	private String postTime;
	
	@DBField(type = "varchar(50)")
	private String beOfUsed;
	
	@DBField(type = "varchar(50)")
	private String collection;
	
	@DBField(type = "text")
	private String text;
	
	@DBField(type = "varchar(300)")
	private String comment;
	
	@DBField(type = "varchar(50)")
	private String reply;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaCosmeticDetailBean.class);
	}
	
}
