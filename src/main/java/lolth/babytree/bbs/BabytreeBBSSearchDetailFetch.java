package lolth.babytree.bbs;

import java.io.IOException;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.babytree.bbs.bean.BabytreeBBSBean;
import lolth.babytree.bbs.bean.BabytreeBabyUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class BabytreeBBSSearchDetailFetch extends PageParseFetchTaskHandler {

	public BabytreeBBSSearchDetailFetch(String taskQueueName) {
		super(taskQueueName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		BabytreeBBSBean babytreeBean = new BabytreeBBSBean();
		String url = task.getUrl();
		String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
		babytreeBean.setId(id);
		// 用户主页
		babytreeBean.setTopicUrl(url);
		babytreeBean.setKeyword(task.getName());
		babytreeBean.setSubjectTask(task.getExtra());
		// 宝宝圈子
		Elements elements = doc.select("#BreadcrumbHeader > span.current");
		if (!elements.isEmpty()) {
			babytreeBean.setCircle(elements.text());
		}
		// 主题
		elements = doc.select("#DivHbbs > tbody h1");
		if (!elements.isEmpty()) {
			babytreeBean.setBanner(elements.text());
		}

		// 浏览数和回复数
		elements = doc.select("#DivHbbs > tbody p");
		if (!elements.isEmpty()) {
			String tmp = elements.text();
			int i1 = tmp.indexOf("浏览");
			int i2 = tmp.indexOf("回复");
			if (i1 > -1) {
				String s1 = tmp.substring(i1 + 3, i2 > -1 ? i2 : tmp.length());
				babytreeBean.setBrowseNum(s1.trim());
			}
			if (i2 > -1) {
				String s2 = tmp.substring(i2 + 3, tmp.length());
				babytreeBean.setAnswerNum(s2.trim());
			}
		}
		BabytreeBabyUserBean babyUserBean = null;
		elements = doc.select("#community > div.community-body-wrapper > div.clubTopicSingle > div.clubTopicList");
		if (!elements.isEmpty()) {
			Element first = elements.first();
			//抓取宝宝信息
			babyUserBean = pageBabyUser(first);
			babytreeBean.setUserId(babyUserBean.getUserId());
			// 发帖时间
			Elements select = first.select("div.postBody > div.post-title > p.postTime");
			if (!select.isEmpty()) {
				String publishTime = select.text();
				String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(publishTime, "|");
				if (tokens.length > 1) {
					publishTime = tokens[0].trim();
				} else {
					publishTime = "";
				}
				babytreeBean.setPublishTime(publishTime);
			}
			// 文章
			select = elements.select("#topic_content");
			if (!select.isEmpty()) {
				babytreeBean.setText(select.text());
			}
		}
		//
		
		log.debug(babyUserBean.toString());
		log.debug(babytreeBean.toString());
		
		// 进行数据库存储
		//宝宝信息
		boolean exist = babyUserBean.exist();
		if (!exist) {
			babyUserBean.persist();
		}
		//帖子信息信息
		exist = babytreeBean.exist();
		if (!exist) {
			babytreeBean.persist();
		}

	}

	/*
	 * 宝宝信息
	 */
	private BabytreeBabyUserBean pageBabyUser(Element element) {
		BabytreeBabyUserBean babyUserBean = new BabytreeBabyUserBean();
		// 用户昵称
		Elements select = element.select("div.postUserProfile > p > a");
		if (!select.isEmpty()) {
			String nameUrl = select.attr("href");
			if (StringUtils.isNotBlank(nameUrl)) {
				String userId = nameUrl.substring(nameUrl.lastIndexOf("/") + 1, nameUrl.length());
				babyUserBean.setUserId(userId.trim());
				String sex = pageSex(nameUrl);
				babyUserBean.setBabySex(sex);
			}
			babyUserBean.setUserUrl(nameUrl);
			babyUserBean.setNickName(select.text());
		}
		// 宝宝状态
		// 宝宝年龄
		select = element.select("div.postUserProfile > ul.userProfileDetail > li:nth-child(3)");
		if (!select.isEmpty()) {
			for (Element et : select) {
				Element child = et.child(0);
				String babyAge = child.text();
				babyUserBean.setBabyAge(babyAge);
				String babyType = child.parent().ownText();
				babyUserBean.setBabyType(babyType);
			}
		}
		// 地域
		select = element.select("div.postUserProfile > ul.userProfileDetail > li:nth-child(5)");
		if (!select.isEmpty()) {
			for (Element et : select) {
				Element child = et.child(0);
				String region = child.text();
				String tmp = child.parent().ownText();
				if ("来自".equals(tmp)) {
					babyUserBean.setRegion(region);
				}
			}
		}
		return babyUserBean;
	}

	/*
	 * 解析宝宝性别
	 */
	private String pageSex(String nameUrl) {
		String sex = "none";
		try {
			Thread.sleep(sleep);
			Document document = GlobalComponents.fetcher.document(nameUrl);
			Elements select = document.select("#mytree-basic-info > ul > li:nth-child(2) > span");
			if (select.size() > 0) {
				sex = select.attr("class");
			}
		} catch (Exception e) {
			log.debug("解析宝宝性别出错了," + e.getMessage() + "," + nameUrl);
		}
		return sex;
	}

	@Override
	public void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		String url = task.getUrl();
		if (StringUtils.isBlank(url)) {
			return;
		}
		Document doc = GlobalComponents.dynamicFetch.document(url);
		parsePage(doc, task);
	}

	public static void main(String[] args) {
		String taskQueueName = BabytreeBBSSearchDetailTaskProducer.BABYTREE_BBS_LIST_DETAIL;
		BabytreeBBSSearchDetailFetch detailFetch = new BabytreeBBSSearchDetailFetch(taskQueueName);
		detailFetch.setSleep(5000);
		detailFetch.run();
	}
}
