package lolth.suning.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_suning_item_comment")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper = false)
public class SuningItemCommentBean extends BaseBean{
	
	@DBConstraintPK
	private String id;
	
	private String score;
	private String labels;
	private String content;
	private String publishTime;
	
	private String publishIp;
	private String orderTime;
	private String replyCount;
	private String usefulVoteCount;
	
	private String publisherId;
	private String itemId;
	
	public static void main(String[] args) throws Exception{
		new SuningItemCommentBean().buildTable();
	}
}
