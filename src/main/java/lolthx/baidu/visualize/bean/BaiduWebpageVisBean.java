package lolthx.baidu.visualize.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_baidu_vis_webpage")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduWebpageVisBean extends DBBean {

	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	private String keyword;
	
	private String city;

	private String title;
	
	@DBField(type = "text")
	private String text;
	
	public static void main(String args[]) throws SQLException{
		DBBean.createTable(BaiduWebpageVisBean.class);
	}

}
