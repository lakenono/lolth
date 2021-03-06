package lolthx.baidu.post.bean;

import lakenono.db.BaseBean;
import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_baidu_post")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduPostBean extends DBBean {
	
	@DBConstraintPK
	private String id;
	// 版块
	private String forum;
	private String title;
	private String url;
	
	@DBField(type = "text")
	private String content;
	private String userId;
	private String postTime;
	private String replys;
	@DBConstraintPK
	private String projectName;
	private String keyword;

	public static void main(String[] args) throws Exception {
		DBBean.createTable(BaiduPostBean.class);
	}

}
