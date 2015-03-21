package lolth.jd.consultation.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_jd_consultation")
public class JDGoodsConsultationBean extends BaseBean {
	private String goodsId;

	private String type;

	private String user;

	private String userUrl;

	private String ask;

	@DBField(type="varchar(500)")
	private String answer;

	private String askTime;

	private String url;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAskTime() {
		return askTime;
	}

	public void setAskTime(String askTime) {
		this.askTime = askTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public static void main(String[] args) throws Exception {
		new JDGoodsConsultationBean().buildTable();
	}

	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(JDGoodsConsultationBean.class) + " where url=?", DB.scaleHandler, url);

		if (count > 0) {
			this.log.info("JDGoodsConsultationBean is exist..");
			return;
		}
		super.persist();
	}

}
