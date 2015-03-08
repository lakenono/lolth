package lolth.weibo.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.log.BaseLog;
import lolth.weibo.task.bean.WeiboTaskBean;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

public class WeiboTaskBuilder extends BaseLog
{
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException, SQLException
	{
		new WeiboTaskBuilder().build("东风风度MX6", "20150101", "20150230");
		new WeiboTaskBuilder().build("哈弗H6", "20150101", "20150230");
		new WeiboTaskBuilder().build("奔腾X80", "20150101", "20150230");
		new WeiboTaskBuilder().build("长安CS75", "20150101", "20150230");
		new WeiboTaskBuilder().build("传祺GS5", "20150101", "20150230");
	}

	public void build(String keyword, String startTime, String endTime) throws ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException
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

			// push redis
			GlobalComponents.jedis.lpush(BaseBean.getTableName(WeiboTaskBean.class), JSON.toJSONString(bean));

			startDate = DateUtils.addDays(startDate, 1);

			if (DateUtils.isSameDay(startDate, endDate))
			{
				this.log.info("keyword {} is build success.. {} {} ", keyword, startTime, endTime);
				break;
			}
		}
	}
}
