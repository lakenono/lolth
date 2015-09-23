package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.weibo.fetch.WeiboSearchFetch;

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
		String projectName = "guanzhi_Relaunch";
		String[] ids = { "mrroc	", "5701975466", "5687740949", "5687391614", "5687390129", "5687388022", "5687385509", "5687385388", "5687148214", "5687130005", "5687121329", "5687120919", "5687119731", "5687119232", "5687119071", "5686764637", "5686761786", "5686760072", "5685810898", "5685787607", "5684438092", "5684382137", "5684081649", "5684080502", "5683415520", "5682663895", "5681456558", "5683413504", "717176888", "706786281", "639978991", "601266190", "535676740", "534275677", "534109123", "521444123", "521432234", "470480993", "345201069", "333870922", "278204441", "237897919", "236780654", "234036095", "228280826", "207571555", "52966601", "37904174", "27903816" };
		for (String id : ids) {
			new WeiboConcernUserTask(id, projectName).run();
		}
	}

}
