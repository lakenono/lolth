package lolth.weibo.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.NoArgsConstructor;

@DBTable(name = "data_sina_weibo_user")
@Data
@NoArgsConstructor
public class WeiboUserBean extends BaseBean {
	@DBConstraintPK
	private String id;
	// 可能是数字，可能是字母
	private String uid;
	
	private String name;
	private String area;
	private String sex;
	private String birthday;
	private String auth;
	private String bloodType;
	@DBField(type="varchar(500)")
	private String summery;

	private String tags;
	@DBConstraintPK
	private String projectName;

	public WeiboUserBean(String id) {
		this.id = id;
	}

	public static void main(String[] args) throws Exception {
		new WeiboUserBean().buildTable();
	}
}
