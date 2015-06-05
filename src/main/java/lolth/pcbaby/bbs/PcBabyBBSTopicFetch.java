package lolth.pcbaby.bbs;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.DistributedPageParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolth.pcbaby.bbs.Bean.TopicBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class PcBabyBBSTopicFetch extends DistributedPageParser {

	private String baseUrl;

	// 抽取发帖时间正则
	Pattern p = Pattern.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}");

	@Override
	public String getQueueName() {
		return "pcbaby_bbs_topic";
	}

	@Override
	public int getMaxPage(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result) || StringUtils.isBlank(baseUrl)) {
			return 0;
		}
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("div.pager > i");
		if (elements.isEmpty()) {
			return 1;
		}
		task.setQueueName("pcbaby_bbs_topic_reply");
		String pageStr = elements.first().text();
		return Integer.parseInt(pageStr);
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(baseUrl, pageNum);
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		TopicBean topicBean = new TopicBean();
		String url = task.getUrl();
		baseUrl = StringUtils.substringBeforeLast(url, ".html") + "-{0}.html";
		String id = StringUtils.substringBetween(url, "cn/", ".html");
		topicBean.setId(id);
		topicBean.setUrl(url);
		topicBean.setKeyword(task.getExtra());
		topicBean.setProjectName(task.getProjectName());

		Document doc = Jsoup.parse(result);
		// 头部信息
		Elements elements = doc.select("div.post_list_top");
		parseHead(elements, topicBean);
		// 楼主帖子左边
		elements = doc.select("div.post_wrap_first th.post_left");
		parseLeft(elements, topicBean);
		// 楼主帖子右边
		elements = doc.select("div.post_wrap_first td.post_right");
		parseRight(elements, topicBean);
		log.debug(topicBean.toString());

		topicBean.persistOnNotExist();
		//
		String userUrl = "http://my.pcbaby.com.cn/" + topicBean.getUserId() + "/home/";
		task.setQueueName("pcbaby_userinfo");
		Task newTask = buildTask(userUrl, task);
		Queue.push(newTask);
		

	}

	private void parseRight(Elements elements, TopicBean topicBean) {
		if (elements.isEmpty()) {
			return;
		}
		// 帖子发表时间
		String time = elements.select("div.post_info").text();
		Matcher m = p.matcher(time);
		while (m.find()) {
			String date = m.group();
			topicBean.setPostTime(date);
		}
		// 楼主的帖子正文
		topicBean.setText(elements.select("div.replyBody").text());

	}

	private void parseLeft(Elements elements, TopicBean topicBean) {
		if (elements.isEmpty()) {
			return;
		}
		// 用户昵称和id
		Elements tmp = elements.select("dt.fb a");
		if (!tmp.isEmpty()) {
			topicBean.setNickName(tmp.text());
			String tm = tmp.attr("href");
			String id = StringUtils.substringBetween(tm, "id/", "/bbs");
			topicBean.setUserId(id);
		}
	}

	private void parseHead(Elements elements, TopicBean topicBean) {
		if (elements.isEmpty()) {
			return;
		}
		Elements tmp = elements.select("p.overView");
		// 查看和回复
		if (!tmp.isEmpty()) {
			Elements spans = tmp.select("span");
			for (Element span : spans) {
				if ("views".equals(span.attr("id"))) {
					topicBean.setViews(span.text());
				} else {
					topicBean.setReply(span.text());
				}
			}
		}
		// 正文
		tmp = elements.select("h1");
		if (!tmp.isEmpty()) {
			topicBean.setTitle(tmp.text());
		}
	}

}
