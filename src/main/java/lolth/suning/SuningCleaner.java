package lolth.suning;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SuningCleaner {
	public static void main(String[] args) {
		String keyword = "惠氏";

		String[] batchNames = { SuningSearchListTaskProducer.SUNING_SEARCH_LIST, SuningSearchListFetch.SUNING_ITEM_COMMENT_MAXPAGE, SuningSearchListFetch.SUNING_ITEM_DETAIL, SuningItemCommentListTaskProducer.Producer.SUNING_ITEM_COMMENT_LIST };

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
