package lolth.pacuto.bbs.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_pacuto_bbs_post")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PacutoBBSPostBean extends BaseBean {
	
	@DBConstraintPK
	private String id;
	private String title;
	private String url;
	private String authorId;
	
	@DBField(type="text")
	private String content;
	//主题性质——列表页获得
	private String type;

	private String views;
	private String replys;
	
	private String postTime;
	
	private String forumId;
	@DBConstraintPK
	private String keyword;

	public static void main(String[] args) throws Exception {
		new PacutoBBSPostBean().buildTable();
	}
}
