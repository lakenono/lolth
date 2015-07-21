package lolth.yhd.ask.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@DBTable(name = "data_yhd_ask_bean")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class AskBean extends BaseBean{
	//商品id
	private String goodsId;
	//评论id
	@DBConstraintPK
	private String askId;
	private String question;
	private String answer;
	private String date;
	
}
