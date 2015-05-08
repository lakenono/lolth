package lolth.suning.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_suning_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class SuningUserBean extends BaseBean {
	@DBConstraintPK
	private String id;

	private String nickName;
	private String province;
	private String birthday;
	private String gender;
	private String levelId;
	private String levelName;

	public static void main(String[] args) throws Exception {
		new SuningUserBean().buildTable();
	}
}
