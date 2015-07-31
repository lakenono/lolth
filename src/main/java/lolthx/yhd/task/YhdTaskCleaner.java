package lolthx.yhd.task;

import lakenono.base.Queue;

public class YhdTaskCleaner {
	public static void main(String[] args) {
		String batchName = "yhd_ask_queue";
		Queue.viewQueueNum(batchName);
		Queue.cleanQueue(batchName);
	}
}
