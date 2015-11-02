package lolthx.weibo.task;

import java.text.MessageFormat;
import java.util.List;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.weibo.fetch.WeiboSearchFetch;
import lolthx.weibo.utils.WeiboFileUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 微博关注用户列表 注意：在创建任务的时候，需根据projectName创建表，projectName为英文
 * 
 * @author yanghp
 *
 */
@Slf4j
public class WeiboConcernUserTask extends Producer {
	public WeiboSearchFetch search = new WeiboSearchFetch();
	private final String WEIBO_USER_FOLLOWS_URL_TEMPLATE = "http://weibo.cn/{0}/follow?page={1}";
	public static final String WEIBO_USER_CONCERN_LIST = "weibo_user_concern_list";
	private String projectName;
	private String id;
	public String uid;

	public WeiboConcernUserTask(String id, String projectName) {
		super(projectName);
		this.id = id;
		this.uid = id;
		this.projectName = projectName;
	}

	@Override
	public String getQueueName() {
		return WEIBO_USER_CONCERN_LIST;
	}

	@Override
	protected int parse() throws Exception {
		if (!StringUtils.isNumeric(this.id)) {
			this.uid = search.getUid(this.id);
			Thread.sleep(15000);
		}
		String url = buildUrl(1);
		try {
			String cookies = GlobalComponents.authService.getCookies("weibo.cn");
			String page_html = GlobalComponents.jsoupFetcher.fetch(url, cookies, "");
			Document doc = Jsoup.parse(page_html);
			Elements pageList = doc.select("div#pagelist div");
			if (pageList.size() > 0) {
				String pageStr = pageList.first().ownText();
				pageStr = StringUtils.substringBetween(pageStr, "/", "页");
				pageStr = StringUtils.trim(pageStr);

				if (StringUtils.isNumeric(pageStr)) {
					// return Integer.parseInt(pageStr);
					// 最多获取2000个听众
					int max = Integer.parseInt(pageStr);
					if (max > 200) {
						return 200;
					} else {
						return max;
					}
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
		return MessageFormat.format(WEIBO_USER_FOLLOWS_URL_TEMPLATE, uid, String.valueOf(pageNum));
	}

	@Override
	public Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(WEIBO_USER_CONCERN_LIST);
		task.setUrl(url);
		task.setExtra(id);
		return task;
	}

	public static void main(String[] args) throws Exception {
		String dir = Class.class.getResource("/") + "weiboConcernUser";
		dir = StringUtils.substringAfter(dir, ":");
		String file = WeiboFileUtils.rename2Temp(dir);
		if(file == null){
			log.info("no task file,Program exits!!!");
			return;
		}
		log.info("task begin is :{}", file);
		String projectName = WeiboFileUtils.getProjectName(file);
		List<String> readFile = WeiboFileUtils.readFile(file);
		for (String id : readFile) {
			new WeiboConcernUserTask(id, projectName).run();
			Thread.sleep(15000);
		}
		WeiboFileUtils.rename2Done(file);
	}

}
