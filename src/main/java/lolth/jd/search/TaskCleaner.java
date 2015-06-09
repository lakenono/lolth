package lolth.jd.search;

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
		String keyword = "威仕高";

		String[] batchNames = { "jd_search_commodity_list", "jd_commodity"};

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
