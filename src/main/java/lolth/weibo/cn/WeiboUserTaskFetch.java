package lolth.weibo.cn;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lakenono.task.FetchTaskProducer;
import lolth.weibo.bean.WeiboUserBean;
import lolth.weibo.cn.WeiboUserTagTaskFetch.WeiboUserTagTaskProducer;
import lolth.weibo.fetcher.WeiboFetcher;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

public class WeiboUserTaskFetch extends FetchTaskHandler {
	private WeiboUserTagTaskProducer userTagTaskProducer = null;

	public WeiboUserTaskFetch() {
		super(WeiboUserTaskProducer.WEIBO_USER);
		userTagTaskProducer = new WeiboUserTagTaskProducer();
	}

	public static void main(String[] args) throws Exception {
		WeiboUserTaskFetch fetch = new WeiboUserTaskFetch();
		fetch.setSleep(15000);
		fetch.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		Document doc = WeiboFetcher.cnFetcher.fetch(task.getUrl());
		Elements div = doc.getElementsMatchingOwnText("基本信息");
		if (div.size() > 0) {
			Element c = div.first().nextElementSibling();
			String text = c.html();

			String[] fields = StringUtils.splitByWholeSeparator(text, "\n<br>");
			if (fields != null) {
				WeiboUserBean user = new WeiboUserBean();

				String[] ids = StringUtils.split(task.getExtra(), ',');
				user.setId(ids[0]);
				user.setUid(ids[1]);

				for (String f : fields) {
					if (StringUtils.startsWith(f, "昵称:")) {
						user.setName(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "性别:")) {
						user.setSex(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "地区:")) {
						user.setArea(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "生日:")) {
						user.setBirthday(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "简介:")) {
						user.setSummery(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "认证:")) {
						user.setAuth(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "血型:")) {
						user.setBloodType(StringUtils.substringAfter(f, ":"));
					}
				}

				boolean persist = user.persistOnNotExist();

				//推送标签任务
				if(persist){
					userTagTaskProducer.push(user.getUid(), task);
				}
			}
		}
	}

	@Slf4j
	public static class WeiboUserTaskProducer extends FetchTaskProducer {

		public static final String WEIBO_USER = "cn_weibo_user";

		private static final String WEIBO_USER_URL_TEMPLATE = "http://weibo.cn/{0}";
		private static final String WEIBO_USER_INFO_URL_TEMPLAGE = "http://weibo.cn/{0}/info";

		private int sleep = 15000;

		public WeiboUserTaskProducer() {
			super(WEIBO_USER);
		}

		public void push(String userId, String taskName) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
			FetchTask fetchTask = buildTask(userId, taskName);
			saveAndPushTask(fetchTask);
		}

		protected FetchTask buildTask(String id, String taskName) {
			// 需要根据string类型的id获取uid
			FetchTask task = null;

			try {
				String uid = id;
				if (!StringUtils.isNumeric(uid)) {
					uid = getUid(id);
					Thread.sleep(sleep);
				}

				if (Strings.isNullOrEmpty(uid)) {
					throw new RuntimeException("uid can not get id : " + id);
				}

				task = new FetchTask();
				task.setName(taskName);
				task.setBatchName(WEIBO_USER);
				task.setUrl(buildUserInfoUrl(uid));

				task.setExtra(id + "," + uid);
			} catch (Exception e) {
				log.error("{} get uid error :", id, e);
			}

			return task;
		}

		private String buildUserUrl(String id) {
			return MessageFormat.format(WEIBO_USER_URL_TEMPLATE, id);
		}

		private String buildUserInfoUrl(String uid) {
			return MessageFormat.format(WEIBO_USER_INFO_URL_TEMPLAGE, uid);
		}

		private String getUid(String nickname) throws IOException, InterruptedException {
			String uid = null;

			Document doc = WeiboFetcher.cnFetcher.fetch(buildUserUrl(nickname));
			Elements imgElements = doc.select("img.por");
			if (imgElements.size() > 0) {
				uid = imgElements.first().parent().attr("href");
				uid = StringUtils.substringBetween(uid, "/", "/");
			}
			return uid;
		}
	}

}
