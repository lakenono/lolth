package lolthx.yoka.cosmetics.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_cosmetic_comment")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaCosmeticCommentBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String forumId;
	
	@DBConstraintPK
	private String keyword;
	
	@DBField(type = "varchar(50)")
	private String postTime;
	
	@DBConstraintPK
	private String floor;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(50)")
	private String authorId;
	
	@DBField(type = "varchar(50)")
	private String authorName;
	
	@DBField(type = "text")
	private String comment;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaCosmeticCommentBean.class);
	}
}
