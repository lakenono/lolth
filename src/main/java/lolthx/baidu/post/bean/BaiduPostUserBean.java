package lolthx.baidu.post.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_baidu_post_user")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduPostUserBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	private String name;
	private String sex;
	private String url;
	private String postAge;
	private String posts;
	
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(BaiduPostUserBean.class);
	}
}
