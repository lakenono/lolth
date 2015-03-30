package lolth.zol.bbs;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.zol.bbs.bean.ZolBBSUserBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZolBBSUserFetch extends PageParseFetchTaskHandler {

	public ZolBBSUserFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception{
		String taskQueueName = ZolBBSUserTaskProducer.ZOL_BBS_POST_USER;
		ZolBBSUserFetch fetch = new ZolBBSUserFetch(taskQueueName);
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

}
