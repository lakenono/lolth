package lolthx.weixin.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_weixin_article")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class WeiXinBean extends DBBean {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(WeiXinBean.class);
	}
	
	@DBConstraintPK
	private String id;
	
	private String title;

	private String url;

	private String postTime;

	private String authorname;

	private String authorurl;

	private String authorid;

	@DBField(type = "text")
	private String text;
	
	@DBConstraintPK
	private String projectName;
	
	private String keyword;

	@Override
	public String toString() {
		return "WeiXinBean [id=" + id + ",title=" + title + ", url=" + url + ", postTime=" + postTime + ", authorname=" + authorname + ", authorurl=" + authorurl + ", authorid=" + authorid + ", text=" + text + ", keyword=" + keyword + "]";
	}
}
