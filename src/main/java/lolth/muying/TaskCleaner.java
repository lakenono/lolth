package lolth.muying;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCleaner {

	public static void main(String[] args) {
		String keyword = "惠氏";

		String[] batchNames = { MuYingCommoditySearchList.MUYING_SHOP_LIST, MuYingCommodityDetailTaskProducer.MUYING_SHOP_LIST_DETAIL, MuYingCommodityDetailFetch.MUYING_SHOP_DETAIL_COMMENT };

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
