package lolthx.bitauto.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_bitauto_bbs_comment")
@Data
public class BitautoBBSCommentBean extends DBBean {

	//主键ID
	private String id;

	// 楼层
	private String floor;

	// text 
	@DBField(type = "text")
	private String text;

	// 作者
	private String author;

	//作者ID
	private String authorId;
	
	// 发布时间
	private String postTime;

	// 帖子
	private String url;
	
	//帖子名称
	private String title;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(BitautoBBSCommentBean.class);
	}
}