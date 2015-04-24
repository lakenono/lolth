package lolth.bitauto.bbs;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.bitauto.bean.BitautoBBSPostBean;
import lolth.bitauto.bean.BitautoBBSUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class BitautoBBSDetailFetch extends PageParseFetchTaskHandler {

	public BitautoBBSDetailFetch() {
		super(BitautoBBSDetailTaskProducer.BITAUTO_BBS_POST_DETAIL);
	}

	public static void main(String[] args) {
		BitautoBBSDetailFetch fetch = new BitautoBBSDetailFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {

		// 帖子信息
		BitautoBBSPostBean post = null;

		// 用户信息
		BitautoBBSUserBean user = null;

		Elements postRight = doc.select("div.postcont_list.post_fist div.postright");
		if (!postRight.isEmpty()) {
			post = new BitautoBBSPostBean();

			// Title
			Elements titleElements = doc.select("div.title_box>h1");
			if (!titleElements.isEmpty()) {
				post.setTitle(titleElements.first().text());
			}

			// Views
			// Replys
			Elements contents = doc.select("div.title_box>span");

			if (!contents.isEmpty()) {
				String str = contents.first().text();
				String replys = StringUtils.substringBefore(str, "/");
				String views = StringUtils.substringAfter(str, "/");
				post.setViews(views);
				post.setReplys(replys);
			}

			// postTime
			Elements postTimeElements = doc.select("div.time_box>span");

			if (!postTimeElements.isEmpty()) {
				String PostTime = postTimeElements.first().text();
				String ActuallyPostTime = StringUtils.substringAfter(PostTime, "发表于");
				post.setPostTime(ActuallyPostTime);
			}

			// content
			Elements contentElements = doc.select("div.post_width");

			if (!contentElements.isEmpty()) {
				post.setContent(contentElements.first().text());
			}

		}

		Elements postLeft = doc.select("div.postcont_list.post_fist div.postcont_border div#postleft1.postleft");
		if (!postLeft.isEmpty()) {
			user = new BitautoBBSUserBean();

			Elements authorIdELements = doc.select("div.user_name a.mingzi");
			// id url
			if (!authorIdELements.isEmpty()) {
				String url = authorIdELements.first().absUrl("href");
				String authorId = StringUtils.substringBetween(url, "http://i.yiche.com/", "/");
				user.setId(authorId);
				user.setUrl(url);
			}

			// name
			Elements nameElements = doc.select("div.user_name a.mingzi");
			if (!nameElements.isEmpty()) {
				user.setName(nameElements.first().text());
			}

			// userInfo
			Elements userInfo = doc.select("div.post_fist div.user_info ul li");

			for (Element liElement : userInfo) {
				String data = liElement.text();
				String dataTitle = StringUtils.substringBefore(data, "：");

				if (dataTitle.equals("等 级")) {
					String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
					user.setLevel(dataResult);
				}
				if (dataTitle.equals("帖 子")) {
					String dataResult1 = StringUtils.substringAfter(data, "：");
					String dataResult2 = StringUtils.replace(dataResult1, "精华)", "");
					user.setPosts(StringUtils.trim(StringUtils.substringBefore(dataResult2, "(")));
					user.setElites(StringUtils.trim(StringUtils.substringAfter(dataResult2, "(")));
				}
				if (dataTitle.equals("注 册")) {
					String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
					user.setRegTime(dataResult);
				}
				if (dataTitle.equals("地 区")) {
					String dataResult = StringUtils.substringAfter(data, "：");
					String city = StringUtils.trim(StringUtils.substringAfter(dataResult, " "));
					String province = StringUtils.trim(StringUtils.substringBefore(dataResult, " "));

					user.setCity(city);
					if (StringUtils.isNotBlank(province)) {
						user.setProvince(province);
					} else {
						user.setProvince(city);
					}

				}
				if (dataTitle.equals("车 型")) {
					String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
					user.setCar(dataResult);
				}

			}
		}

		if (post != null) {
			String id = StringUtils.substringBetween(task.getUrl(), "-", ".html");
			post.setKeyword(task.getName());
			post.setId(id);
			post.setUrl(task.getUrl());

			String extra[] = StringUtils.splitByWholeSeparator(task.getExtra(), ",");
			post.setForumId(extra[0]);
			post.setType(extra[1]);

			if (user != null) {
				user.persistOnNotExist();
				post.setAuthorId(user.getId());
			}

			post.persistOnNotExist();
		}

		log.debug("Parse post:{} ", post);
		log.debug("Parse user:{} ", user);

	}

}
