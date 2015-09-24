package lolthx.suning.bean;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_suning_item_comment")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper = false)
public class SuningItemCommentBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	@DBConstraintPK
	private String projectName;
	private String keyword;

	private String content;
	private String publishTime;
	
	private String qualityStar;
	private String shopName;
	private String commodityName;
	
	
	private String userName;
	private String userLevelId;
	private String userLevelName;
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		DBBean.createTable(SuningItemCommentBean.class);
	}
}
