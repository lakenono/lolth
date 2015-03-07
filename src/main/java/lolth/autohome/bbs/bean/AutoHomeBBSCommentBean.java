package lolth.autohome.bbs.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_autohome_bbs_comment")
public class AutoHomeBBSCommentBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new AutoHomeBBSCommentBean().buildTable();
	}

	// 楼层
	private String floor;

	// text 
	private String text;

	// 作者
	private String author;

	// 发布时间
	private String postTime;

	// 帖子
	private String url;

	@Override
	public String toString()
	{
		return "AutoHomeBBSCommentBean [floor=" + floor + ", text=" + text + ", author=" + author + ", postTime=" + postTime + ", url=" + url + "]";
	}

	public String getFloor()
	{
		return floor;
	}

	public void setFloor(String floor)
	{
		this.floor = floor;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
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

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
