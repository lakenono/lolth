package lolth.babytree.ask.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;
@DBTable(name = "data_babytree_ask_answer")
public class AnswerBean extends BaseBean{
	public static void main(String[] args) throws SQLException {
		new AnswerBean().buildTable();
	}
	// url
	private String url;

	// floor
	private String floor;

	// author
	private String author;

	// author url
	private String authorUrl;

	// babyStatus
	private String babyStatus = "还没有宝宝";

	// postTime
	private String postTime;

	public void persist() throws SQLException {
		GlobalComponents.db.getRunner().update("insert into data_babytree_ask_answer(url,floor,author,authorUrl,babyStatus,postTime) values(?,?,?,?,?,?)", this.url, this.floor, this.author, this.authorUrl, this.babyStatus, this.postTime);
	}

	@Override
	public String toString() {
		return "AnswerBean [url=" + url + ", floor=" + floor + ", author=" + author + ", authorUrl=" + authorUrl + ", babyStatus=" + babyStatus + ", postTime=" + postTime + "]";
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBabyStatus() {
		return babyStatus;
	}

	public void setBabyStatus(String babyStatus) {
		this.babyStatus = babyStatus;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

}
