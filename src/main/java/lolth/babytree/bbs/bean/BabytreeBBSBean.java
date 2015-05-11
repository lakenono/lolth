package lolth.babytree.bbs.bean;

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
	
	@DBConstraintPK
	private String id;
	//主题
	private String banner = "";
	//正文
	@DBField(type = "text")
	private String text= "";
	//用户名称
	private String nickName= "";
	//用户主页
	private String userUrl= "";
	//地域
	private String region= "";
	//浏览数
	private String browseNum= "";
	//回复数
	private String answerNum= "";
	//发表时间
	private String publishTime= "";
	//圈子归属
	private String circle= "";
	//宝宝状态
	private String babyType= "";
	//宝宝年龄
	private String babyAge= "";
	//宝宝性别
	private String babySex= "";
	
	private String keyword="";
}
