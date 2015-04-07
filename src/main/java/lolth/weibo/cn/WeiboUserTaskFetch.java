package lolth.weibo.cn;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lolth.weibo.bean.WeiboUserBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lolth.weibo.task.WeiboUserTaskProducer;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiboUserTaskFetch extends FetchTaskHandler {

	public WeiboUserTaskFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		String taskQueueName = WeiboUserTaskProducer.WEIBO_USER;
		WeiboUserTaskFetch fetch = new WeiboUserTaskFetch(taskQueueName);
		fetch.setSleep(15000);
		fetch.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		Document doc = WeiboFetcher.cnFetcher.fetch(task.getUrl());
		Elements div = doc.getElementsMatchingOwnText("基本信息");
		if (div.size() > 0) {
			Element c = div.first().nextElementSibling();
			String text = c.html();

			String[] fields = StringUtils.splitByWholeSeparator(text, "\n<br>");
			if (fields != null) {
				WeiboUserBean user = new WeiboUserBean();

				String[] ids = StringUtils.split(task.getExtra(), ',');
				user.setId(ids[0]);
				user.setUid(ids[1]);

				for (String f : fields) {
					if (StringUtils.startsWith(f, "昵称:")) {
						user.setName(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "性别:")) {
						user.setSex(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "地区:")) {
						user.setArea(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "生日:")) {
						user.setBirthday(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "简介:")) {
						user.setSummery(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "认证:")) {
						user.setAuth(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.startsWith(f, "血型:")) {
						user.setBloodType(StringUtils.substringAfter(f, ":"));
					}
				}
				user.persistOnNotExist();
			}
		}
	}

}
