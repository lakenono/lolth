package lolth.weibo.com.fetch.repost.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_sina_weibo_repost")
@Data
public class RepostBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new RepostBean().buildTable();
	}

	@DBConstraintPK
	private String weboId;

	private String userId;

	private String nick;

	private String reposts;

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
