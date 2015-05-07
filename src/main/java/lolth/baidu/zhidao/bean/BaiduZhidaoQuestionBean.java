package lolth.baidu.zhidao.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_baidu_zhidao_question")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BaiduZhidaoQuestionBean extends BaseBean {
	@DBConstraintPK
	private String id;
	private String url;
	private String taskName;
	private String keyword;

	private String askTitle;
	@DBField(type="varchar(500)")
	private String askContent;

	private String askTime;
	private String askerId;

	// 详情页
	private String moneyAward;
	private String from;
	private String type;
	private String views;

	// 回答
	@DBField(type="varchar(500)")
	private String answerContent;
	private String answerTime;
	private String answererId;

	// private String answerComments;
	// 答案支持数
	private String answerSupports;
	// 答案反对数
	private String answerOppositions;

	public static void main(String[] args) throws Exception {
		new BaiduZhidaoQuestionBean().buildTable();
	}
}
