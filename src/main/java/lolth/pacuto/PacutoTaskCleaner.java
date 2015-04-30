package lolth.pacuto;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.pacuto.bbs.PacutoBBSDetailTaskProducer;
import lolth.pacuto.bbs.PacutoBBSListTaskProducer;
import lolth.pacuto.k.PacutoWordOfMouthListTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacutoTaskCleaner {

	public static void main(String[] args) {
		String keyword = "chevrolet";

		String[] batchNames = { PacutoBBSListTaskProducer.PACUTO_BBS_POST_LIST, PacutoBBSDetailTaskProducer.PACUTO_BBS_POST_DETAIL,PacutoWordOfMouthListTaskProducer.PACUTO_K_POST_LIST };

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