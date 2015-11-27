package lolthx.zhihu.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_zhihu_kw_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ZhihuKwUserBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String authorName;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(300)")
	private String  selfIntroduction;
	
	@DBField(type = "varchar(200)")
	private String address;
	
	@DBField(type = "varchar(200)")
	private String industry;//行业
	
	@DBField(type = "varchar(200)")
	private String university;
	
	@DBField(type = "varchar(200)")
	private String department;
	
	@DBField(type = "varchar(300)")
	private String brief;//简介
	
	@DBField(type = "varchar(50)")
	private String approval;//赞同
	
	@DBField(type = "varchar(50)")
	private String thanks;//感谢
	
	@DBField(type = "varchar(50)")
	private String questions;//问题
	
	@DBField(type = "varchar(50)")
	private String answers;//回答
	
	@DBField(type = "varchar(50)")
	private String article;//文章
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(ZhihuKwUserBean.class);
	}
	
}
