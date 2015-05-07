package lolth.autohome;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.autohome.buy.AutohomeBuyInfoListTaskProuducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutohomeBBSTaskCleaner {
	public static void main(String[] args) {
		String keyword = "chevrolet";

		String[] batchNames = { AutohomeBuyInfoListTaskProuducer.AUTOHOME_BUY_INFO_LIST};

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
