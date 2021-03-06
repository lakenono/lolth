package lolthx.xcar.bean;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@DBTable(name = "data_xcar_bbs_user")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class XCarBBSUserBean extends DBBean {
	@DBConstraintPK
	private String id;
	private String name;
	private String url;
	private String sex;

	private String province;
	private String city;

	private String regTime;
	private String posts;
	private String level;

	public static void main(String[] args) throws Exception {
		DBBean.createTable(XCarBBSUserBean.class);
	}
}
