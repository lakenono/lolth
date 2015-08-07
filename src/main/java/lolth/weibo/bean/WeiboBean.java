package lolth.weibo.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_sina_weibo")
@Data
public class WeiboBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new WeiboBean().buildTable();
	}

	// 数据字段
	private String id;
	@DBConstraintPK
	private String mid;
	
	private String weibourl;
	@DBField(type="varchar(500)")
	private String text;
	private String postTime;
	private String source;
	private String username;
	private String userid;
	private String userurl;
	private String reposts;
	private String comments;
	private String likes;
	private String pid;
	private String pmid;
	private String pweibourl;
	
	private String keyword;
	
	@DBConstraintPK
	private String projectName;
	
	private String fetchTime;
	
	private String at;
	//话题
	private String topic;
	//转发
	private String mainText;
	//转发列表
	private String forwardList;
	//表情
	private String feelings;
	
}
