package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.Column;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_bbs_comment")
@Data
public class AutoHomeBBSCommentBean extends DBBean {

	// 主键ID
	@DBConstraintPK
	@Column(selectColumn=true,columnAs="ID")
	private String id;

	// 楼层
	@DBConstraintPK
	@Column(selectColumn=true,columnAs="楼层")
	private String floor;

	// text
	@DBField(type = "text")
	@Column(selectColumn=true,columnAs="文本")
	private String text;

	// 作者
	@Column(selectColumn=true,columnAs="用户名称")
	private String author;

	// 作者ID
	@Column(selectColumn=true,columnAs="用户ID")
	private String authorId;

	// 发布时间
	@Column(selectColumn=true,columnAs="发布时间")
	private String postTime;

	// 帖子
	@Column(selectColumn=true,columnAs="帖子链接")
	private String url;

	// 帖子名称
	@Column(selectColumn=true,columnAs="帖子名称")
	private String title;
	
	@Column(selectColumn=true,columnAs="频道ID")
	private String forumId;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="项目名称")
	private String projectName;

	@DBField(type = "varchar(32)")
	@Column(selectColumn=true,columnAs="关键字")
	private String keyword;

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(AutoHomeBBSCommentBean.class);
	}

}
