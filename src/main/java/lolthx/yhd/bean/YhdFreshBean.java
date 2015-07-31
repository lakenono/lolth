package lolthx.yhd.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yhd_fresh")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YhdFreshBean extends BaseBean{
	
	@DBConstraintPK
	private String id;
	private String url;
	private String name;
	
	public static void main(String[] args) throws SQLException {
		new YhdFreshBean().buildTable();
	}
}
