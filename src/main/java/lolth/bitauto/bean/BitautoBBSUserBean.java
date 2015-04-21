package lolth.bitauto.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_bitauto_bbs_user")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BitautoBBSUserBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String name;
	private String url;

	// 省
	private String province;
	// 市
	private String city;
	
	//注册时间
	private String regTime;
	
	//-------------可变数据-----------------
	//等级
	private String level;
	//帖子数
	private String posts;
	//精华数
	private String elites;
	//车型
	private String car;
	
	public static void main(String[] args)throws Exception{
		new BitautoBBSUserBean().buildTable();
	}

}
