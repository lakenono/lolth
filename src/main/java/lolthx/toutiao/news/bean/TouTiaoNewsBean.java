package lolthx.toutiao.news.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_toutiao_news")
@Data
public class TouTiaoNewsBean extends BaseBean {

	public static void main(String[] args) throws SQLException {
		new TouTiaoNewsBean().buildTable();
	}

	@DBConstraintPK
	@DBField(type = "varchar(128)")
	private String id;

	//帖子标题
	private String title;
	
	//帖子正文
	@DBField(type = "text")
	private String text;

	private String url;

	private String seoUrl;

	@DBField(type = "varchar(32)")
	private String datetime;
	@DBField(type = "varchar(32)")
	private String keyword;
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String projectName;

	public void update() throws SQLException {
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(TouTiaoNewsBean.class) + " set text=? where seoUrl=?", this.text, this.seoUrl);
	}
}
