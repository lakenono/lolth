package lolth.mamanet;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MamaCleaner {
	public static void main(String[] args) {
		//keyword
		String keyword = "惠氏";
		//队列名次
		String batchName = MamaNetSearchListProducer.MAMANET_SEARCH_LIST;
		try {
			FetchTaskProducer.cleanAllTask(batchName);
			FetchTaskProducer.cleanAllTaskLog(keyword, batchName);
		} catch (SQLException e) {
			log.error("{} , {} clean error : ", keyword, batchName, e);
		}
		
	}
}
