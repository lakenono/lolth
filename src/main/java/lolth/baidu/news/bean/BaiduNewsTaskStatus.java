package lolth.baidu.news.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

@DBTable(name = "lakenono_task_baidu_news")
public class BaiduNewsTaskStatus extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new BaiduNewsTaskStatus().buildTable();
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
