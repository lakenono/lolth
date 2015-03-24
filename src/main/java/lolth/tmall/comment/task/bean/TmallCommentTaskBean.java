package lolth.tmall.comment.task.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "lakenono_task_tmall_comment")
@Data
public class TmallCommentTaskBean extends BaseBean {
	private String goodsId;
	private String url;
	private String status;

	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallCommentTaskBean.class) + " where goodsId=? and url=? ", DB.scaleHandler, this.goodsId, this.url);

		if (count > 0) {
			this.log.info("task is exist..");
			return;
		}

		super.persist();
	}

	public static void main(String[] args) throws Exception {
		new TmallCommentTaskBean().buildTable();
	}

	private void updateResult(String status) throws Exception{
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(TmallCommentTaskBean.class) + " set status=? where  goodsId=? and url=?", status, this.goodsId, this.url);
	}
	
	public void updateSuccess() throws Exception {
		updateResult("success");
	}
	
	public void updateError() throws Exception {
		updateResult("error");
	}
}
