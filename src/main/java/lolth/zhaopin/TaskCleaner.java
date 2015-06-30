package lolth.zhaopin;

import java.sql.SQLException;

import lakenono.base.Queue;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCleaner {

	// TODO Queue utlis
	public static void main(String[] args) {

		String[] projectNames = { "www.newsmth.net", "bbs.sysu.edu.cn", "bbs.xdnice.com", "www.ujsbbs.com", "bbs.njtech.edu.cn", "hnnu.myubbs.com", "bbs.zjut.edu.cn" };
		String[] batchNames = { "baidu_search_bbs_list", "baidu_search_bbs_topic" };
		for (String projectName : projectNames) {
			for (String bath : batchNames) {
				// Queue.viewQueueNum(batchName);
				try {
					Queue.cleanQueue(bath);
					Task.deleteLog(projectName, bath);
				} catch (SQLException e) {
					log.error("{} , {} clean error : ", projectName, bath, e);
				}
			}
		}
	}
}
