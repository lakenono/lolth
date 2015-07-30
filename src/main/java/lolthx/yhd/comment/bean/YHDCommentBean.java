package lolthx.yhd.comment.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * 一号店商品评论
 * @author yanghp
 *
 */
@DBTable(name = "data_yhd_comment_bean")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class YHDCommentBean extends BaseBean{
	//商品id
	private String goodsId;
	//评论id
	@DBConstraintPK
	private String commentId;
	private String score;
	@DBField(type="varchar(500)")
	private String content;
	@DBField(type="varchar(500)")
	private String repley;
	private String date;
	
	public static void main(String[] args) throws SQLException {
		new YHDCommentBean().buildTable();
	}
}
