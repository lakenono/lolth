package lolthx.onlylady.bbs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
	public static void main(String[] args) throws UnsupportedEncodingException {
		String keyword = "眼线";

		String[] batchNames = { "onlylady_bbs_search_list", "onlylady_bbs_topic","onlylady_bbs_user"};

		for (String batchName : batchNames) {
			Queue.viewQueueNum(batchName);
			try {
				Queue.cleanQueue(batchName);
				Task.deleteLog(keyword, batchName);
			} catch (SQLException e) {
				log.error("{} , {} clean error : ", keyword, batchName, e);
			}
		}
	}
}
