package lolth.mq;

import lakenono.base.Queue;

public class MqTest {
	public static void main(String[] args) {
		long num = Queue.viewQueueNum("lakenono_task_weibo");
		System.out.println(num);
	}
}
