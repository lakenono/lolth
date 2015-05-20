package lolth.weibo.cn;

import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lakenono.task.FetchTaskProducer;
import lolth.weibo.bean.WeiboUserBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboUserTagTaskFetch extends FetchTaskHandler {

	public WeiboUserTagTaskFetch() {
		super(WeiboUserTagTaskProducer.WEIBO_USER_TAG);
	}

	public static void main(String[] args) {
		WeiboUserTagTaskFetch fetch = new WeiboUserTagTaskFetch();
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

			String tagStr = tags.toString();
			if (StringUtils.isNoneBlank(tagStr)) {
				update(task.getExtra(), tags.toString());
			}else{
				log.debug("{} no tags ! ", task.getExtra());
			}
		}
	}

	private void update(String uid, String tags) throws SQLException {
		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(WeiboUserBean.class) + " set tags=? where uid=?", tags, uid);
		log.debug("{} : {}", uid, tags);
	}

	public static class WeiboUserTagTaskProducer extends FetchTaskProducer {
		public WeiboUserTagTaskProducer() {
			super(WEIBO_USER_TAG);
		}

		public static final String WEIBO_USER_TAG = "cn_weibo_user_tag";
		private static final String WEIBO_USER_TAG_URL_TEMPLATE = "http://weibo.cn/account/privacy/tags/?uid={0}";

		public void push(String uid, FetchTask frontTask) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
			FetchTask task = buildTask(uid, frontTask);
			saveAndPushTask(task);
		}

		protected FetchTask buildTask(String uid, FetchTask frontTask) {
			FetchTask task = new FetchTask();
			task.setName(frontTask.getName());
			task.setBatchName(WEIBO_USER_TAG);
			task.setUrl(buildUrl(uid));
			task.setExtra(uid);
			return task;
		}

		private String buildUrl(String uid) {
			return MessageFormat.format(WEIBO_USER_TAG_URL_TEMPLATE, uid);
		}
	}
}
