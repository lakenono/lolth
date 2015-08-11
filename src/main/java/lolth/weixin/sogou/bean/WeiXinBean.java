package lolth.weixin.sogou.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_weixin_article")
@Data
public class WeiXinBean extends DBBean
{
	public static void main(String[] args) throws SQLException
	{
		DBBean.createTable(WeiXinBean.class);
	}

	private String title;

	@DBConstraintPK
	private String url;

	private String postTime;

	private String authorname;

	private String authorurl;

	private String authorid;

	@DBField(type = "text")
	private String text;

	private String keyword;
	
	private String projectName;

}
