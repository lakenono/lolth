package lolth.official.oppo;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.official.oppo.bean.OppoPostBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 解析列表页
 * 
 * @author shi.lei
 *
 */
public class OppoForumPostListFetch extends PageParseFetchTaskHandler {

	public OppoForumPostListFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		String queueName = OppoForumPostListTaskProducer.OPPO_FORUM_POST_LIST;

		OppoForumPostListFetch fetch = new OppoForumPostListFetch(queueName);

		fetch.setSleep(1000);

		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements tbody = doc.select("form#moderate table tbody");
		if (tbody.size() > 0) {
			String forum = null;
			Elements forumE = doc.select("div.bm.cl div.z");
			if (forumE.size() > 0) {
				forum = forumE.first().text();
				forum = StringUtils.substringAfter(forum, "›");
			}

			for (Element item : tbody) {
				String id = item.attr("id");
				if (StringUtils.isBlank(id)) {
					continue;
				}

				OppoPostBean post = new OppoPostBean();
				id = StringUtils.substringAfterLast(id, "_");
				post.setId(id);

				// 用户ID
				Elements uid = item.select("tr td.h_avatar span a");
				if (uid.size() > 0) {
					String uidStr = uid.first().attr("href");
					uidStr = StringUtils.substringAfterLast(uidStr, "-");
					uidStr = StringUtils.substringBefore(uidStr, ".html");
					if (StringUtils.isNotBlank(uidStr)) {
						post.setAutherId(uidStr);
					}
				}

				// 版块

				// views
				Elements views = item.select("span.views");
				if (views.size() > 0) {
					post.setViews(views.first().text());
				}

				// 主题
				Elements topic = item.select("span.thread_title em a");
				if (topic.size() > 0) {
					post.setTopic(topic.first().text());
				}

				// title
				Elements title = item.select("span.thread_title a.xst");
				if (title.size() > 0) {
					post.setTitle(title.first().attr("title"));
					post.setUrl(title.first().attr("href"));
				}

				// 图片附件
				Elements image = item.getElementsByAttributeValue("alt", "attach_img");
				if (image.size() > 0) {
					post.setImage(image.attr("title"));
				}

				// 等级
				Elements level = item.getElementsByAttributeValue("alt", "heatlevel");
				if (level.size() > 0) {
					post.setLevel(level.first().attr("title"));
				}

				// 是否加分
				Elements bonusPoint = item.getElementsByAttributeValue("alt", "agree");
				if (bonusPoint.size() > 0) {
					post.setBonusPoint(bonusPoint.first().attr("title"));
				}

				// 回复数
				Elements replys = item.select("div.h_thread_i a.xi2");
				if (replys.size() > 0) {
					post.setReplys(replys.first().text());
				}

				// 发帖时间
				Elements author = item.select("div.h_thread_i em i.author");
				if (author.size() > 0) {
					Elements postTime = author.first().parent().getElementsByTag("span");
					if (postTime.size() > 0) {
						post.setPostTime(postTime.first().text());
					}
				}

				post.setForum(forum);
				post.setKeyword("oppo");

				post.persistOnNotExist();
			}
		}
	}

}
