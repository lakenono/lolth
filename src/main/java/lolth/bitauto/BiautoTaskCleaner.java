package lolth.bitauto;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.bitauto.bbs.BitautoBBSDetailTaskProducer;
import lolth.bitauto.bbs.BitautoBBSListTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiautoTaskCleaner {

	public static void main(String[] args) {
		String keyword = "chevrolet";

		String[] batchNames = { BitautoBBSListTaskProducer.BITAUTO_BBS_POST_LIST, BitautoBBSDetailTaskProducer.BITAUTO_BBS_POST_DETAIL, BitautoBBSDetailTaskProducer.BITAUTO_K_POST_DETAIL };

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