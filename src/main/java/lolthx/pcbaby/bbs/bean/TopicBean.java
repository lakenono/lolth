package lolthx.pcbaby.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_pcbaby_bbs_topic")
@Data
public class TopicBean extends BaseBean{

	public static void main(String[] args) throws SQLException {
		new TopicBean().buildTable();
	}
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	
	private String url;
	
	//帖子标题
	private String title;
	
	//帖子正文
	@DBField(type = "text")
	private String text;
	
	//帖子查看
	@DBField(type = "varchar(32)")
	private String views;
	
	//帖子回复
	@DBField(type = "varchar(32)")
	private String reply;
	
	//楼主id
	@DBField(type = "varchar(32)")
	private String userId;
	
	//楼主用户
	@DBField(type = "varchar(32)")
	private String nickName;
	
	//楼主发帖时间
	@DBField(type = "varchar(32)")
	private String postTime;
	
	@DBField(type = "varchar(32)")
	private String keyword;
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;
}
