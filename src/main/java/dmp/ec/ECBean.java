package dmp.ec;

import lakenono.db.DBBean;
import lakenono.db.DMPBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@DBTable(name = "data_dmp_ec")
@Data
@EqualsAndHashCode(callSuper = false)
public class ECBean extends DBBean {
	// 商品ID
	@DBConstraintPK
	private String id;
	// 商品url
	private String url;
	// 商品名称
	private String title;
	// 商品类别
	private String category;
	// 提取关键字
	@DBConstraintPK
	private String keyword;

	public ECBean(String tableKey) {
		super(tableKey);
	}

	public static void main(String[] args) throws Exception {

		// 建表时制定网站来源会建立 data_dmp_ec_taobao的表
		DBBean.createTable(DMPBean.class, "taobao");

		// 插入时指定网站来源 data_dmp_ec_taobao的表插入数据
		ECBean taobaoBean = new ECBean("taobao");
		taobaoBean.setId("1");
		taobaoBean.setUrl("舍HI");
		taobaoBean.setTitle("t");
		taobaoBean.setCategory("123>123>1");

		taobaoBean.saveOnNotExist();
	}
}
