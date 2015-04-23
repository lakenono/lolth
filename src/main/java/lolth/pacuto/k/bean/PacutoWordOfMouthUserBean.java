package lolth.pacuto.k.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_pacuto_k_post")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PacutoWordOfMouthUserBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String name;
	private String url;

	public static void main(String[] args) throws Exception {
		new PacutoWordOfMouthPostBean().buildTable();
	}
}
