package lolthx.baidu.post;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.baidu.post.bean.BaiduPostUserBean;
import lolthx.baidu.post.bean.BaiduPostBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaiduPostListFetch extends DistributedParser {

	private static final String BAIDU_POST_DETAIL_URL_PREFIX = "http://tieba.baidu.com";

	@Override
	public String getQueueName() {
		return "baidu_tieba_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		Set<String> urlSet = new LinkedHashSet<>();

		Elements topicElements = doc.select("a.th_tit");
		if (topicElements.size() > 0) {
			for (Element href : topicElements) {
				urlSet.add(href.attr("href"));
			}
		}

		Elements hrefElements = doc.select("a.j_th_tit");
		if (hrefElements.size() > 0) {
			for (Element href : hrefElements) {
				urlSet.add(BAIDU_POST_DETAIL_URL_PREFIX + href.attr("href"));
			}
		}

		
		for (String url : urlSet) {
			Task newTask = buildTask(url, "baidu_post_detail", task);
			Queue.push(newTask);		
		}

	}


	public static void main(String args[]) {
		new BaiduPostListFetch().run();
	}

}
