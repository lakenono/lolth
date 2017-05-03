package lolthx.weibo.task;

import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSON;

@Slf4j
public class WeiboSearchByTimeTask {
	public static final String weiboSearchByTimeTaskQueue = "Weibo_Search_By_Time_Task";

	public static void doMaster() throws Exception {
		String[] keys = { "中国国际航空", "海南航空", "东方航空", "南方航空", "深圳航空", "厦门航空", "四川航空", "河北航空", "上海航空", "春秋航空", "吉祥航空", "天津航空", "山东航空", "青岛航空", "北部湾航空", "大新华航空", "长龙航空", "成都航空", "大连航空", "重庆航空", "中国邮政航空", "香港华民航空", "国泰航空", "港龙航空", "香港航空", "澳门航空", "捷亚航空", "德安航空", "长荣航空", "中华航空", "国航", "海航", "东航", "南航", "深航", "厦航", "川航", "上航", "春秋", "邮政航空", "华民航空", "菲律宾航空", "亚洲航空", "马来西亚航空", "韩亚航空", "大韩航空", "全日空航空", "日本航空", "泰国东方航空", "泰国国际航空", "文莱皇家航空公司", "捷星航空", "新加坡航空", "老虎航空", "印度航空", "越南航空", "埃及航空", "南非航空", "澳亚航空", "澳洲航空", "新西兰航空", "自由航空", "爱尔兰航空", "俄罗斯航空", "芬兰航空", "法国航空", "柏林航空", "荷兰皇家航空", "汉莎航空", "葡萄牙航空", "瑞士国际航空", "土耳其航空", "西班牙国家航空", "意大利航空", "英国航空", "易捷航空", "维珍航空", "加拿大航空", "加拿大爵士航空", "美国联合航空", "美国航空", "美国达美航空", "卡塔尔航空", "阿联酋联合航空", "印尼鹰航", "阿提哈德航空", "阿联酋航空", "亚洲国际航空", "亚航", "马航", "全日空", "ANA", "文莱航空" };
		String projectName = "AirlineCompany";

		DateTime start = new DateTime(2016, 06, 1, 0, 0);
		DateTime end = new DateTime(2017, 03, 31, 0, 0);
		DateTime plusWeeks = start.plusWeeks(1);

		// 推送任务
		while (plusWeeks.isBefore(end)) {

			for (String key : keys) {
				// 修改成推送队列，然后获取抓取的情况
				WeiboSearchTask task = new WeiboSearchTask(projectName, key, start.toString("yyyyMMdd"), plusWeeks.toString("yyyyMMdd"));
				GlobalComponents.redisAPI.lpush(weiboSearchByTimeTaskQueue, JSON.toJSONString(task));
				Thread.sleep(1000);
			}
			log.info("{} 至 {} 时间任务处理完成", start, plusWeeks);
			plusWeeks = plusWeeks.plusWeeks(1);
			start = start.plusWeeks(1);
		}
		// 添加剩下的结尾日期
		for (String key : keys) {
			WeiboSearchTask task = new WeiboSearchTask(projectName, key, start.toString("yyyyMMdd"), end.toString("yyyyMMdd"));
			GlobalComponents.redisAPI.lpush(weiboSearchByTimeTaskQueue, JSON.toJSONString(task));
			Thread.sleep(1000);
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
