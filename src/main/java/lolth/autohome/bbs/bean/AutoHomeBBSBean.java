package lolth.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_autohome_bbs_list")
public class AutoHomeBBSBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new AutoHomeBBSBean().buildTable();
	}

	private String jobId;

	private String title;

	private String url;

	private String author;

	private String authorUrl;

	private String postTime;

	private String views;

	private String replys;

	@DBField(type = "text")
	private String text;

	@Override
	public String toString()
	{
		return "AutoHomeBBSBean [title=" + title + ", url=" + url + ", author=" + author + ", authorUrl=" + authorUrl + ", postTime=" + postTime + ", views=" + views + ", replys=" + replys + ", text=" + text + "]";
	}

	public void update() throws SQLException
	{
		GlobalComponents.db.getRunner().update("update sns_autohome_bbs set views=? ,replys=? ,text=? where url=?", this.views, this.replys, this.text, this.url);
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getViews()
	{
		return views;
	}

	public void setViews(String views)
	{
		this.views = views;
	}

	public String getReplys()
	{
		return replys;
	}

	public void setReplys(String replys)
	{
		this.replys = replys;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
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

	public String getAuthorUrl()
	{
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl)
	{
		this.authorUrl = authorUrl;
	}

	public String getPostTime()
	{
		return postTime;
	}

	public void setPostTime(String postTime)
	{
		this.postTime = postTime;
	}

}
