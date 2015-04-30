package lolth.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_autohome_bbs_post")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper=false)
public class AutoHomeBBSPostBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new AutoHomeBBSPostBean().buildTable();
	}

	private String jobId;

	private String title;

	private String url;
	
	private String type;

	private String author;

	private String authorUrl;

	private String postTime;

	private String views;

	private String replys;

	@DBField(type = "text")
	private String text;

	private String comment_status;

	public void update() throws SQLException
	{
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(AutoHomeBBSPostBean.class) + " set views=? ,replys=? ,text=? where url=?", this.views, this.replys, this.text, this.url);
	}

	
}
