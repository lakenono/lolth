package lolth.tmall.detail.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;
import lombok.Data;

/**
 * 天猫店铺
 * @author shi.lei
 *
 */
@DBTable(name = "data_tmall_shop")
@Data
public class TmallShopBean extends BaseBean {
	private String id;

	private String url;

	private String name;

	private String company;

	private String area;

	private String descScore;

	private String serviceScore;

	private String deliverScore;

	private String descLevel;

	private String serviceLevel;

	private String deliverLevel;
	
	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallShopBean.class) + " where id=?", DB.scaleHandler, this.id);

		if (count > 0) {
			this.log.info("TmallShopBean is exist..");
			return;
		}

		super.persist();
	}

	public static void main(String[] args) throws Exception {
		new TmallShopBean().buildTable();
	}

}
