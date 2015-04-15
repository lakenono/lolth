package lolth.official.oppo;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OppoForumPostTaskCleaner {

	public static void main(String[] args) {
		String keyword = "oppo";

		String[] batchNames = { OppoForumPostListTaskProducer.OPPO_FORUM_POST_LIST, OppoForumPostDetailTaskProducer.OPPO_FORUM_POST_DETAIL, OppoForumPostUserTaskProducer.OPPO_FORUM_POST_USER };

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
