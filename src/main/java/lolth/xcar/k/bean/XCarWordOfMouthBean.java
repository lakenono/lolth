package lolth.xcar.k.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@DBTable(name="data_xcar_koubei")
@Data
@EqualsAndHashCode(callSuper=false)
public class XCarWordOfMouthBean extends BaseBean{
	
	private String title;
	
	//评价
	private String comment;
	
	//点评角度
	private String commentItem;
	
	private String content;
	
	private String from;
	
	private String usefulCount;
	
	private String postId;
	
	private String keyword;
	
	private String brandId;
	
	public static void main(String[] args) throws Exception{
		new XCarWordOfMouthBean().buildTable();
	}
}
