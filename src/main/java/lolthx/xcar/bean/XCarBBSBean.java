package lolthx.xcar.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_xcar_bbs")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class XCarBBSBean extends DBBean {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(XCarBBSBean.class);
	}

	@DBConstraintPK
	private String id;

//	private String jobname;

	private String title;

	private String url;

	private String authorId;

//	private String authorUrl;

	private String postTime;

	private String views;

	private String replys;

	@DBField(type = "text")
	private String text;

//	private String comment_status;
	
	private String projectName;
	
	private String keyword;
	
	private String forumId;


}
