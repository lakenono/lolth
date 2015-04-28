package lolth.xcar;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.xcar.bbs.XCarBBSPostDetailTaskProducer;
import lolth.xcar.bbs.XCarBBSPostListTaskProducer;
import lolth.xcar.k.XCarWordOfMouthListTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XCarBBSTaskCleaner {
	public static void main(String[] args) {
		String keyword = "chevrolet";

		String[] batchNames = { XCarWordOfMouthListTaskProducer.XCAR_K_LIST, XCarBBSPostListTaskProducer.XCAR_BBS_POST_LIST, XCarBBSPostDetailTaskProducer.XCAR_BBS_POST_DETAIL };

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
