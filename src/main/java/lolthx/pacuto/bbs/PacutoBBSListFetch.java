package lolthx.pacuto.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.pacuto.bean.PacutoBBSBean;
import lolthx.pacuto.bean.PacutoBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PacutoBBSListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "pacuto_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		Elements items = doc.select("td.folder a");

		for (Element item : items) {
			try {
				PacutoBBSBean bbsBean = null;
				PacutoBBSUserBean userBean = null;

				// url
				String sendUrl = "";
				sendUrl = item.absUrl("href");

				String type = "";
				type = item.attr("title");

				// 搜寻明细信息
				Thread.sleep(1000);
				String html = GlobalComponents.fetcher.fetch(sendUrl);
				Document docDetail = Jsoup.parse(html);

				Elements postList = docDetail.select("div.psot_wrap_first");

				if (!postList.isEmpty()) {

					// user对象
					userBean = new PacutoBBSUserBean();

					// userId name url
					Elements userInfoElements = postList.select("td.post_left p.uName>a");
					if (!userInfoElements.isEmpty()) {
						String url = userInfoElements.first().absUrl("href");
						userBean.setUrl(url);
						String userId = StringUtils.substringBetween(url, "http://my.pcauto.com.cn/", "/forum/");
						userBean.setId(userId);
						String name = userInfoElements.first().text();
						userBean.setName(name);

					}

					// city
					Elements areaElements = postList.select("td.post_left div.floor-userInfo div.user_info li");
					if (!areaElements.isEmpty()) {
						for (Element liElement : areaElements) {
							String dataElement = liElement.text();
							if (dataElement.startsWith("地区")) {
								String dataResult = StringUtils.substringAfter(dataElement, "： ");
								userBean.setCity(dataResult);
							}
						}
					}

					// post对象
					bbsBean = new PacutoBBSBean();
					// content
					Elements contentElements = postList.select("td.post_right div.post_main");
					if (!contentElements.isEmpty()) {
						String content = contentElements.first().text();
						bbsBean.setContent(content);
					}

					// postTime
					Elements postTimeElements = postList.select("td.post_right div.post_time");
					if (!postTimeElements.isEmpty()) {
						String postTime = StringUtils.substringAfter(postTimeElements.first().text(), "发表于");
						bbsBean.setPostTime(postTime);
					}

					// views replys
					Elements overView = docDetail.select("td.post_left p.overView");
					if (!overView.isEmpty()) {
						String viewReply = overView.first().text();
						String views = StringUtils.substringBetween(viewReply, "查看：", " ");
						String replys = StringUtils.substringAfter(viewReply, "回复：");
						bbsBean.setViews(views);
						bbsBean.setReplys(replys);
					}

					// title
					Elements titleElements = docDetail.select("td.post_right div.post_r_tit>h1");
					if (!titleElements.isEmpty()) {
						String title = titleElements.first().text();
						bbsBean.setTitle(title);
					}

					// id
					String postUrl = sendUrl;
					String postId = StringUtils.substringBetween(postUrl, "http://bbs.pcauto.com.cn/topic-", ".html");
					// bbsBean.setId(postId);
					bbsBean.setAuthorId(userBean.getId());

				}

				if (bbsBean != null) {
					String id = StringUtils.substringBetween(sendUrl, "-", ".html");
					bbsBean.setId(id);
					bbsBean.setUrl(sendUrl);

					String extra[] = StringUtils.splitByWholeSeparator(task.getExtra(), ":");
					bbsBean.setForumId(extra[0]);
					bbsBean.setKeyword(extra[1]);
					bbsBean.setProjectName(task.getProjectName());
					bbsBean.setType(type);

					if (userBean != null && StringUtils.isNotBlank(userBean.getId())) {
						userBean.saveOnNotExist();
						bbsBean.setAuthorId(userBean.getId());
					}

					bbsBean.saveOnNotExist();
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public static void main(String args[]) {
		for (int i = 1; i <= 10; i++) {
			new PacutoBBSListFetch().run();
		}
	}

}
