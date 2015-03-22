package lolth.tmall.comment.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_tmall_goods_comment")
@Data
public class TmallGoodsCommentBean extends BaseBean {
	private String id;
	// 内容
	@DBField(type="varchar(400)")
	private String comment;
	// 回复
	private String replay;
	// 追加
	@DBField(type="varchar(400)")
	private String appendComment;
	// 服务
	private String serviceComment;
	private String commentTime;
	
	//user---------
	private String user;
	private String userVipLevel;
	
	private String goodsId;
	
	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallGoodsCommentBean.class) + " where id=?", DB.scaleHandler, this.id);

		if (count > 0) {
			this.log.info("TmallGoodsCommentBean is exist..");
			return;
		}

		super.persist();
	}
	
	public static void main(String[] args) throws Exception {
		new TmallGoodsCommentBean().buildTable();
	}
}
