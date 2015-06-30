package lolth.hupu.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_hupu_bbs_topic")
@Data
public class TopicBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new TopicBean().buildTable();
	}

	@DBConstraintPK
	private String id;

	private String url;

	private String userId;

	private String title;

	@DBField(type = "text")
	private String text;

	private String browse;

	private String reply;
	
	private String bright;

	private String time;

	private String plate;

	private String keyword;

	@DBConstraintPK
	private String projectName;

}
