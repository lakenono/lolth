package lolth.hanyouzan.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_hanyouzan_goods")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class HanyouzanGoods extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new HanyouzanGoods().buildTable();
	}

	@DBConstraintPK
	private String url;
	private String name;
	private String picUrl;
	private String price;
}
