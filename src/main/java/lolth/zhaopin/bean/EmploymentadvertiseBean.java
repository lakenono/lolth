package lolth.zhaopin.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_baidu_search_bbs_zhaopin")
@Data
public class EmploymentadvertiseBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new EmploymentadvertiseBean().buildTable();
	}
	
	@DBConstraintPK
	private String url;

	private String title;
	
	@DBField(type = "text")
	private String text;

	private String keyword;
	@DBConstraintPK
	private String projectName;
}
