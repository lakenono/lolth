package lolthx.weibo.fetch;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboUserBean;
import lombok.extern.slf4j.Slf4j;
/**
 * 微博粉丝数，微博数，关注数抓取
 * @author yanghp
 *
 */
@Slf4j
public class WeiboFansNumFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return WeiboUserFetch.WEIBO_FANS_NUM;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			log.info("weibo main page result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
		String text = doc.select("div.tip2").text();
		if(StringUtils.isNotBlank(text)){
			String profile = StringUtils.substringBetween(text, "博[", "]");
			String follow = StringUtils.substringBetween(text, "注[", "]");
			String fans = StringUtils.substringBetween(text, "丝[", "]");
			updateUserFans(task,profile,follow,fans);
		}
		
	}
	
	private void updateUserFans(Task task, String profile, String follow,
			String fans) throws SQLException {
		GlobalComponents.db.getRunner().update(
				"update " + DBBean.getTableName(WeiboUserBean.class)
						+ " set profile=? ,follow=? ,fans=? where uid=?", profile, follow,fans,task.getExtra());
		log.debug("{} : 微博数{},关注数{},粉丝数{}", task.getExtra(), profile,follow,fans);
	}

	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}
}
