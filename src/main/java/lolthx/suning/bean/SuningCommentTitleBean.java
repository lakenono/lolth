package lolthx.suning.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name="data_suning_comment_title")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper=false)
public class SuningCommentTitleBean extends DBBean {
	@DBConstraintPK
	private String id;

	@DBConstraintPK
	private String projectName;
	private String keywords;
	
	private String property1;
	private String description1;
	private String property2;
	private String description2;
	
	private String labelDescriptions;
	
	private String oneStarCount;
	private String twoStarCount;
	private String threeStarCount;
	private String fourStarCount;
	private String fiveStarCount;
	private String againCount;
	private String bestCount;
	private String picFlagCount;
	private String totalCount;
	private String qualityStar;
	
	public static void main(String args[]) throws SQLException{
		DBBean.createTable(SuningCommentTitleBean.class);
	}
	
}
