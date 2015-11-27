package lolthx.zhihu.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_zhihu_kw_question")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ZhihuKwQuestionBean extends DBBean{
	
	@DBConstraintPK
	private String id;
	
	@DBConstraintPK
	private String projectName;
	
	@DBConstraintPK
	private String keyword;
	
	@DBField(type = "varchar(300)")
	private String url;
	
	@DBField(type = "varchar(300)")
	private String title;
	
	@DBField(type = "text")
	private String content;
	
	@DBField(type = "varchar(150)")
	private String labels;
	
	@DBField(type = "varchar(50)")
	private String replys;
	
	@DBField(type = "varchar(50)")
	private String answers;
	
	@DBField(type = "varchar(50)")
	private String likes;
	
	public static void main(String[] args) throws SQLException{
		DBBean.createTable(ZhihuKwQuestionBean.class);
	}
	
	
}
