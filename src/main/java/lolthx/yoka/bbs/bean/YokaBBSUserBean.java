package lolthx.yoka.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_bbs_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaBBSUserBean extends DBBean{
	
	@DBConstraintPK
	private String authorId;
	
	private String authorName;
	
	private String url;
	
	private String work;
	
	private String currency;
	
	private String honor;
	
	private String address;
	
	private String attention;
	
	private String fans;
	
	private String post;
	
	private String essence;
	
	private String registTime;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaBBSUserBean.class);
	}
	
}
