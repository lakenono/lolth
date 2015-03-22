package lolth.weibo.auth.task;

import java.util.Map;
import java.util.UUID;

import lolth.weibo.auth.WeiboLogin;
import lolth.weibo.auth.bean.WeiboAuthBean;
import lolth.weibo.auth.bean.WeiboSiteType;
import lolth.weibo.auth.com.SeleniumComWeiboLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class COMWeiboLoginTask {
	private static final Logger log = LoggerFactory.getLogger(COMWeiboLoginTask.class);

	public static void main(String[] args) throws Exception {
		new COMWeiboLoginTask().run();

	}

	private void run() throws Exception {
		String user = "puppet.mars@gmail.com";
		String password = "Go123$%^";

		WeiboLogin comWeiboLogin = new SeleniumComWeiboLogin(user, password);

		log.info("{} login ! ", user);

		Map<String, String> cookies = comWeiboLogin.login();
		log.info("cookies : {}  ! ", cookies);
		if (cookies != null) {
			log.info("cookies : {}  ! ", cookies.size());
			WeiboAuthBean bean = new WeiboAuthBean();

			bean.setUuid(UUID.randomUUID().toString());
			bean.setCookieMap(cookies);
			bean.setType(WeiboSiteType.COM.ordinal() + "");

			bean.persist();
		}
	}
}
