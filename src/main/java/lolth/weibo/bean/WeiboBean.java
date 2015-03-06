package lolth.weibo.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name = "sns_sinaweibo_weibo_list")
public class WeiboBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new WeiboBean().buildTable();
	}

	// 数据字段
	private String id;
	private String mid;
	private String text;
	private String postTime;
	private String source;
	private String username;
	private String userurl;
	private String geo;
	private String reposts;
	private String comments;
	private String likes;
	private String pid;

	// 业务字段
	private String keyword;

	@Override
	public String toString()
	{
		return "WeiboBean [id=" + id + ", mid=" + mid + ", text=" + text + ", postTime=" + postTime + ", source=" + source + ", username=" + username + ", userurl=" + userurl + ", geo=" + geo + ", reposts=" + reposts + ", comments=" + comments + ", likes=" + likes + ", pid=" + pid + ", keyword=" + keyword + "]";
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getPostTime()
	{
		return postTime;
	}

	public void setPostTime(String postTime)
	{
		this.postTime = postTime;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserurl()
	{
		return userurl;
	}

	public void setUserurl(String userurl)
	{
		this.userurl = userurl;
	}

	public String getGeo()
	{
		return geo;
	}

	public void setGeo(String geo)
	{
		this.geo = geo;
	}

	public String getReposts()
	{
		return reposts;
	}

	public void setReposts(String reposts)
	{
		this.reposts = reposts;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getLikes()
	{
		return likes;
	}

	public void setLikes(String likes)
	{
		this.likes = likes;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
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
