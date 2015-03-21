package lolth.weibo.auth.task;

import java.util.Map;
import java.util.UUID;

import lolth.weibo.auth.WeiboLogin;
import lolth.weibo.auth.bean.WeiboAuthBean;
import lolth.weibo.auth.bean.WeiboSiteType;
import lolth.weibo.auth.cn.CNWeiboLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CNWeiboLoginTask {
	private static final Logger log = LoggerFactory.getLogger(CNWeiboLoginTask.class);

	public static void main(String[] args) throws Exception {
		new CNWeiboLoginTask().run();

	}

	private void run() throws Exception {
		String user = "puppet.mars@gmail.com";
		String password = "Go123$%^";

		WeiboLogin comWeiboLogin = new CNWeiboLogin(user, password);

		log.info("{} login ! ", user);

		Map<String, String> cookies = comWeiboLogin.login();
		log.info("cookies : {}  ! ", cookies);
		if (cookies != null) {
			log.info("cookies : {}  ! ", cookies.size());
			WeiboAuthBean bean = new WeiboAuthBean();

			bean.setUuid(UUID.randomUUID().toString());
			bean.setCookieMap(cookies);
			bean.setType(WeiboSiteType.CN.ordinal() + "");

			bean.persist();
		}
	}
}
