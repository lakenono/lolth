package lolthx.alexa.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_alexa_url")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class AlexaUrlBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	
	private String projectName;
	
	private String keyword;
	
	private String url;
	
	private String w0; //当日排名
	
	private String w1;//排名变化趋势
	
	private String w2;//
	
	private String w3;//
	
	private String w4;//
	
	private String w5;//
	
	private String w6;//
	
	private String w7;//
	
	private String w8;//
	
	private String w9;//
	
	private String w10;//
	
	private String w11;//
	
	private String w12;//
	
	private String w13;//
	
	private String w14;//
	
	private String w15;//
	
	public static void main(String args[]) throws SQLException{
		DBBean.createTable(AlexaUrlBean.class);
	}

}
