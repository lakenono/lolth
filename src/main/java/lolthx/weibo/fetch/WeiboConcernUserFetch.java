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
 * @author yanghp
 *
 */
@Slf4j
public class WeiboConcernUserFetch extends DistributedParser {
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
		Elements aS = doc.select("table a");
		for (Element a : aS) {
			if (StringUtils.equals(a.ownText(), "关注他")) {
				String href = a.absUrl("href");

				String concernUserId = HttpURLUtils.getUrlParams(href, "GBK").get("uid");
				if (StringUtils.isNotBlank(concernUserId)) {
					// 持久化用户，关注对应关系
//					new WeiboUserConcernRefBean(task.getExtra(), concernUserId).persistOnNotExist();
					WeiboUserConcernRefBean bean = new WeiboUserConcernRefBean(task.getProjectName());
					bean.setConcernUserId(concernUserId);
					bean.setUserId(task.getExtra());
					bean.saveOnNotExist();
					// 推送用户队列
					weibo.bulidWeiboUserTask(concernUserId, task.getProjectName());
				}
			}
		}
	}
	
	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}
	
}
