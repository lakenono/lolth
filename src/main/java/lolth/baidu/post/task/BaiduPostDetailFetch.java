package lolth.baidu.post.task;

import java.io.IOException;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.baidu.fetch.BaiduFetcher;
import lolth.baidu.post.bean.BaiduPostBean;
import lolth.baidu.post.bean.BaiduPostUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class BaiduPostDetailFetch extends PageParseFetchTaskHandler {

	public static void main(String[] args) throws Exception {
		// 找到生产者提交的队列
		String taskQueueName = BaiduPostListFetch.BAIDU_POST_DETAIL;
		// 执行分布式抓取
		BaiduPostDetailFetch fetch = new BaiduPostDetailFetch(taskQueueName);
		fetch.setSleep(3000);
		fetch.run();
	}

	public BaiduPostDetailFetch(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) {
		try {
			BaiduPostBean postBean = getBaiduPostBean(doc, task);
			BaiduPostUserBean userBean = getBaiduUserBean(doc, task, postBean);

			postBean.persistOnNotExist();
			log.debug("Save BaiduPostUserBean {}", postBean);

			if (userBean != null) {
				userBean.persistOnNotExist();

				log.debug("Save BaiduPostUserBean {}", userBean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BaiduPostUserBean getBaiduUserBean(Document doc, FetchTask task, BaiduPostBean postBean) {

		BaiduPostUserBean userBean = null;

		Elements userDiv = doc.select("div.l_post.l_post_bright.noborder");

		if (userDiv.size() > 0) {

			userBean = new BaiduPostUserBean();
			String postInfo = userDiv.first().attr("data-field");

			JSONObject json = JSON.parseObject(postInfo);

			JSONObject user = json.getJSONObject("author");
			userBean.setId(user.getString("user_id"));
			userBean.setUrl("http://tieba.baidu.com/home/main?un=" + user.getString("name_u"));

			String sex = user.getString("user_sex");
			if ("0".equals(sex) || "1".equals(sex)) {
				userBean.setSex("男");
			} else if ("2".equals(sex)) {
				userBean.setSex("女");
			}

			JSONObject content = json.getJSONObject("content");
			String postTime = content.getString("date");

			postBean.setPostTime(postTime);
			postBean.setUserId(userBean.getId());
		}
		return userBean;
	}

	private BaiduPostBean getBaiduPostBean(Document doc, FetchTask task) {

		// ------------------------帖子--------------------------------------
		BaiduPostBean post = new BaiduPostBean();

		post.setId(StringUtils.substringAfterLast(task.getUrl(), "/"));
		post.setUrl(task.getUrl());
		post.setKeyword(task.getName());

		// 板块
		Elements forum = doc.select("a.plat_title_h3");
		if (forum.size() > 0) {
			post.setForum(forum.first().text());
		}

		// 回复数
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
			Elements title = leftSection.select("h1.core_title_txt");
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

			// postTime
			Elements postTimeUl = leftSection.first().select("ul.p_tail");
			if (postTimeUl.size() > 0) {
				String postTime = postTimeUl.first().text();
				postTime = StringUtils.substringAfter(postTime, "1楼");
				post.setPostTime(postTime);
			}
		}
		return post;
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = BaiduFetcher.fetcher.document(task.getUrl());
		parsePage(doc, task);
		Thread.sleep(sleep);
	}

}
