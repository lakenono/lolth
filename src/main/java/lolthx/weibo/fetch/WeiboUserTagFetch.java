package lolthx.weibo.fetch;

import java.sql.SQLException;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 微博用户标签爬取
 * 
 * @author yanghp
 *
 */
@Slf4j
public class WeiboUserTagFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return WeiboUserFetch.WEIBO_USER_TAG;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			log.info("weibo search result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
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
				update(task.getExtra(), tags.toString(),task.getProjectName());
			} else {
				log.debug("{} no tags ! ", task.getExtra());
			}
		}
	}

	private void update(String uid, String tags,String projectName) throws SQLException {
		GlobalComponents.db.getRunner().update(
				"update " + DBBean.getTableName(WeiboUserBean.class,projectName)
						+ " set tags=? where uid=?", tags, uid);
		log.debug("{} : {}", uid, tags);
	}

	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}

}
