package lolthx.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_autohome_bbs")
@Data
public class AutoHomeBBSBean extends BaseBean{

	public static void main(String[] args) throws SQLException {
		new AutoHomeBBSBean().buildTable();
	}
	
	@DBConstraintPK
	private String id;
	
	private String title;

	private String url;
	
	private String type;
	
	private String author;
	
	private String authorId;
	
	@DBField(type = "varchar(32)")
	private String postTime;

	@DBField(type = "varchar(32)")
	private String views;

	@DBField(type = "varchar(32)")
	private String replys;

	@DBField(type = "text")
	private String text;
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;
	
	@DBField(type = "varchar(32)")
	private String keyword;
	
	public void update() throws SQLException{
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(AutoHomeBBSBean.class) + " set views=? ,replys=? ,text=? where id=?", this.views, this.replys, this.text, this.id);
	}
}
