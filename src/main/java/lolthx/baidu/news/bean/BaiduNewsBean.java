package lolthx.baidu.news.bean;

import java.sql.SQLException;

import lakenono.db.DBBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lolthx.baidu.post.bean.BaiduPostBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang.StringUtils;

@DBTable(name = "data_baidu_news")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=false)
public class BaiduNewsBean extends DBBean {
	public static void main(String[] args) throws SQLException {
		DBBean.createTable(BaiduNewsBean.class);
	}

	@DBConstraintPK
	private String id;
	
	// title
	private String title;

	// url
	private String url;

	// author
	private String author;

	// post time
	private String postTime;

	// more
	private String more = "";

	// more link
	private String moreLink = "";

	// baidu cache url
	@DBField(type = "varchar(500)")
	private String baiduCacheUrl;

	@DBField(type = "text")
	private String text;

	private String projectName;
	
	@DBConstraintPK
	private String forumId;
	
	// keyword
	private String keyword;

	// status
	private String status;

	@Override
	public String toString() {
		return "BaiduNewsBean [title=" + title + ", url=" + url + ", author=" + author + ", postTime=" + postTime + ", more=" + more + ", moreLink=" + moreLink + ", baiduCacheUrl=" + baiduCacheUrl + ", text=" + StringUtils.substring(this.text, 0, 15) + ", keyword=" + keyword + ", status=" + status + "]";
	}

}
