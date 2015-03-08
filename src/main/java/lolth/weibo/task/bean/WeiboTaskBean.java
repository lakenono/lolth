package lolth.weibo.task.bean;

import java.sql.SQLException;
import java.text.ParseException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.db.annotation.DBTable;

@DBTable(name = "lakenono_task_weibo")
public class WeiboTaskBean extends BaseBean
{
	public static void main(String[] args) throws SQLException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		new WeiboTaskBean().buildTable();
	}

	private String id;
	private String keyword;
	private String starttime;
	private String endtime;
	private String status;

	@Override
	public void persist() throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException
	{
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(WeiboTaskBean.class) + " where keyword=? and starttime=? and endtime=?", DB.scaleHandler, this.keyword, this.starttime, this.endtime);

		if (count > 0)
		{
			this.log.info("task is exist..");
			return;
		}

		super.persist();
	}

	@Override
	public String toString()
	{
		return "WeiboTaskBean [id=" + id + ", keyword=" + keyword + ", starttime=" + starttime + ", endtime=" + endtime + ", status=" + status + "]";
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
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
