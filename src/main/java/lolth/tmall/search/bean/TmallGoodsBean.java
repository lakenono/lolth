package lolth.tmall.search.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;
import lombok.Data;

@DBTable(name = "data_tmall_goods")
@Data
public class TmallGoodsBean extends BaseBean {
	private String id;
	private String title;
	private String url;
	private String keyword;
	private String price;
	private String monthSales;
	private String comments;
	private String shopId;

	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallGoodsBean.class) + " where id=? and keyword=?", DB.scaleHandler, this.id, this.keyword);

		if (count > 0) {
			this.log.info("TmallGoodsBean is exist..");
			return;
		}

		super.persist();
	}

	public static void main(String[] args) throws Exception {
		new TmallGoodsBean().buildTable();
	}
}
