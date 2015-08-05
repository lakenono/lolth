package lolthx.pcbaby.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.pcbaby.bbs.bean.TopicReplyBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class PcBabyBBSTopicReplyFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "pcbaby_bbs_topic_reply";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);

		String url = task.getUrl();
		String topicId = StringUtils.substringBetween(url, "cn/", ".html");
		topicId = StringUtils.substringBeforeLast(topicId, "-");

		Elements elements = doc.select("div#post_list div[class^=\"post_wrap\"]");
		if (elements.isEmpty()) {
			return;
		}
		TopicReplyBean replyBean = null;
		for (Element element : elements) {
			replyBean = new TopicReplyBean();
			replyBean.setUrl(url);
			String id = element.attr("id");
			replyBean.setId(id);
			replyBean.setTopicId(topicId);
			Elements select = element.select("td.post_right");
			if (!select.isEmpty()) {
				if (select.select("div.post_info").text().indexOf("楼主") > -1) {
					continue;
				}
				// 回复
				replyBean.setText(select.select("div.replyBody").text());
			}
			select = element.select("th.post_left");
			parseLeft(select, replyBean);
			log.debug(replyBean.toString());
			replyBean.persistOnNotExist();
			//
			String userUrl = "http://my.pcbaby.com.cn/" + replyBean.getUserId() + "/home/";
			Task newTask = buildTask(userUrl, "pcbaby_userinfo", task);
			Queue.push(newTask);
		}
	}

	private void parseLeft(Elements select, TopicReplyBean replyBean) {
		if (select.isEmpty()) {
			return;
		}
		// 用户昵称和id
		Elements tmp = select.select("dt.fb a");
		if (!tmp.isEmpty()) {
			replyBean.setNickName(tmp.text());
			String tm = tmp.attr("href");
			String id = StringUtils.substringBetween(tm, "id/", "/bbs");
			replyBean.setUserId(id);
		}

	}

}
