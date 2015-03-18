package lolth.jd.comment.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name="data_jd_comment")
public class JDGoodsCommentBean extends BaseBean {
	// 用户名
	private String username;

	// 用户url
	private String userUrl;

	// 用户级别
	private String userLevel;

	// 用户地域
	private String area;

	// 评价类型
	private String type;

	// 用户评星
	private String star;

	// 用户评论时间
	private String time;

	// 用户评论url
	private String url;

	// 标签
	private String tag = "none";

	// 心得
	private String text;

	// 型号
	private String itemType;

	// 购买日期
	private String purchaseDate;

	@Override
	public String toString() {
		return "GoodsComment [username=" + username + ", userUrl=" + userUrl
				+ ", userLevel=" + userLevel + ", area=" + area + ", type="
				+ type + ", star=" + star + ", time=" + time + ", url=" + url
				+ ", tag=" + tag + ", text=" + text + ", itemType=" + itemType
				+ ", purchaseDate=" + purchaseDate + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public static void main(String[] args) throws Exception {
		new JDGoodsCommentBean().buildTable();
	}
}
