package lolth.weibo.cn;

import java.io.IOException;

import lakenono.fetch.adv.utils.HttpURLUtils;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.weibo.bean.WeiboUserConcernRefBean;
import lolth.weibo.cn.WeiboUserTaskFetch.WeiboUserTaskProducer;
import lolth.weibo.fetcher.WeiboFetcher;
import lolth.weibo.task.WeiboUserConcernListTaskProducer;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiboUserConcernListFetch extends PageParseFetchTaskHandler {

	private WeiboUserTaskProducer userTaskProducer = null;

	public WeiboUserConcernListFetch() {
		super(WeiboUserConcernListTaskProducer.WEIBO_USER_CONCERN_LIST);
		userTaskProducer = new WeiboUserTaskProducer();
	}
	
	public static void main(String[] args) {
		WeiboUserConcernListFetch fetch = new WeiboUserConcernListFetch();
		fetch.setSleep(15000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements aS = doc.select("table a");
		for (Element a : aS) {
			if (StringUtils.equals(a.ownText(), "关注他")) {
				String href = a.absUrl("href");

				String concernUserId = HttpURLUtils.getUrlParams(href, "GBK").get("uid");
				if (StringUtils.isNotBlank(concernUserId)) {
					// 持久化用户，关注对应关系
					new WeiboUserConcernRefBean(task.getExtra(), concernUserId).persistOnNotExist();
					// 推送用户队列
					userTaskProducer.push(concernUserId, task.getName());
				}
			}
		}
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = WeiboFetcher.cnFetcher.fetch(task.getUrl());
		parsePage(doc, task);
	}
	
	

}
