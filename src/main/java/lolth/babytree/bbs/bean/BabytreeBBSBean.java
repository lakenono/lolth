package lolth.babytree.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/*
 * 宝宝树的bean
 */
@DBTable(name = "data_babytree_bbs")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BabytreeBBSBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new BabytreeBBSBean().buildTable();
	}
	@DBConstraintPK
	private String id;
	//用户ID
	private String userId;
	//主题
	private String banner = "";
	//正文
	@DBField(type = "text")
	private String text= "";
	//帖子url
	private String topicUrl="";
	//浏览数
	private String browseNum= "";
	//回复数
	private String answerNum= "";
	//发表时间
	private String publishTime= "";
	//圈子归属
	private String circle= "";
	
	private String keyword="";
	
	private String subjectTask="";
}
