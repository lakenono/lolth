package lolthx.baidu.post.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_baidu_post_user_kw")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduPostUserBykwBean  extends DBBean{

	@DBConstraintPK
	private String id;
	private String name;
	private String sex;
	private String url;
	private String postAge;
	private String posts;
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(BaiduPostUserBykwBean.class);
	}

}
