package lolthx.baidu.zhidao;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.baidu.zhidao.bean.BaiduZhidaoQuestionBean;

public class BaiduZhidaoListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_zhidao_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		Elements as = doc.select("div.list-inner dl.dl dt a");

		Set<String> urlSet = new LinkedHashSet<>();

		for (Element a : as) {
			String url = a.absUrl("href");
			if (StringUtils.contains(url, "?")) {
				url = StringUtils.substringBefore(url, "?");
			}
			if (StringUtils.isNotBlank(url)) {
				urlSet.add(url);
			}
		}

		for (String url : urlSet) {
				Task newTask = buildTask(url, "baidu_zhidao_detail", task);
				Queue.push(newTask);		
		}
	}

	public static void main(String args[]) {
		for(int i = 1 ; i <= 50 ; i++){
			new BaiduZhidaoListFetch().run();
		}
	}

}
