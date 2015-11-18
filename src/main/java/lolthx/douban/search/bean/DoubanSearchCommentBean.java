package lolthx.douban.search.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_douban_search_comment")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class DoubanSearchCommentBean extends DBBean {

	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String keyword;
	
	@DBConstraintPK
	private String floor;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(50)")
	private String authorId;
	
	@DBField(type = "varchar(50)")
	private String authorName;
	
	@DBField(type = "varchar(300)")
	private String title;
	
	@DBField(type = "text")
	private String comment;
	
	@DBField(type = "varchar(50)")
	private String postTime;
	
	@DBField(type = "varchar(50)")
	private String praise;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(DoubanSearchCommentBean.class); 
	}
	
	
}
