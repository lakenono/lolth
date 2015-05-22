package lolth.zol.bbs;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.zol.bbs.old.ZolBBSDetailTaskProducer;
import lolth.zol.bbs.old.ZolBBSUserTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZolBBSPostTaskCleaner {

	public static void main(String[] args) {
		String keyword = "oppo";

		String[] batchNames = { ZolBBSListTaskProducer.ZOL_POST_LIST, ZolBBSDetailTaskProducer.ZOL_DETAIL_PAGE, ZolBBSUserTaskProducer.ZOL_BBS_POST_USER };

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
