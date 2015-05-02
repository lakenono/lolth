package lolth.weibo.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.log.BaseLog;
import lolth.weibo.cn.WeiboRealTimeFetch;
import lolth.weibo.cn.WeiboSearchFetch;
import lolth.weibo.task.bean.WeiboTaskBean;

import com.alibaba.fastjson.JSON;

public class WeiboTaskRunner extends BaseLog
{
	public static void main(String[] args) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException, IOException, ParseException, InterruptedException
	{
		new WeiboTaskRunner().run();
	}

	WeiboSearchFetch fetch = new WeiboSearchFetch();

	public void run() throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException, IOException, ParseException, InterruptedException
	{
		while (true)
		{
			try
			{
				WeiboTaskBean task = this.getTask();

				if (task == null)
				{
					this.log.info("等待新任务...");
					Thread.sleep(10*60*1000);
				}

				if (!this.isFinish(task))
				{
					WeiboRealTimeFetch fetch = new WeiboRealTimeFetch(task.getKeyword(), task.getStarttime(), task.getEndtime());
					fetch.run();
					this.setFinish(task);
				}
			}
			catch (Exception e)
			{
				this.log.error("", e);
			}
		}
	}

	public WeiboTaskBean getTask()
	{
		String json = GlobalComponents.jedis.rpop(BaseBean.getTableName(WeiboTaskBean.class));

		if (json == null)
		{
			return null;
		}

		WeiboTaskBean bean = JSON.parseObject(json, WeiboTaskBean.class);
		return bean;
	}

	public boolean isFinish(WeiboTaskBean task) throws SQLException
	{
		String id = task.getId();
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(WeiboTaskBean.class) + " where id=? and status='success'", DB.scaleHandler, id);

		if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setFinish(WeiboTaskBean task) throws SQLException
	{
		this.log.info("任务完成... {}", task.toString());
		this.log.info("update " + BaseBean.getTableName(WeiboTaskBean.class) + " set status='success' where id=?", task.getId());
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(WeiboTaskBean.class) + " set status='success' where id=?", task.getId());
	}
}
