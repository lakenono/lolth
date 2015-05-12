package lolth.babytree;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lolth.babytree.bbs.BabytreeBBSSearchDetailTaskProducer;
import lolth.babytree.bbs.BabytreeBBSSearchList;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TaskCleaner {

	public static void main(String[] args) {
		String keyword = "惠氏启赋";

		String[] batchNames = { BabytreeBBSSearchList.BABYTREE_BBS_LIST, BabytreeBBSSearchDetailTaskProducer.BABYTREE_BBS_LIST_DETAIL };

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
