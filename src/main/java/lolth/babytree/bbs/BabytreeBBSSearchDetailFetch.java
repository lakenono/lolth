package lolth.babytree.bbs;

import java.io.IOException;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.babytree.bbs.bean.BabytreeBBSBean;
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
		if (StringUtils.isBlank(url)) {
			return;
		}
		String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
		babytreeBean.setId(id);
		// 用户主页
		babytreeBean.setUserUrl(url);
		babytreeBean.setKeyword(task.getName());
		// 宝宝圈子
		Elements elements = doc.select("#BreadcrumbHeader > span:nth-child(7) > a");
		if (elements.size() > 0) {
			babytreeBean.setCircle(elements.text());
		}
		// 主题
		elements = doc.select("#DivHbbs > tbody > tr > td > h1");
		if (elements.size() > 0) {
			babytreeBean.setBanner(elements.text());
		}

		// 浏览数和回复数
		elements = doc.select("#DivHbbs > tbody > tr > td > p");
		if (elements.size() > 0) {
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
		//
		String nameUrl = pageBybeOrBbs(doc, babytreeBean);
		if (StringUtils.isNotBlank(nameUrl)) {
			String sex = pageSex(nameUrl);
			babytreeBean.setBabySex(sex);
		}
		log.debug(babytreeBean.toString());
		//进行数据库存储
		boolean exist = babytreeBean.exist();
		if(!exist){
			babytreeBean.persist();
		}
		
	}
	/*
	 * 解析宝宝性别
	 */
	private String pageSex(String nameUrl) {
		String sex = "";
		try {
			Document document = GlobalComponents.fetcher.document(nameUrl);
			Elements select = document.select("#mytree-basic-info > ul > li:nth-child(2) > span");
			if(select.size()>0){
				sex = select.attr("class");
			}
		} catch (Exception e) {
			log.debug("解析宝宝性别出错了,"+e.getMessage()+","+nameUrl);
		}
		return sex;
	}

	/*
	 * 解析楼主和帖子信息
	 */
	private String pageBybeOrBbs(Document doc, BabytreeBBSBean babytreeBean) {
		String nameUrl = "";
		Elements elements = doc.select("#community > div.community-body-wrapper > div.clubTopicSingle > div.clubTopicList > div:nth-child(1)");
		if (elements.size() > 0) {
			// 用户昵称
			Elements select = elements.select("div.clubTopicSinglePost > div.postUserProfile > p > a");
			if (select.size() > 0) {
				nameUrl = select.attr("href");
				babytreeBean.setNickName(select.text());
			}

			// 宝宝状态
			// 宝宝年龄
			select = elements.select("div.clubTopicSinglePost > div.postUserProfile > ul.userProfileDetail > li:nth-child(3)");
			if (select.size() > 0) {
				for (Element element : select) {
					Element child = element.child(0);
					String babyAge = child.text();
					babytreeBean.setBabyAge(babyAge);
					String babyType = child.parent().ownText();
					babytreeBean.setBabyType(babyType);
				}
			}
			// 地域
			select = elements.select("div.clubTopicSinglePost > div.postUserProfile > ul.userProfileDetail > li:nth-child(5)");
			if (select.size() > 0) {
				for (Element element : select) {
					Element child = element.child(0);
					String region = child.text();
					babytreeBean.setRegion(region);
				}
			}
			// 发帖时间
			select = elements.select("div.clubTopicSinglePost > div.postBody > div.post-title > p.postTime");
			if (elements.size() > 0) {
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
			if (elements.size() > 0) {
				babytreeBean.setText(select.text());
			}
		}
		return nameUrl;
	}

	@Override
	public void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.dynamicFetch.document(task.getUrl());
		parsePage(doc, task);
	}

	public static void main(String[] args) {

	}
}
