package lolth.pcbaby.bbs.Bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_pcbaby_bbs_topic_reply")
@Data
public class TopicReplyBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new TopicReplyBean().buildTable();
	}
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;

	private String url;
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String topicId;

	@DBField(type = "varchar(32)")
	private String userId;

	// 楼主用户
	@DBField(type = "varchar(32)")
	private String nickName;
	
	@DBField(type = "text")
	private String text;
}
