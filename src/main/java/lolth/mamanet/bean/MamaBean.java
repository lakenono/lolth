package lolth.mamanet.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_mamanet")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class MamaBean extends BaseBean {
	
	//标题
	private String tilte;
	//正文
	@DBField(type="text")
	private String content;
	//url
	@DBConstraintPK
	private String url;
	//发表时间
	private String publishTime;
	//搜索关键字
	private String keyword;
	//主题词
	private String name;
	
	public static void main(String[] args) throws Exception {
		new MamaBean().buildTable();
	}
}
