package lolthx.weibo.search.fetch;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.weibo.search.task.WeiboSearchTask;

public class WeiboSearchFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return WeiboSearchTask.WEIBO_SEARCH_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		// TODO Auto-generated method stub

	}
	public static void main(String[] args) {
		new WeiboSearchFetch().run();
	}
	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}
}
