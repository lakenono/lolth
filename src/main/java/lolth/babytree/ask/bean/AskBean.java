package lolth.babytree.ask.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_babytree_ask")
public class AskBean extends BaseBean{
	
	public static void main(String[] args) throws SQLException {
		new AskBean().buildTable();
	}
	// url
	private String url;

	// title
	private String title;

	// date
	private String date;

	// cid
	private String cid;

	// keyword
	private String keyword;

	// -----

	// questioner 提问者
	private String questioner;

	// 提问者url
	private String questionerUrl;

	// babyStatus 宝宝状态
	private String babyStatus;

	// 回答数
	private String answerNumber;

	// 浏览数
	private String views;
	
	private String status;

	public void persist() throws SQLException {
		GlobalComponents.db.getRunner().update("insert into data_babytree_ask(url,title,date,cid,keyword) values(?,?,?,?,?)", this.url, this.title, this.date, this.cid, this.keyword);
	}

	public void update() throws SQLException {
		GlobalComponents.db.getRunner().update("update data_babytree_ask set questioner=?,questionerUrl=?,babyStatus=?,answerNumber=?,views=?,title=? where url=?", this.questioner, this.questionerUrl, this.babyStatus, this.answerNumber, this.views, this.title, this.url);
	}

	@Override
	public String toString() {
		return "AskBean [url=" + url + ", title=" + title + ", date=" + date + ", cid=" + cid + ", keyword=" + keyword + ", questioner=" + questioner + ", questionerUrl=" + questionerUrl + ", babyStatus=" + babyStatus + ", answerNumber=" + answerNumber + ", views=" + views + "]";
	}

	public String getQuestionerUrl() {
		return questionerUrl;
	}

	public void setQuestionerUrl(String questionerUrl) {
		this.questionerUrl = questionerUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getQuestioner() {
		return questioner;
	}

	public void setQuestioner(String questioner) {
		this.questioner = questioner;
	}

	public String getBabyStatus() {
		return babyStatus;
	}

	public void setBabyStatus(String babyStatus) {
		this.babyStatus = babyStatus;
	}

	public String getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(String answerNumber) {
		this.answerNumber = answerNumber;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
