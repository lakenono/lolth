package lolth.yhd.fresh.task;

import lakenono.base.Queue;

public class YhdTaskCleaner {
	public static void main(String[] args) {
		String batchName = "yhd_fresh_list";
		Queue.viewQueueNum(batchName);
		Queue.cleanQueue(batchName);
	}
}
