package lolth.weibo.cn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.weibo.bean.WeiboUserBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiboUserTagFetch {
	private static final Logger log = LoggerFactory
			.getLogger(WeiboUserTagFetch.class);

	public static void main(String[] args) throws Exception {
		new WeiboUserTagFetch().run();
	}

	private void run() throws Exception {
		while (true) {
			List<String> uids = getTask();
			if (uids.isEmpty()) {
				break;
			}

			for (String uid : uids) {
				String newId = uid;
				
				WeiboUserBean bean = null;
				
				try {
					if (!StringUtils.isNumeric(uid)) {
						newId = getUid(uid);
					}
					
					if(StringUtils.isNotBlank(newId)){
						String url = buildUrl(newId);
						bean = processUrl(url);
						
					}else{
						bean = new WeiboUserBean();
						bean.setTags("unknown");
					}
					
					bean.setId(uid);
					update(bean);
					log.info("{} success ", uid);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("{} error ! ", uid, e);
				}
			}
		}
		log.info("fetch finish!");
	}

	private WeiboUserBean processUrl(String url) throws IOException,
			InterruptedException {
		Document doc = fetch(url);

		WeiboUserBean bean = new WeiboUserBean();
		StringBuilder tags = new StringBuilder();

		Elements tagDiv = doc.getElementsContainingOwnText("的标签");

		if (tagDiv.size() > 0) {
			Elements tagElements = tagDiv.first().select("a");

			if (tagElements.size() > 0) {
				for (Element tag : tagElements) {
					tags.append(tag.text()).append(';');
				}
				tags.deleteCharAt(tags.length() - 1);
			}
		}
		bean.setTags(tags.toString());
		return bean;
	}

	private void update(WeiboUserBean bean) throws SQLException {
		GlobalComponents.db.getRunner().update(
				"update " + BaseBean.getTableName(WeiboUserBean.class)
						+ " set tags=? where userId=?", bean.getTags(),
				bean.getId());
	}

	private String buildUrl(String uid) {
		return "http://weibo.cn/account/privacy/tags/?uid=" + uid;
	}

	private List<String> getTask() throws SQLException {
		List<String> uids = GlobalComponents.db.getRunner().query(
				"select userId from "
						+ BaseBean.getTableName(WeiboUserBean.class)
						+ " where tags is null limit 100",
				new ColumnListHandler<String>());
		return uids;
	}

	private Document fetch(String url) throws IOException, InterruptedException {
		Connection connect = Jsoup.connect(url);

		// cookie
		connect.cookie("_T_WM", "0f0602cfd6ce7a1ae8dd3020d31aafdc");
		connect.cookie(
				"SUB",
				"_2A2551RSYDeTxGeVO71QT8SbFyTmIHXVbObzQrDV6PUJbrdANLVXhkW0Gk7s86reO8Lb7VV8jQvjftR9KlQ..");
		connect.cookie("gsid_CTandWM", "4uFQ731b1WNb44bDzIUsjcMsyeV");
		connect.cookie("M_WEIBOCN_PARAMS", "rl%3D1");

		// ua
		connect.userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		connect.header("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.timeout(1000 * 5);

		int retry = 5;

		for (int i = 1; i <= retry; i++) {
			try {
				// 休眠时间
				Thread.sleep(3 * 1000);
				return connect.post();
			} catch (java.net.SocketTimeoutException e) {
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			} catch (java.net.ConnectException e) {
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			}

		}
		throw new RuntimeException("fetcher重试[" + retry + "]次后无法成功.");
	}

	private String buildInfoUrl(String uid) {
		return "http://weibo.cn/" + uid;
	}

	private String getUid(String nickname) throws IOException,
			InterruptedException {
		String uid = null;

		Document doc = fetch(buildInfoUrl(nickname));
		Elements imgElements = doc.select("img.por");
		if (imgElements.size() > 0) {
			uid = imgElements.first().parent().attr("href");
			uid = StringUtils.substringBetween(uid, "/", "/");
		}
		return uid;
	}
}
