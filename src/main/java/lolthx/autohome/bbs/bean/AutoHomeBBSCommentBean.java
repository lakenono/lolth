package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_bbs_comment")
@Data
public class AutoHomeBBSCommentBean extends DBBean {

	// 主键ID
	@DBConstraintPK
	private String id;

	// 楼层
	private String floor;

	// text
	@DBField(type = "text")
	private String text;

	// 作者
	private String author;

	// 作者ID
	private String authorId;

	// 发布时间
	private String postTime;

	// 帖子
	private String url;

	// 帖子名称
	private String title;

	@DBConstraintPK
	private String forumId;

	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

	@DBField(type = "varchar(32)")
	private String keyword;

	public static void main(String[] args) throws SQLException {
		DBBean.createTable(AutoHomeBBSCommentBean.class);
	}

}
