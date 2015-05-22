package lolth.zol.bbs.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_zol_bbs_post")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class ZolBBSPostBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String title;
	private String url;
	@DBField(type = "text")
	private String content;
	private String postTime;

	// 类型：热门还是精华
	private String type;

	// 归属
	private String belong;

	private String userId;

	// 查看数
	private String views;
	// 回复数
	private String replys;

	// 是否有图
	private String hasImage;

	private String keyword;
	
	private String bbsName;

	public static void main(String[] args) throws Exception {
		new ZolBBSPostBean().buildTable();
	}

}
