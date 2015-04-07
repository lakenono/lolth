package lolth.weibo.cn;

import java.sql.SQLException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lolth.weibo.bean.WeiboUserBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lolth.weibo.task.WeiboUserTagTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeiboUserTagTaskFetch extends FetchTaskHandler {

	public WeiboUserTagTaskFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) {
		String taskQueueName = WeiboUserTagTaskProducer.WEIBO_USER_TAG;
		WeiboUserTagTaskFetch fetch = new WeiboUserTagTaskFetch(taskQueueName);
		fetch.setSleep(15000);
		fetch.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		Document doc = WeiboFetcher.cnFetcher.fetch(task.getUrl());

		StringBuilder tags = new StringBuilder();

		Elements tagDiv = doc.getElementsContainingOwnText("的标签");

		if (tagDiv.size() > 0) {
			Elements tagElements = tagDiv.first().select("a");

			if (tagElements.size() > 0) {
				for (Element tag : tagElements) {
					tags.append(tag.text()).append(';');
				}
				tags.deleteCharAt(tags.length() - 1);
			}

			update(task.getExtra(), tags.toString());
		}
	}

	private void update(String uid, String tags) throws SQLException {
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(WeiboUserBean.class) + " set tags=? where uid=?", tags, uid);
		log.debug("{} : {}", uid, tags);
	}
}
