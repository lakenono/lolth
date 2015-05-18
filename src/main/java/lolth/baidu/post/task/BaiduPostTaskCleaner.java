package lolth.baidu.post.task;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduPostTaskCleaner {

	public static void main(String[] args) {
		String keyword = "育儿";

		String[] batchNames = { BaiduPostListTaskProducer.BAIDU_POST_LIST, BaiduPostListFetch.BAIDU_POST_DETAIL, BaiduPostUserTaskProducer.BAIDU_POST_USER };

		for (String batchName : batchNames) {
			try {
				FetchTaskProducer.cleanAllTask(batchName);
				FetchTaskProducer.cleanAllTaskLog(keyword, batchName);
			} catch (SQLException e) {
				log.error("{} , {} clean error : ", keyword, batchName, e);
			}
		}

	}
}
