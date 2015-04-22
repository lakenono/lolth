package lolth.bitauto.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_bitauto_bbs_post")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BitautoBBSPostBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String title;
	private String url;
	// 性质
	private String type;

	private String postTime;

	@DBField(type="text")
	private String content;

	private String authorId;
	private String forumId;

	// 查看数
	private String views;

	// 回复数
	private String replys;
	
	private String keyword;

	public static void main(String[] args) throws Exception {
		new BitautoBBSPostBean().buildTable();
	}

}
