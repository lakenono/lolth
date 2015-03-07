package lolth.weixin.sogou.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_weixin_article")
public class WeiXinBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new WeiXinBean().buildTable();
	}

	private String title;

	private String url;

	private String postTime;

	private String authorname;

	private String authorurl;

	private String authorid;

	@DBField(type = "text")
	private String text;

	private String keyword;

	@Override
	public String toString()
	{
		return "WeiXinBean [title=" + title + ", url=" + url + ", postTime=" + postTime + ", authorname=" + authorname + ", authorurl=" + authorurl + ", authorid=" + authorid + ", text=" + text + ", keyword=" + keyword + "]";
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

	public String getPostTime()
	{
		return postTime;
	}

	public void setPostTime(String postTime)
	{
		this.postTime = postTime;
	}

	public String getAuthorname()
	{
		return authorname;
	}

	public void setAuthorname(String authorname)
	{
		this.authorname = authorname;
	}

	public String getAuthorurl()
	{
		return authorurl;
	}

	public void setAuthorurl(String authorurl)
	{
		this.authorurl = authorurl;
	}

	public String getAuthorid()
	{
		return authorid;
	}

	public void setAuthorid(String authorid)
	{
		this.authorid = authorid;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

}
