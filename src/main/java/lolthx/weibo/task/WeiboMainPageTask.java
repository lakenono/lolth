package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 微博用户主页任务
 * @author yanghp
 *
 */
@Slf4j
public class WeiboMainPageTask extends Producer {

	public static final String MAIN_PAGE_QUEUE = "weibo_main_page";
	private final String USER_MAIN_PAGE_URL_TEMPLATE = "http://weibo.cn/{0}?page={1}";
	private String user;
	private String keyword;

	public WeiboMainPageTask(String user, String keyword) {
		super(keyword);
		this.user = user;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return MAIN_PAGE_QUEUE;
	}

	@Override
	protected int parse() throws Exception {
		String url = buildUrl(1);
		try {
			String cookies = GlobalComponents.authService
					.getCookies("weibo.cn");
			// String cookies =
			// "_T_WM=381052f5df15a47db4b6c216d9fa6b8e; SUB=_2A254qy2qDeSRGeNL7FQS9inIyj-IHXVYV7PirDV6PUJbrdANLVPhkW1Mx5Pwf3qtPcXl9Bixn6Md_eO72Q..; gsid_CTandWM=4uDre42b1a7eMv2kMnqKPnoFp6F";
			String page_html = GlobalComponents.jsoupFetcher
					.fetch(url, cookies);
			Document doc = Jsoup.parse(page_html);
			// Thread.sleep(15000);

			if (doc.select("div#pagelist").size() == 0) {
				Elements elements = doc.select("div.c[id]");
				if (elements.isEmpty()) {
					return 0;
				}
				return 1;
			} else {
				String html = doc.select("div#pagelist").first().text();
				String page = StringUtils.substringBetween(html, "/", "页");
				int maxPage = Integer.parseInt(page);
				if (maxPage > 3) {
					maxPage = 3;
				}
				return maxPage;
			}
		} catch (Exception e) {
			log.error("{} get maxPage error : ", url, e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(USER_MAIN_PAGE_URL_TEMPLATE, user,
				String.valueOf(pageNum));
	}

	@Override
	public Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.keyword);
		task.setQueueName(MAIN_PAGE_QUEUE);
		task.setUrl(url);
		task.setExtra(user);
		return task;
	}

}
