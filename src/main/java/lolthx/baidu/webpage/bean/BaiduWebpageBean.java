package lolthx.baidu.webpage.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_baidu_webpage")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduWebpageBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	
	private String projectName;
	
	private String keyword;
	
	private String city;
	
	private String date;
	
	private String num;
	
	
	public static void main(String args[]) throws SQLException{
		DBBean.createTable(BaiduWebpageBean.class);
	}
	

}
