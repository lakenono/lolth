package lolth.official.oppo.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_oppo_user")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class OppoUserBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String url;

	private String name;
	private String sex;
	private String area;
	private String groupName;

	// -----------------------

	private String education;
	private String marry;
	private String onlineTimes;
	private String regTime;

	// -----------------------
	// 积分
	private String integral;

	private String oCoins;
	// 勋章
	private String medel;
	// 成就
	private String achievement;
	// 贡献
	private String contribution;

	// oppo 积分
	private String oppoIntegral;

	public static void main(String[] args) throws Exception {
		new OppoUserBean().buildTable();
	}
}
