package lolthx.weibo.fetch;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolth.weibo.bean.WeiboUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 微博用户爬取，并发送标签任务
 * @author yanghp
 *
 */
@Slf4j
public class WeiboUserFetch extends DistributedParser{
	
	public static final String WEIBO_USER_TAG = "cn_weibo_user_tag";
	private static final String WEIBO_USER_TAG_URL_TEMPLATE = "http://weibo.cn/account/privacy/tags/?uid={0}";

	@Override
	public String getQueueName() {
		return WeiboSearchFetch.USER_QUEUE_NAME;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			log.info("weibo user result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
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

				boolean persist = user.persistOnNotExist();

				//推送标签任务
				if(persist){
					buildUserTagTask(user.getUid(), task);
				}
			}
		}
	}
	private void buildUserTagTask(String uid, Task task) {
		//用不用克隆一个新对象 //TODO
		task.setQueueName(WEIBO_USER_TAG);
		task.setUrl(buildUrl(uid));
		task.setExtra(uid);
		Queue.push(task);
	}
	private String buildUrl(String uid) {
		return MessageFormat.format(WEIBO_USER_TAG_URL_TEMPLATE, uid);
	}

	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}

}
