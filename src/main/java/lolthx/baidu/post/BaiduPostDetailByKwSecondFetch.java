package lolthx.baidu.post;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.baidu.post.bean.BaiduPostBykwBean;
import lolthx.baidu.post.bean.BaiduPostUserBykwBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaiduPostDetailByKwSecondFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_post_bykw_second";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		BaiduPostUserBykwBean userBean = null;
		BaiduPostBykwBean post = null;
		
		post = new BaiduPostBykwBean();

		post.setId(StringUtils.substringAfterLast(task.getUrl(), "/"));
		post.setUrl(task.getUrl());
		post.setKeyword(task.getExtra());
		post.setProjectName(task.getProjectName());

		// 板块 2015-5-18
		Elements forum = doc.select("a.card_title_fname");
		if (forum.size() > 0) {
			post.setForum(forum.first().text());
		}

		// 回复数 2015-5-18
		Elements replyLi = doc.select("div.pb_footer li.l_reply_num");
		if (replyLi.size() > 0) {
			String replys = replyLi.first().text();
			replys = StringUtils.substringBefore(replys, "回复贴");
			if (StringUtils.isNumeric(replys)) {
				post.setReplys(replys);
			}
		}

		// 标题正文
		Elements leftSection = doc.select("div#pb_content.pb_content.clearfix div.left_section");

		if (leftSection.size() > 0) {
			// title
			Elements title = leftSection.select(".core_title_txt");
			if (title.size() > 0) {
				post.setTitle(title.first().text());
			}

			Elements contentDiv = leftSection.select("div.d_post_content_main.d_post_content_firstfloor");
			if (contentDiv.size() > 0) {
				// content
				Elements content = contentDiv.first().select("cc div.d_post_content.j_d_post_content");
				if (content.size() > 0) {
					post.setContent(content.first().text());
				}
			}

			// postTime 2015-5-18
			Elements postTimeUl = leftSection.first().select("div.d_post_content_firstfloor ul.p_tail");
			if (postTimeUl.size() > 0) {
				String postTime = postTimeUl.first().text();
				postTime = StringUtils.substringAfter(postTime, "楼");
				post.setPostTime(postTime);
			}
		}

		
		Elements userDiv = doc.select("div.l_post");
		if (userDiv.size() > 0) {
			userBean = new BaiduPostUserBykwBean();
			String postInfo = userDiv.first().attr("data-field");

			JSONObject json = JSON.parseObject(postInfo);

			JSONObject user = json.getJSONObject("author");
			userBean.setId(user.getString("user_id"));
			String user_name = user.getString("user_name");
			if (StringUtils.isNotBlank(user_name)) {
				userBean.setUrl("http://tieba.baidu.com/home/main?un=" + user_name + "&ie=utf-8&fr=pb");
				userBean.setName(user_name);
			}

			if (StringUtils.isBlank(post.getPostTime())) {
				JSONObject content = json.getJSONObject("content");
				String postTime = content.getString("date");
				post.setPostTime(postTime);
			}
			post.setUserId(userBean.getId());
		}

		Thread.sleep(2000);
		String userhtml = GlobalComponents.jsoupFetcher.fetch(userBean.getUrl());
		Document userDoc = Jsoup.parse(userhtml);

		parseUserBean(userDoc, userBean);

		userBean.saveOnNotExist();

		post.saveOnNotExist();

	
	}

	private void parseUserBean(Document doc, BaiduPostUserBykwBean user) {
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
			return;
		}

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
		// user.setName(name);
		user.setPostAge(postAge);
		user.setPosts(posts);

	}
	
	public static void main(String args[]){
		for(int i = 1;i<=1000;i++){
			new BaiduPostDetailByKwSecondFetch().run();
		}
	}

}
