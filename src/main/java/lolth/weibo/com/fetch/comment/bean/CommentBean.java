package lolth.weibo.com.fetch.comment.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_sina_weibo_comment")
@Data
public class CommentBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new CommentBean().buildTable();
	}

	@DBConstraintPK
	private String commentId;

	private String userId;

	private String nick;

	private String like;

	@DBField(type = "varchar(500)")
	private String text;
	@DBConstraintPK
	private String keyword;

	private String postTime = "";

	private String at;
	// 话题
	private String topic;
	// 转发
	private String mainText;
	// 转发列表
	private String forwardList;
	// 表情
	private String feelings = "";
}
