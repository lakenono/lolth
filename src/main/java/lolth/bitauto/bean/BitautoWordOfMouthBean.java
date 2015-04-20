package lolth.bitauto.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_bitauto_koubei")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BitautoWordOfMouthBean extends BaseBean {
	private String id;
	private String title;
	private String content;
	private String authorId;
	private String keyword;

	private String waiguan;
	private String waiguanComment;

	private String neishi;
	private String neishiComment;

	private String kongjian;
	private String kongjianComment;

	private String dongli;
	private String dongliComment;

	private String caokong;
	private String caokongComment;

	private String peizhi;
	private String peizhiComment;

	private String xingjiabi;
	private String xingjiabiComment;

	private String shushidu;
	private String shushiduComment;

	private String maintainCost;
	private String buyTime;
	private String price;
	private String currentMile;

	public static void main(String[] args) throws Exception {
		new BitautoWordOfMouthBean().buildTable();
	}
}
