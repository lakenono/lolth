package lolth.zol.bbs.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name="data_zol_bbs_user")
@Data
public class ZolBBSUserBean extends BaseBean{
	@DBConstraintPK
	private String id;
	private String name;
	private String url;
	private String level;
	private String sex;
	private String from;
	private String marry;
	private String birthday;
	private String regTime;
	
	public static void main(String[] args) throws Exception {
		new ZolBBSUserBean().buildTable();
	}
}
