package lolth.baidu.post.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name="data_baidu_post_user")
@Data
public class BaiduPostUserBean extends BaseBean{
	
	@DBConstraintPK
	private String id;
	private String name;
	private String sex;
	private String url;
	private String postAge;
	private String posts;
	
	public static void main(String[] args) throws SQLException {
		new BaiduPostUserBean().buildTable();
	}
}
