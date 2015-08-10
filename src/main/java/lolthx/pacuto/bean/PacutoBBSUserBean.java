package lolthx.pacuto.bean;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_pacuto_bbs_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PacutoBBSUserBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	private String name;
	private String url;
	
	// 省
	private String province;
	// 市
	private String city;
	
	//关注车型
	private String concern;
	private String cars;
	
	public static void main(String[] args) throws Exception {
		DBBean.createTable(PacutoBBSUserBean.class);
	}
}
