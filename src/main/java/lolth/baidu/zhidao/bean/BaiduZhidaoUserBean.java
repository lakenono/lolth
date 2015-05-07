package lolth.baidu.zhidao.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_baidu_zhidao_user")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BaiduZhidaoUserBean extends BaseBean {
	@DBConstraintPK
	private String name;
	private String url;
	private String sex;
	private String birtyday;
	private String birtyAddress;
	private String bloodType;
	private String liveAddress;

	public static void main(String[] args) throws Exception {
		new BaiduZhidaoUserBean().buildTable();
	}
}
