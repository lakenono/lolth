package lolthx.baidu.visualize.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;



@DBTable(name = "data_baidu_vis_news")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduNewsVisBean extends DBBean {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(BaiduNewsVisBean.class);
	}
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	// title
	private String title;

	// author
	private String author;

	// post time
	private String postTime;


	@DBField(type = "text")
	private String text;

	
	private String city;
	
	// keyword
	private String keyword;



}
