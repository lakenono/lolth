package lolth.hupu.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_hupu_username")
@Data
public class UserBean extends BaseBean {
	
	public static void main(String[] args) throws SQLException {
		new UserBean().buildTable();
	}
	
	@DBConstraintPK
	private String id;
	
	private String url;
	
	private String username;
	
	@DBField(type = "varchar(8)")
	private String sex;
	//所在地
	private String address;
	//论坛等级
	private String level;
	//卡 路 里
	private String calorie;
	//在线时间
	private String onlineTime;
	//加入时间
	private String joinedTime;
	//上次登录
	private String registering;
	//兴趣
	private String interest;
	//社区职务
	private String duty;
	//所述团队
	private String team;
	//主队
	private String homeTeam;
}
