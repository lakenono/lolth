package lolthx.yoka.cosmetics.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_yoka_cosmetic_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YokaCosmeticUserlBean extends DBBean{
	
	@DBConstraintPK
	private String authorId;
	
	@DBField(type = "varchar(60)")
	private String authorName;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(60)")
	private String age;//年龄
	
	@DBField(type = "varchar(60)")
	private String skin;//肤质
	
	@DBField(type = "varchar(60)")
	private String hair;//发质
	
	@DBField(type = "varchar(60)")
	private String experience;//心得的
	
	@DBField(type = "varchar(60)")
	private String essence;//精华
	
	@DBField(type = "varchar(60)")
	private String bagClassify;
	
	@DBField(type = "varchar(60)")
	private String product;
	
	@DBField(type = "varchar(60)")
	private String brand;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(YokaCosmeticUserlBean.class);
	}
	
}
