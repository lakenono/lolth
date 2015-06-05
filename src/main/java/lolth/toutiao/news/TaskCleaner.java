package lolth.toutiao.news;

import java.sql.SQLException;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCleaner {

	// TODO Queue utlis
	public static void main(String[] args) {
		String keyword = "oppo";

		String[] batchNames = { "cn_weibo_user_gbs" };

		for (String batchName : batchNames) {
//			try {
				Queue.viewQueueNum(batchName);
//				Queue.cleanQueue(batchName);
//				Task.deleteLog(keyword, batchName);
//			} catch (SQLException e) {
//				log.error("{} , {} clean error : ", keyword, batchName, e);
//			}
		}
	}
}
