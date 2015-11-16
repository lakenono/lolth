package lolthx.autohome.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.autohome.bbs.bean.AutoHomeBBSCommentBean;
import lolthx.autohome.bbs.bean.AutoHomeBBSUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class AutoHomeBBSCommentFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "autohome_bbs_comment";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		// 取到所有的回复Div元素
		Elements replyEs = doc.select("div#maxwrap-reply div.clearfix.contstxt.outer-section");

		for (Element el : replyEs) {
			try {
				AutoHomeBBSUserBean userBean = new AutoHomeBBSUserBean();

				AutoHomeBBSCommentBean commentBean = new AutoHomeBBSCommentBean();

				Element conleft = el.select("div.conleft.fl").first();

				parseUser(conleft, userBean);

				Element conright = el.select("div.conright.fl").first();

				String floor = el.attr("id");
				commentBean.setFloor(floor);
				commentBean.setAuthor(userBean.getName());
				commentBean.setUrl(task.getUrl());
				commentBean.setAuthorId(userBean.getId());

				String id = StringUtils.substringBetween(task.getUrl(), "bbs/", ".html");
				commentBean.setId(id);

				String title = doc.select("div#maxwrap-maintopic div#consnav span").last().text();
				commentBean.setTitle(title);

				commentBean.setProjectName(task.getProjectName());

				commentBean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
				commentBean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));

				parseComment(conright, commentBean);

				userBean.saveOnNotExist();

				commentBean.saveOnNotExist();
				
			} catch (Exception e) {
//				e.printStackTrace();
				log.error("handle autohome comment error : {}",e.getMessage(),e);
				continue;
			}

		}

	}

	// set userBean
	private void parseUser(Element els, AutoHomeBBSUserBean userBean) {

		Element userEl = els.select("ul.maxw li.txtcenter.fw a").first();
		String authorUrl = userEl.attr("href");
		userBean.setAuthorUrl(authorUrl);

		String id = StringUtils.substringBetween(authorUrl, "cn/", "/home");
		userBean.setId(id);

		String name = userEl.text();
		userBean.setName(name);

		Elements liEs = els.select("ul.leftlist li");
		String str = "";
		for (Element li : liEs) {
			str = li.text();
			if (str.trim().startsWith("来自")) {
				userBean.setArea(StringUtils.substringAfter(str, "："));
			}
			if (str.trim().startsWith("爱车")) {
				userBean.setCar(StringUtils.substringAfter(str, "："));
			}
			if (str.trim().startsWith("关注")) {
				userBean.setConcern(StringUtils.substringAfter(str, "："));
			}
		}
	}

	// set 评论基本信息
	private void parseComment(Element element, AutoHomeBBSCommentBean commentBean) {
		// text
		String text = element.select("div[xname=content]").first().text();
		{
			Elements imgs = element.select("div[xname=content] img");
			for (Element img : imgs) {
				String attr = img.attr("src");
				text = text + " " + attr;
			}
		}
		commentBean.setText(text);

		// 发布时间
		String postTime = element.select("span[xname=date]").first().ownText();
		commentBean.setPostTime(postTime);

	}

	public static void main(String args[]) {
		for (int i = 1; i <= 100000; i++) {
			new AutoHomeBBSCommentFetch().run();
		}
	}

}
