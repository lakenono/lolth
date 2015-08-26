package dmp.ec;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
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
	@DBField(type="varchar(1000)")
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
		String tableType = "taobao";

		// 建表时制定网站来源会建立 data_dmp_ec_taobao的表
		DBBean.createTable(ECBean.class, tableType);

		// 插入时指定网站来源 data_dmp_ec_taobao的表插入数据
		ECBean taobaoBean = new ECBean(tableType);
		taobaoBean.setId("1");
		taobaoBean.setUrl("http://www.baidu.com");
		taobaoBean.setTitle("test");
		taobaoBean.setCategory("1>2>3>4");
		taobaoBean.setKeyword("生鲜");

		taobaoBean.saveOnNotExist();
	}
}
