package lolth.news.baidu.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name = "meta_search_news_baidu_status")
public class BaiduNewsJobStatus extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new BaiduNewsJobStatus().buildTable();
	}

	private String date;
	private String keyword;
	private String status;

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
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
