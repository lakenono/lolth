package lolthx.xcar.bean;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_xcar_koubei")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class XCarWordOfMouthBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	
	private String title;
	
	//评价
	private String comment;
	
	//点评角度
	private String commentItem;
	
	private String content;
	
	private String from;
	
	private String usefulCount;
	
	private String projectName;
	
	private String postId;
	
	private String formId;
	
	private String keyword;

	public static void main(String[] args) throws Exception{
		DBBean.createTable(XCarWordOfMouthBean.class);
	}
}
