package lolth.weibo.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_sina_weibo_user")
public class WeiboUserBean extends BaseBean {
	private String userId;
	private String tags;

	public WeiboUserBean() {

	}

	public WeiboUserBean(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public static void main(String[] args) throws Exception {
		new WeiboUserBean().buildTable();
	}

	@Override
	public void persist() throws IllegalArgumentException,
			IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query(
				"select count(*) from "
						+ BaseBean.getTableName(WeiboUserBean.class)
						+ " where userId=?", DB.scaleHandler,
				userId);

		if (count > 0) {
			this.log.info("task is exist..");
			return;
		}
		super.persist();
	}
}
