package lolth.weibo.auth.task;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lolth.weibo.auth.bean.WeiboAuthBean;

import org.apache.commons.dbutils.handlers.BeanHandler;

public class WeiboLoginTest {

	public static void main(String[] args) throws Exception {
		new WeiboLoginTest().run();
	}

	private void run() throws Exception {
		WeiboAuthBean bean = getAuthBean();
		String url = "http://weibo.cn/account/privacy/tags/?uid=2638174615";

		HttpFetcher fetcher = new HttpClientFetcher();
		HttpRequest req = new HttpRequest();
		req.setUrl(url);
		req.setCookies(bean.getCookieMap());
		req.setNeedContent(true);
		req.setNeedCookies(false);

		HttpResponse resp = fetcher.run(req);
		fetcher.close();
		System.out.println(new String(resp.getContent(), "utf-8"));
	}

	public WeiboAuthBean getAuthBean() throws Exception {
		WeiboAuthBean bean = GlobalComponents.db.getRunner().query("select * from " + BaseBean.getTableName(WeiboAuthBean.class) + " where uuid=?", new BeanHandler<WeiboAuthBean>(WeiboAuthBean.class),"23181179-249b-44eb-9e88-35d49097925f");
		return bean;
	}

}
