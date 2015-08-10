package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 微博关注用户列表
 * @author yanghp
 *
 */
public class WeiboConcernUserTask extends Producer {

	private final String WEIBO_USER_FOLLOWS_URL_TEMPLATE = "http://weibo.cn/{0}/follow?page={1}";
	public static final String WEIBO_USER_CONCERN_LIST = "weibo_user_concern_list";
	private String projectName;
	private String uid;

	public WeiboConcernUserTask(String uid, String projectName) {
		super(projectName);
		this.uid = uid;
		this.projectName = projectName;
	}

	@Override
	public String getQueueName() {
		return WEIBO_USER_CONCERN_LIST;
	}

	@Override
	protected int parse() throws Exception {
		String url = buildUrl(1);
		try {
			String cookies = GlobalComponents.authService
					.getCookies("weibo.cn");
			String page_html = GlobalComponents.jsoupFetcher
					.fetch(url, cookies);
			Document doc = Jsoup.parse(page_html);
			Elements pageList = doc.select("div#pagelist div");
			if (pageList.size() > 0) {
				String pageStr = pageList.first().ownText();
				pageStr = StringUtils.substringBetween(pageStr, "/", "页");
				pageStr = StringUtils.trim(pageStr);

				if (StringUtils.isNumeric(pageStr)) {
					return Integer.parseInt(pageStr);
				}
			} else {
				Elements concerns = doc.select("table");
				if (!concerns.isEmpty()) {
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(WEIBO_USER_FOLLOWS_URL_TEMPLATE, uid,
				String.valueOf(pageNum));
	}
	
	@Override
	public Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(WEIBO_USER_CONCERN_LIST);
		task.setUrl(url);
		task.setExtra(uid);
		return task;
	}
	
}
