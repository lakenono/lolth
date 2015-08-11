package lolthx.xcar.bbs;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lakenono.task.FetchTask;
import lolthx.xcar.bean.XCarBBSBean;
import lolthx.xcar.bean.XCarBBSUserBean;

public class XCarBBSListFetch extends DistributedParser {
	
	private static Date start = null;
	private static Date end = null;

	static {
		try {
			start = DateUtils.parseDate("2014-12-31", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-07-01", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public String getQueueName() {
		return "xcar_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		// 获得所有的detail链接地址
		if (StringUtils.isBlank(result)) {
			return;
		}

		XCarBBSUserBean user = null;
		XCarBBSBean bbsBean = null;
				
		Document mainDoc = Jsoup.parse(result);
		Elements elements = mainDoc.select("#F_box_1 table.row");
		
		for (Element element : elements) {
			
			try {
				String postTime = StringUtils.trim(element.select("span.smalltxt.lighttxt").text());
				if (!isTime(postTime)) {
					continue;
				}
				bbsBean = new XCarBBSBean();
				
				String lstUrl = element.select("a.open_view").first().attr("href");
				String sendUrl = "http://www.xcar.com.cn" + lstUrl;

				//搜寻明细信息
				String html = GlobalComponents.fetcher.fetch(sendUrl);
				Document doc = Jsoup.parse(html);
				
				bbsBean.setId(StringUtils.substringAfter(lstUrl, "viewthread.php?tid="));
				bbsBean.setUrl(sendUrl);
				bbsBean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
				bbsBean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));
				bbsBean.setProjectName(task.getProjectName());
				
				Elements postElements = doc.select("div#_img>div.F_box_2");
				if (postElements.size() > 0) {
					Element mainPost = postElements.first();
					// 帖子部分
					// 标题
					Elements title = mainPost.select("h1.title");
					if (!title.isEmpty()) {
						String titleStr = title.first().ownText();
						titleStr = StringUtils.substringAfter(titleStr, ">");
						titleStr = StringUtils.trim(titleStr);
						bbsBean.setTitle(titleStr);
					}

					// 回复
					Elements viewReply = mainPost.select("td.titleStyle3>p>span");
					if (!viewReply.isEmpty()) {
						String viewReplyStr = viewReply.first().text();
						String views = StringUtils.substringBetween(viewReplyStr, "查看：", " ");
						views = StringUtils.trim(views);

						bbsBean.setViews(views);

						String replys = StringUtils.substringBetween(viewReplyStr, "回复：", " ");
						replys = StringUtils.trim(replys);

						bbsBean.setReplys(replys);
					}
				}

				Elements postContent = doc.select("form#delpost>div.F_box_2");
				if (!postContent.isEmpty()) {
					Element mainPost = postContent.first();

					Elements mainContent = mainPost.select("table.t_msg tr");
					if (!mainContent.isEmpty()) {
						// 发表时间
						String postTimeStr = mainContent.first().text();
						postTimeStr = StringUtils.substringAfter(postTimeStr, "发表于");
						//修复发表时间
						postTimeStr = StringUtils.substringBefore(postTimeStr, "|");
						postTimeStr = StringUtils.normalizeSpace(postTimeStr);
						postTimeStr = StringUtils.trim(postTimeStr);
						bbsBean.setPostTime(postTimeStr);
					}

					// 内容
					Elements text = mainPost.select("td.line");
					if (!text.isEmpty()) {
						bbsBean.setText(text.text());
					}

					// 用户部分
					Elements userTds = mainPost.select("td.t_user");
					if (userTds.size() > 0) {
						user = getUser(userTds.first(), task);
					}

				}

				if (user != null) {
					user.saveOnNotExist();
					bbsBean.setAuthorId(user.getId());
				}

				if (bbsBean != null) {
					bbsBean.saveOnNotExist();
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}

	}
	
	private XCarBBSUserBean getUser(Element userElement, Task task) {
		XCarBBSUserBean user = new XCarBBSUserBean();

		Elements name = userElement.select("a.bold");
		if (!name.isEmpty()) {
			String url = name.first().absUrl("href");

			user.setId(HttpURLUtils.getUrlParams(url, "utf-8").get("uid"));
			user.setUrl(url);
			user.setName(name.first().text());
		}

		Elements sex = userElement.select(">img");
		if (!sex.isEmpty()) {
			user.setSex(sex.first().attr("title"));
		}

		Elements level = userElement.select("div.smalltxt p:nth-last-of-type(2)");
		if (!level.isEmpty()) {
			user.setLevel(level.first().text());
		}

		Elements info = userElement.select("div.smalltxt p:last-of-type");
		if (!info.isEmpty()) {
			String text = info.first().html();

			String[] fields = StringUtils.splitByWholeSeparator(text, "<br>");

			if (fields != null) {
				for (String f : fields) {
					if (StringUtils.contains(f, "注册")) {
						user.setRegTime(StringUtils.substringAfter(f, ":"));
					}

					if (StringUtils.contains(f, "帖子")) {
						String posts = StringUtils.substringBetween(f, ">", "帖 ");
						user.setPosts(posts);
					}

					if (StringUtils.contains(f, "来自")) {
						String from = StringUtils.substringAfter(f, ":");
						String[] area = StringUtils.splitByWholeSeparator(from, "|");

						if (area != null) {
							if (area.length == 1) {
								user.setCity(area[0]);
							}
							if (area.length == 2) {
								user.setProvince(area[0]);
								user.setCity(area[1]);
							}
						}
					}

				}
			}
		}
		return user;
	}
	
	private boolean isTime(String time) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
			return between(start, end, srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}
	
	public static void main(String args[]){
		//for(int i =1; i <= 4 ;i++){
			new XCarBBSListFetch().run();
		//}
	}
	
}
