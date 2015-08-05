package lolthx.pcbaby;

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

		 Queue.viewQueueNum("baidu_search_bbs_topic");
		// Queue.cleanQueue("cn_weibo_user");
//		String keyword = "cba";
//
//		String[] batchNames = { "ppty_cba_list" ,"ppty_cba_list_comment"};
//
//		for (String batchName : batchNames) {
//			// Queue.viewQueueNum(batchName);
//			try {
//				Queue.cleanQueue(batchName);
//				Task.deleteLog(keyword, batchName);
//			} catch (SQLException e) {
//				log.error("{} , {} clean error : ", keyword, batchName, e);
//			}
//		}
	}
}
