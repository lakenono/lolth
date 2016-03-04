package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.Column;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_bbs")
@Data
public class AutoHomeBBSBean extends DBBean {

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(AutoHomeBBSBean.class);
	}

	@DBConstraintPK
	@Column(selectColumn=true,columnAs="ID")
	private String id;
	
	@Column(selectColumn=true,columnAs="标题")
	private String title;
	
	@Column(selectColumn=true,columnAs="链接")
	private String url;
	
	@Column(selectColumn=true,columnAs="类型")
	private String type;
	
	@Column(selectColumn=true,columnAs="用户")
	private String author;
	
	@Column(selectColumn=true,columnAs="用户ID")
	private String authorId;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="发布时间")
	private String postTime;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="点击量")
	private String views;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="回复")
	private String replys;

	@DBField(type = "text")
	@Column(selectColumn=true,columnAs="文本")
	private String text;

	@DBConstraintPK
	@Column(selectColumn=true,columnAs="频道ID")
	private String forumId;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="项目名称")
	private String projectName;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="关键字")
	private String keyword;

}
