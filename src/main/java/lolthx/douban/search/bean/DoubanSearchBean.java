package lolthx.douban.search.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_douban_search")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class DoubanSearchBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String keyword;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(50)")
	private String authorId;
	
	@DBField(type = "varchar(50)")
	private String authorName;
	
	@DBField(type = "varchar(300)")
	private String title;
	
	@DBField(type = "text")
	private String content;
	
	@DBField(type = "varchar(50)")
	private String postTime;
	
	@DBField(type = "varchar(50)")
	private String reply;
	
	@DBField(type = "varchar(50)")
	private String likes;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(DoubanSearchBean.class); 
	}
	
	
	
}
