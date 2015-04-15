package lolth.official.oppo.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_oppo_post")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class OppoPostBean extends BaseBean{
	@DBConstraintPK
	private String id;
	private String url;
	private String forum;
	private String topic;
	private String title;
	
	@DBField(type="text")
	private String content;
	private String autherId;
	private String postTime;
	
	private String replys;
	private String views;
	//赞美
	private String praise;
	//贡献
	private String collections;
	
	private String image;
	private String level;
	//加分
	private String bonusPoint;
	private String keyword;
	
	public static void main(String[] args) throws Exception{
		new OppoPostBean().buildTable();
	}
}
