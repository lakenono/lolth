package lolth.xcar;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.xcar.bbs.XCarBBSPostDetailProducer;
import lolth.xcar.bbs.XCarBBSPostListProducer;
import lolth.xcar.k.XCarWordOfMouthListProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XCarBBSTaskCleaner {
	public static void main(String[] args) {
		String keyword = "chevrolet";

		String[] batchNames = { XCarWordOfMouthListProducer.XCAR_K_LIST, XCarBBSPostListProducer.XCAR_BBS_POST_LIST, XCarBBSPostDetailProducer.XCAR_BBS_POST_DETAIL };

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
