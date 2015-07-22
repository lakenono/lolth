package lolth.weibo.task;

import lolth.weibo.cn.WeiboSearchFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealTimeWeiboTaskRunner {
	public static void main(String[] args) throws Exception {
		long sleep = 10 * 60 * 1000;
		if (args.length == 0) {
			System.out.println("Useage : RealTimeWeiboTaskRunner keyword !");
			return;
		}
		String keyword = args[0];
		while (true) {
			try {
				log.info("start {} fetch ...",keyword);
				new WeiboSearchFetch(keyword).run();
				log.info("Wait for next batch fetch ... ");
				Thread.sleep(sleep);
			} catch (Exception e) {
				log.error("Weibo fetch fail ! ", e);
			}
		}
	}
}
