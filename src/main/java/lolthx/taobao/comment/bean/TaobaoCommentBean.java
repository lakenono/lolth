package lolthx.taobao.comment.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_taobao_comment")
@Data
public class TaobaoCommentBean extends BaseBean{

	@DBConstraintPK
	private String id;
	
	private String nick;
	
	private String commentTime;
	
	private String vip;
	
	private String conmment;
	
	private String reply;
	
	private String appandConmment;
	
	private String serviceComment;
	
	private String itemId;
	
	@DBConstraintPK
	private String type;
	
}
