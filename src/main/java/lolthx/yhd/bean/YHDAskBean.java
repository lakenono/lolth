package lolthx.yhd.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@DBTable(name = "data_yhd_ask_bean")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YHDAskBean extends BaseBean{
	//商品id
	private String goodsId;
	//评论id
	@DBConstraintPK
	private String askId;
	@DBField(type="varchar(500)")
	private String question;
	@DBField(type="varchar(500)")
	private String answer;
	private String date;
	private String projectName;
	
	public static void main(String[] args) throws SQLException {
		new YHDAskBean().buildTable();
	}
	
}
