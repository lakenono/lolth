package lolth.xcar.bbs;

import lakenono.fetch.adv.utils.HttpURLUtils;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.xcar.bbs.bean.XCarBBSPostBean;
import lolth.xcar.bbs.bean.XCarBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XCarBBSPostDetailFetch extends PageParseFetchTaskHandler {

	public static void main(String[] args) throws Exception {
		String queueName = XCarBBSPostDetailTaskProducer.XCAR_BBS_POST_DETAIL;

		XCarBBSPostDetailFetch fetch = new XCarBBSPostDetailFetch(queueName);

		fetch.setSleep(1000);
		fetch.run();
	}

	public XCarBBSPostDetailFetch(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		XCarBBSUserBean user = null;

		XCarBBSPostBean post = new XCarBBSPostBean();
		post.setId(HttpURLUtils.getUrlParams(task.getUrl(), "utf-8").get("tid"));
		post.setUrl(task.getUrl());
		post.setKeyword(task.getName());
		post.setBrandId(task.getExtra());

		Elements postElements = doc.select("div#_img>div.F_box_2");
		if (postElements.size() > 0) {
			Element mainPost = postElements.first();
			// 帖子部分
			// 标题
			Elements title = mainPost.select("h1.title");
			if (!title.isEmpty()) {
				String titleStr = title.first().ownText();
				titleStr = StringUtils.substringAfter(titleStr, ">");
				titleStr = StringUtils.trim(titleStr);
				post.setTitle(titleStr);
			}

			// 回复
			Elements viewReply = mainPost.select("td.titleStyle3>p>span");
			if (!viewReply.isEmpty()) {
				String viewReplyStr = viewReply.first().text();
				String views = StringUtils.substringBetween(viewReplyStr, "查看：", " ");
				views = StringUtils.trim(views);

				post.setViews(views);

				String replys = StringUtils.substringBetween(viewReplyStr, "回复：", " ");
				replys = StringUtils.trim(replys);

				post.setReplys(replys);
			}
		}

		Elements postContent = doc.select("form#delpost>div.F_box_2");
		if (!postContent.isEmpty()) {
			Element mainPost = postContent.first();

			Elements mainContent = mainPost.select("table.t_msg tr");
			if (!mainContent.isEmpty()) {
				// 发表时间
				String postTimeStr = mainContent.first().text();
				postTimeStr = StringUtils.substringAfter(postTimeStr, "发表于");
				postTimeStr = StringUtils.normalizeSpace(postTimeStr);
				postTimeStr = StringUtils.trim(postTimeStr);
				post.setPostTime(postTimeStr);
			}

			// 内容
			Elements text = mainPost.select("td.line");
			if (!text.isEmpty()) {
				post.setText(text.text());
			}

			// 用户部分
			Elements userTds = mainPost.select("td.t_user");
			if (userTds.size() > 0) {
				user = getUser(userTds.first(), task);
			}

		}

		if (user != null) {
			user.persistOnNotExist();
			post.setAuthorId(user.getId());
		}

		if (post != null) {
			post.persistOnNotExist();
		}
	}

	private XCarBBSUserBean getUser(Element userElement, FetchTask task) {
		XCarBBSUserBean user = new XCarBBSUserBean();

		Elements name = userElement.select("a.bold");
		if (!name.isEmpty()) {
			String url = name.first().absUrl("href");

			user.setId(HttpURLUtils.getUrlParams(url, "utf-8").get("uid"));
			user.setUrl(url);
			user.setName(name.first().text());
		}

		Elements sex = userElement.select(">img");
		if (!sex.isEmpty()) {
			user.setSex(sex.first().attr("title"));
		}

		Elements level = userElement.select("div.smalltxt p:nth-last-of-type(2)");
		if (!level.isEmpty()) {
			user.setLevel(level.first().text());
		}

		Elements info = userElement.select("div.smalltxt p:last-of-type");
		if (!info.isEmpty()) {
			String text = info.first().html();

			String[] fields = StringUtils.splitByWholeSeparator(text, "<br>");

			if (fields != null) {
				for (String f : fields) {
					if (StringUtils.contains(f, "注册")) {
						user.setRegTime(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.contains(f, "帖子")) {
						String posts = StringUtils.substringBetween(f, ">", "帖 ");
						user.setPosts(posts);
					}

					if (StringUtils.contains(f, "来自")) {
						String from = StringUtils.substringAfter(f, ":");
						String[] area = StringUtils.splitByWholeSeparator(from, "|");

						if (area != null) {
							if (area.length == 1) {
								user.setCity(area[0]);
							}
							if (area.length == 2) {
								user.setProvince(area[0]);
								user.setCity(area[1]);
							}
						}
					}

				}
			}
		}
		return user;
	}
}
