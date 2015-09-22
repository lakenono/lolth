package lolthx.weibo.fetch;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolthx.weibo.bean.WeiboUserConcernRefBean;
import lolthx.weibo.task.WeiboConcernUserTask;
import lombok.extern.slf4j.Slf4j;

/**
 * 微博用户关联抓取
 * 
 * @author yanghp
 *
 */
@Slf4j
public class WeiboConcernUserFetch extends DistributedParser {
	private boolean isMQ = true;
	public WeiboConcernUserFetch(boolean isMQ){
		this.isMQ = isMQ;
	}
	private WeiboSearchFetch weibo = new WeiboSearchFetch();
	

	@Override
	public String getQueueName() {
		return WeiboConcernUserTask.WEIBO_USER_CONCERN_LIST;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			log.info("weibo concern user result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements aS = doc.select("table");
		for (Element a : aS) {
			Elements select = a.select("td a");
			String name = "";
			if(select.size()>2){
				name = select.get(1).text();
			}
			String url = a.select("a").first().absUrl("href");
			String href = a.select("a").last().absUrl("href");

			String concernUserId = HttpURLUtils.getUrlParams(href, "GBK").get(
					"uid");
			if (StringUtils.isNotBlank(concernUserId)) {
				// 持久化用户，关注对应关系
				// new WeiboUserConcernRefBean(task.getExtra(),
				// concernUserId).persistOnNotExist();
				WeiboUserConcernRefBean bean = new WeiboUserConcernRefBean(
						task.getProjectName());
				bean.setConcernUserId(concernUserId);
				bean.setUserId(task.getExtra());
				bean.setConcernUserURL(url);
				bean.setConcernUserName(name);
				bean.saveOnNotExist();
				// 推送用户队列
				if(isMQ){
				weibo.bulidWeiboUserTask(concernUserId, url,
						task.getProjectName());
				}
			}
		}
	}

	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}
	public static void main(String[] args) {
		new WeiboConcernUserFetch(false).run();
	}
}
