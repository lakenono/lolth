package lolth.xcar.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lolth.autohome.bbs.bean.AutoHomeBBSPostBean;

@DBTable(name = "data_xcar_bbs_post")
public class XCarBBSPostBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new XCarBBSPostBean().buildTable();
	}

	private String jobname;

	private String title;

	private String url;

	private String author;

	private String authorUrl;

	private String postTime;

	private String views;

	private String replys;

	@DBField(type = "text")
	private String text;

	private String comment_status;

	@Override
	public String toString()
	{
		return "XCarBBSBean [jobname=" + jobname + ", title=" + title + ", url=" + url + ", author=" + author + ", authorUrl=" + authorUrl + ", postTime=" + postTime + ", views=" + views + ", replys=" + replys + ", text=" + text + "]";
	}

	public void update() throws SQLException
	{
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(XCarBBSPostBean.class) + " set text=? where url=?", this.text, this.url);
	}

	public String getComment_status()
	{
		return comment_status;
	}

	public void setComment_status(String comment_status)
	{
		this.comment_status = comment_status;
	}

	public String getJobname()
	{
		return jobname;
	}

	public void setJobname(String jobname)
	{
		this.jobname = jobname;
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

}
