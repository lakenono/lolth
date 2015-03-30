package lolth.baidu.post.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_baidu_post")
@Data
public class BaiduPostBean extends BaseBean {
	
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
	private String keyword;

	public static void main(String[] args) throws Exception {
		new BaiduPostBean().buildTable();
	}

}
