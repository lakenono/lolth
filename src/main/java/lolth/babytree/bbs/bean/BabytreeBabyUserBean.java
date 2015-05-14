package lolth.babytree.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_babytree_baby_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BabytreeBabyUserBean extends BaseBean {
	public static void main(String[] args) throws SQLException {
		new BabytreeBabyUserBean().buildTable();
	}
	//id
	@DBConstraintPK
	private String userId = "";
	// 用户名称
	private String nickName = "";
	// 用户url
	private String userUrl = "";
	// 地域
	private String region = "";
	// 宝宝状态
	private String babyType = "";
	// 宝宝年龄
	private String babyAge = "";
	// 宝宝性别
	private String babySex = "";
}
