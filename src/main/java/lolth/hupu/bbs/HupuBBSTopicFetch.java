package lolth.hupu.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.hupu.bbs.bean.TopicBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class HupuBBSTopicFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "hupu_bbs_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "com/", ".html");
		Document doc = Jsoup.parse(result);
		TopicBean topicBean = new TopicBean();
		// #t_main > div.bbs_head
		Elements head = doc.select("#t_main > div.bbs_head");
		String title = head.select("div.bbs-hd-h1 > h1").text();
		topicBean.setTitle(title);
		topicBean.setId(id);
		topicBean.setKeyword(task.getExtra());
		topicBean.setProjectName(task.getProjectName());
		topicBean.setPlate(task.getExtra());
		String tmp = head.select("div.bbs-hd-h1 > span").text();
		String[] split = tmp.split(" ");
		if (split.length == 2) {
			if (split[0].indexOf("/") > -1) {
				String[] spl = split[0].split("/");
				topicBean.setReply(spl[0].substring(0, spl[0].length() - 2));
				topicBean.setBright(spl[1].substring(0, spl[1].length() - 1));
			} else {
				topicBean.setReply(split[0].substring(0, split[0].length() - 2));
			}
			topicBean.setBrowse(split[1].substring(0, split[1].length() - 2));
		}
		// #tpc
		Elements tpc = doc.select("#tpc");
		String uid = tpc.select("div.user > div").attr("uid");
		topicBean.setUserId(uid);
		topicBean.setUrl(url);
		String time = tpc.select("div.floor_box > div.author > div.left > span.stime").text();
		topicBean.setTime(time);
		String text = tpc.select("div.floor_box > table.case > tbody > tr > td").text();
		topicBean.setText(text);
		log.debug(topicBean.toString());
		topicBean.persistOnNotExist();
	}

}
