package lolth.muying.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_muying_commodity_comment")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class CommentBean extends BaseBean {
	public static void main(String[] args) throws SQLException {
		new CommentBean().buildTable();
	}

	/*
	 * 评论ID
	 */
	@DBConstraintPK
	private String commentId;
	/*
	 * 商品ID
	 */
	private String commodityId;
	/*
	 * 评论昵称
	 */
	private String commentNike = "";
	/*
	 * 评论时间
	 */
	private String commentTime = "";
	/*
	 * 评论评分
	 */
	private String mark = "";
	/*
	 * 评论质量
	 */
	private String commentMass = "";
	/*
	 * 评论内容
	 */
	@DBField(type = "varchar(504)")
	private String commentText = "";
}
