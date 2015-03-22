package lolth.tmall.search.task.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "lakenono_task_tmall_search_list")
@Data
public class TmallSearchListTaskBean extends BaseBean {
	private String keyword;
	private String type;
	private String url;
	private String status;

	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallSearchListTaskBean.class) + " where keyword=? and url=? and type=?", DB.scaleHandler, this.keyword, this.url, this.type);

		if (count > 0) {
			this.log.info("task is exist..");
			return;
		}

		super.persist();
	}

	public static void main(String[] args) throws Exception {
		new TmallSearchListTaskBean().buildTable();
	}

	public void updateSuccess() throws Exception {
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(TmallSearchListTaskBean.class) + " set status=? where keyword=? and url=?", "success", this.keyword, this.url);
	}

}
