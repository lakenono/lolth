package lolth.xcar.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_xcar_bbs_post")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class XCarBBSPostBean extends BaseBean {
	public static void main(String[] args) throws SQLException {
		new XCarBBSPostBean().buildTable();
	}

	@DBConstraintPK
	private String id;

//	private String jobname;

	private String title;

	private String url;

	private String authorId;

//	private String authorUrl;

	private String postTime;

	private String views;

	private String replys;

	@DBField(type = "text")
	private String text;

//	private String comment_status;
	
	private String keyword;
	
	private String brandId;

	public void update() throws SQLException {
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(XCarBBSPostBean.class) + " set text=? where url=?", this.text, this.url);
	}

}
