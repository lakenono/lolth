package lolth.weibo.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.log.BaseLog;
import lolth.weibo.task.bean.WeiboTaskBean;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

public class WeiboTaskBuilder extends BaseLog
{
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException, SQLException
	{
		//new WeiboTaskBuilder().buildDB("东风风度MX6", "20150101", "20150230");
		//new WeiboTaskBuilder().buildDB("哈弗H6", "20150101", "20150230");
		//new WeiboTaskBuilder().buildDB("奔腾X80", "20150101", "20150230");
		//new WeiboTaskBuilder().buildDB("长安CS75", "20150101", "20150230");
		//new WeiboTaskBuilder().buildDB("传祺GS5", "20150101", "20150230");
		//new WeiboTaskBuilder().buildDB("穹顶之下", "20150228", "20150310");
		//new WeiboTaskBuilder().buildDB("12369", "20150228", "20150310");
		//new WeiboTaskBuilder().buildDB("柴静", "20150228", "20150310");
		
		//
		new WeiboTaskBuilder().buildDB("香奈儿 香水", "20150101", "20150315");
		new WeiboTaskBuilder().pushMQ();
	}

	public void pushMQ() throws SQLException
	{
		// 清空
		GlobalComponents.jedis.ltrim(BaseBean.getTableName(WeiboTaskBean.class), 0, -1);

		List<WeiboTaskBean> tasks = GlobalComponents.db.getRunner().query("select * from " + BaseBean.getTableName(WeiboTaskBean.class) + " where status='todo'", new BeanListHandler<WeiboTaskBean>(WeiboTaskBean.class));

		for (WeiboTaskBean bean : tasks)
		{
			//push redis
			this.log.info("push task {}", bean.toString());
			GlobalComponents.jedis.lpush(BaseBean.getTableName(WeiboTaskBean.class), JSON.toJSONString(bean));
		}
	}

	public void buildDB(String keyword, String startTime, String endTime) throws ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException
	{
		Date startDate = DateUtils.parseDate(startTime, new String[] { "yyyyMMdd" });
		Date endDate = DateUtils.parseDate(endTime, new String[] { "yyyyMMdd" });

		while (true)
		{
			WeiboTaskBean bean = new WeiboTaskBean();
			bean.setKeyword(keyword);

			String date = DateFormatUtils.format(startDate, "yyyyMMdd");
			bean.setId(UUID.randomUUID().toString());
			bean.setStarttime(date);
			bean.setEndtime(date);
			bean.setStatus("todo");

			// save db
			bean.persist();

			startDate = DateUtils.addDays(startDate, 1);

			if (DateUtils.isSameDay(startDate, endDate))
			{
				this.log.info("keyword {} is build success.. {} {} ", keyword, startTime, endTime);
				break;
			}
		}
	}
}
