package lolthx.yoka.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_bbs_comment")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaBBSCommentBean extends DBBean {
	
	// 主键ID
	@DBConstraintPK
	private String id;
	
	// 主键ID
	@DBConstraintPK
	private String floor;
	
	// 主键ID
	@DBConstraintPK
	private String projectName;
	
	private String keyword;

	private String url;
	
	private String title;
	
	private String authorId;
	
	private String authorName;
	
	private String commentTime;
	
	private String comment;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaBBSCommentBean.class);
	}
	
}
