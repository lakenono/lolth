package lolth.baidu.post.task;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.baidu.post.bean.BaiduPostUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class BaiduPostUserFetch extends PageParseFetchTaskHandler {

	public static void main(String[] args) {
		String taskQueueName = BaiduPostUserTaskProducer.BAIDU_POST_USER;
		BaiduPostUserFetch userFetch = new BaiduPostUserFetch(taskQueueName);
		userFetch.setSleep(3000);
		userFetch.run();
	}

	public BaiduPostUserFetch(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws SQLException {
		BaiduPostUserBean user = parseUserBean(doc);
		if (user == null) {
			return;
		}

		user.setId(task.getExtra());
		updateUserBean(user);

		log.info("{} update !", user);
	}

	private BaiduPostUserBean parseUserBean(Document doc) {
		String name = null;
		String posts = null;
		String postAge = null;

		Elements nameSpan = doc.select("span.userinfo_username");
		if (nameSpan.size() > 0) {
			name = nameSpan.first().text();
		}

		Elements userDiv = doc.select("div.userinfo_num");

		if (userDiv.size() > 0) {
			Elements postAgeSpan = userDiv.first().getElementsMatchingOwnText("吧龄:");
			if (postAgeSpan.size() > 0) {
				postAge = postAgeSpan.first().text();
				postAge = StringUtils.substringAfter(postAge, "吧龄:");
			}

			Elements postsSpan = userDiv.first().getElementsMatchingOwnText("发贴:");
			if (postsSpan.size() > 0) {
				posts = postsSpan.first().text();
				posts = StringUtils.substringAfter(posts, "发贴:");
			}
		}

		if (StringUtils.isBlank(name) && StringUtils.isBlank(postAge) && StringUtils.isBlank(posts)) {
			return null;
		}
		BaiduPostUserBean user = new BaiduPostUserBean();
		// 性别
		Elements sex = doc.select("span.userinfo_sex");
		if (sex.size() > 0) {
			String attr = sex.first().attr("class");
			if (StringUtils.endsWith(attr, "sex_male")) {
				user.setSex("男");
			} else {
				user.setSex("女");
			}
		}
		user.setName(name);
		user.setPostAge(postAge);
		user.setPosts(posts);

		return user;
	}

	private void updateUserBean(BaiduPostUserBean user) throws SQLException {
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(BaiduPostUserBean.class) + " SET sex=?, posts=?, postAge=? WHERE id=?", user.getSex(), user.getPosts(), user.getPostAge(), user.getId());
	}

}
