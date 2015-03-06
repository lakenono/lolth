package lolth.baidu.news.bean;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

@DBTable(name = "meta_search_news_baidu")
public class BaiduNewsBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new BaiduNewsBean().buildTable();
	}

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
	private String baiduCacheUrl;

	@DBField(type = "text")
	private String text;

	// keyword
	private String keyword;

	// status
	private String status;

	@Override
	public String toString()
	{
		return "BaiduNewsBean [title=" + title + ", url=" + url + ", author=" + author + ", postTime=" + postTime + ", more=" + more + ", moreLink=" + moreLink + ", baiduCacheUrl=" + baiduCacheUrl + ", text=" + StringUtils.substring(this.text, 0, 15) + ", keyword=" + keyword + ", status=" + status + "]";
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getPostTime()
	{
		return postTime;
	}

	public void setPostTime(String postTime)
	{
		this.postTime = postTime;
	}

	public String getMore()
	{
		return more;
	}

	public void setMore(String more)
	{
		this.more = more;
	}

	public String getMoreLink()
	{
		return moreLink;
	}

	public void setMoreLink(String moreLink)
	{
		this.moreLink = moreLink;
	}

	public String getBaiduCacheUrl()
	{
		return baiduCacheUrl;
	}

	public void setBaiduCacheUrl(String baiduCacheUrl)
	{
		this.baiduCacheUrl = baiduCacheUrl;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
