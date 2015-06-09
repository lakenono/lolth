package lolth.pcbaby.bbs;

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
		String keyword = "测试奶粉";

		String[] batchNames = { "pcbaby_bbs_topic", "pcbaby_bbs_search_list", "pcbaby_bbs_topic_reply", "pcbaby_userinfo" };

		for (String batchName : batchNames) {
//			Queue.viewQueueNum(batchName);
			try {
				Queue.cleanQueue(batchName);
				Task.deleteLog(keyword, batchName);
			} catch (SQLException e) {
				log.error("{} , {} clean error : ", keyword, batchName, e);
			}
		}
	}
}
