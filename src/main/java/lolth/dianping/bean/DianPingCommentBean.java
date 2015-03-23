package lolth.dianping.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@Data
@DBTable(name = "data_dianping_comment")
public class DianPingCommentBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new DianPingCommentBean().buildTable();
	}

	// 评论id
	private String commentid;

	// 用户名
	private String username;

	// 用户url
	private String userurl;
	
	// 用户等级
	private String userlevel;

	// 用户评分
	private String rank;

	// 均价
	private String per;

	// 口味评分
	private String tasteRank;

	// 环境评分
	private String environmentRank;

	// 服务评分
	private String serviceRank;

	@DBField(type = "text")
	private String text;

	private String postTime;

	private String shopid;
	
	private String keyword;
}
