package lolthx.weibo.task;

import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboBean;
import lombok.extern.slf4j.Slf4j;

/**
 * 微博根据关键字实时抓取，projectName为建表附加名
 * @author yanghp
 *
 */
@Slf4j
public class RealTimeWeiboTask {
	public static void main(String[] args) throws Exception {
		long sleep = 10 * 60 * 1000;
		if (args.length < 2) {
			System.out.println("Useage : RealTimeWeiboTaskRunner projectName args[0] ,keyword args[1] !");
			return;
		}
		String projectName = args[0];
		String keyword = args[1];
		//TODO  根据工程名创建表
		DBBean.createTable(WeiboBean.class, projectName);
		while (true) {
			try {
				log.info("start {} fetch ...",keyword);
				new WeiboSearchTask(projectName,keyword).run();
				log.info("Wait for next batch fetch ... ");
				Thread.sleep(sleep);
			} catch (Exception e) {
				log.error("Weibo fetch fail ! ", e);
			}
		}
	}
}
