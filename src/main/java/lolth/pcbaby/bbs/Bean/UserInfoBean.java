package lolth.pcbaby.bbs.Bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_pcbaby_userinfo")
@Data
public class UserInfoBean extends BaseBean{

	public static void main(String[] args) throws SQLException {
		new UserInfoBean().buildTable();
	}
	
	@DBConstraintPK
	@DBField(type = "varchar(32)")
	private String id;
	
	private String url;
	
	//昵称
	@DBField(type = "varchar(32)")
	private String userName;
	
	//性别
	@DBField(type = "varchar(8)")
	private String sex;
	
	//年龄
	@DBField(type = "varchar(8)")
	private String age;
	
	//地址
	@DBField(type = "varchar(16)")
	private String address;
	
	//宝宝昵称
	@DBField(type = "varchar(32)")
	private String babyName;
	
	//宝宝性别
	@DBField(type = "varchar(16)")
	private String babySex;
	
	//宝宝年龄
	@DBField(type = "varchar(16)")
	private String babyAge;
	
	//宝宝生日
	@DBField(type = "varchar(32)")
	private String babyBirthday;
	
	//宝宝生肖
	@DBField(type = "varchar(8)")
	private String babyZodiac;
	
	//宝宝星座
	@DBField(type = "varchar(16)")
	private String babyConstellation;
}
