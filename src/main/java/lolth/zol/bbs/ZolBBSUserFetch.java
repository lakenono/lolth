package lolth.zol.bbs;

import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.zol.bbs.bean.ZolBBSUserBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZolBBSUserFetch extends PageParseFetchTaskHandler {

	public ZolBBSUserFetch() {
		super(ZolBBSUserTaskProducer.ZOL_BBS_POST_USER);
	}

	public static void main(String[] args) throws Exception {
		ZolBBSUserFetch fetch = new ZolBBSUserFetch();
		fetch.setSleep(3000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements userData = doc.select("div.main div#userInfoBox.section div.data-cont");

		if (userData.size() > 0) {
			ZolBBSUserBean userBean = new ZolBBSUserBean();

			Element user = userData.first();

			// 昵称
			Elements name = user.getElementsMatchingOwnText("昵称：");
			if (name.size() > 0) {
				userBean.setName(name.first().nextElementSibling().text());
			}

			Elements level = user.getElementsMatchingOwnText("等级：");
			if (level.size() > 0) {
				userBean.setLevel(level.first().nextElementSibling().text());
			}

			Elements sex = user.getElementsMatchingOwnText("性别：");
			if (sex.size() > 0) {
				userBean.setSex(sex.first().nextElementSibling().text());
			}

			Elements from = user.getElementsMatchingOwnText("所在地：");
			if (from.size() > 0) {
				userBean.setFrom(from.first().nextElementSibling().text());
			}

			Elements marry = user.getElementsMatchingOwnText("婚姻状况：");
			if (marry.size() > 0) {
				userBean.setMarry(marry.first().nextElementSibling().text());
			}

			Elements birthday = user.getElementsMatchingOwnText("生日：");
			if (birthday.size() > 0) {
				userBean.setBirthday(birthday.first().nextElementSibling().text());
			}

			Elements regTime = user.getElementsMatchingOwnText("注册时间：");
			if (regTime.size() > 0) {
				userBean.setRegTime(regTime.first().nextElementSibling().text());
			}

			userBean.setId(task.getExtra());
			userBean.setUrl(task.getUrl());

			userBean.persistOnNotExist();
		}
	}

	public static class ZolBBSUserTaskProducer extends FetchTaskProducer {
		private static final String ZOL_BBS_USER_URL_TEMPLATE = "http://my.zol.com.cn/{0}/settings/";
		public static final String ZOL_BBS_POST_USER = "zol_bbs_post_user";

		public ZolBBSUserTaskProducer() {
			super(ZOL_BBS_POST_USER);
		}

		protected FetchTask buildTask(String user, FetchTask frontTask) {
			FetchTask task = new FetchTask();
			task.setName(frontTask.getName());
			task.setBatchName(ZOL_BBS_POST_USER);
			task.setUrl(buildUrl(user));
			task.setExtra(user);
			return task;
		}

		private String buildUrl(String user) {
			return MessageFormat.format(ZOL_BBS_USER_URL_TEMPLATE, user);
		}

		public void push(String userId, FetchTask frontTask) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
			saveAndPushTask(buildTask(userId, frontTask));
		}

	}

}
