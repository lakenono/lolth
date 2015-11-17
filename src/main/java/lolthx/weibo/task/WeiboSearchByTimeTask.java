package lolthx.weibo.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;

@Slf4j
public class WeiboSearchByTimeTask {
	public static final String weiboSearchByTimeTaskQueue = "Weibo_Search_By_Time_Task";

	public static void doMaster() throws Exception {
		String[] keys = { "小米", "魅族" };
		String projectName = "test";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		try {
			start.setTime(format.parse("20151012"));
			end.setTime(format.parse("20151016"));
		} catch (ParseException e) {
			log.error("parse time is error:{}", e.getMessage());
		}

		// 推送任务
		while (start.before(end)) {
			String endTime = format.format(end.getTime());
			for (String key : keys) {
				// 修改成推送队列，然后获取抓取的情况
				// new WeiboSearchTask(projectName, key, endTime, endTime)
				WeiboSearchTask task = new WeiboSearchTask(projectName, key, endTime, endTime);
				GlobalComponents.redisAPI.lpush(weiboSearchByTimeTaskQueue, JSON.toJSONString(task));

				// Thread.sleep(15000);
			}
			log.info("{} 时间任务处理完成", endTime);
			end.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	/**
	 * master
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length > 0 && "master".equals(args[0])) {
			log.info("this is master !");
			doMaster();
			log.info("master push task finish! ");
		} else {
			log.info("this is slave !");
		}

		// 获取任务
		while (true) {
			String json = GlobalComponents.redisAPI.rpop(weiboSearchByTimeTaskQueue);
			if (json == null) {
				log.info("push task finish!");
				return;
			}

			WeiboSearchTask task = JSON.parseObject(json, WeiboSearchTask.class);
			task.run();
			Thread.sleep(15000);
		}
	}

}
