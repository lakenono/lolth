package lolthx.weibo.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeiboSearchByTimeTask {
	public static void main(String[] args) throws  Exception {
		String[] keys = { "旅游", "郊游", "拓展训练", "亲子游", "农家乐", "休闲游", "农业观光游", "度假地产", "远郊地产", "度假楼盘", "远郊楼盘", "度假小区", "远郊小区", "远郊地区", "远郊区域", "房山" };
		String projectName = "trip";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		try {
			start.setTime(format.parse("20141016"));
			end.setTime(format.parse("20151016"));
		} catch (ParseException e) {
			log.error("parse time is error:{}", e.getMessage());
		}

		while (start.before(end)) {
			String endTime = format.format(end.getTime());
			for (String key : keys) {
				new WeiboSearchTask(projectName, key, endTime, endTime).run();
			}
			log.info("{}时间任务处理完成",endTime);
			end.add(Calendar.DAY_OF_MONTH, -1);
		}
	}
}
