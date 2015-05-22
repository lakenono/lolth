package lolth.weibo.task;

import lolth.weibo.cn.WeiboSearchFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealTimeWeiboTaskRunner {
	public static void main(String[] args) throws Exception {
		long sleep = 10 * 60 * 1000;
		String keyword = "oppo";
		while (true) {
			try {
				new WeiboSearchFetch(keyword).run();
				log.info("Wait for next batch fetch ... ");
				Thread.sleep(sleep);
			} catch (Exception e) {
				log.error("Weibo fetch fail ! ", e);
			}
		}
	}
}
