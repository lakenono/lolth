package lolthx.bitauto.bbs;

import java.sql.SQLException;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.bitauto.bean.BitautoBBSCommentBean;
import lolthx.bitauto.bean.BitautoBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoBBSCommentFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "bitauto_bbs_comment";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		String title = doc.select("div.bt_page div.postcontbox div.bbsnamebox div.title strong#TitleTopicSt").text();

		Elements els = doc.select("div.bt_page div.postcontbox div.postcont_list");

		BitautoBBSCommentBean bean = null;
		BitautoBBSUserBean userBean = null;
		for (Element el : els) {
			try {
				if (el.attr("class").contains("post_fist")) {// 如果为正文信息，则跳出，继续执行下一条
					continue;
				}
				bean = new BitautoBBSCommentBean();
				userBean = new BitautoBBSUserBean();

				Element left = el.select("div.postcont_border div.postleft").first();
				Element right = el.select("div.postcont_border div.postright").first();

				

				String floor = right.select("div.post_text  div.floor_box a").text();
				bean.setFloor(floor);

				String id = el.select("a").first().attr("id");
				bean.setId(id+"-"+floor);
				
				String text = right.select("div.post_text div.post_width").text();
				bean.setText(text);

				String postTime = right.select("div.footinfor_box div.time_postcont span[role=postTime]").text();
				bean.setPostTime(postTime);

				String author = left.select("div.user_name a.mingzi").text();
				bean.setAuthor(author);
				userBean.setName(author);

				String authorUrl = left.select("div.user_name a.mingzi").attr("href");
				userBean.setUrl(authorUrl);

				String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");
				bean.setAuthorId(authorId);
				userBean.setId(authorId);
				bean.setUrl(task.getUrl());
				bean.setTitle(title);
				bean.setProjectName(task.getProjectName());
				
				String[] extras = task.getExtra().split(":");
				String forumId = extras[0];
				String keyword = extras[1];
				String tableKey = extras[2];
				bean.setForumId(forumId);
				bean.setKeyword(keyword);
				
				bean.saveOnNotExist(tableKey);

				// parseUser(left,userBean);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	private void parseUser(Element left, BitautoBBSUserBean bean) {

		Elements lis = left.select("div.user_info li");

		for (Element li : lis) {
			String data = li.text();
			String dataTitle = StringUtils.substringBefore(data, "：");

			if (dataTitle.equals("等 级")) {
				String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
				bean.setLevel(dataResult);
			}
			if (StringUtils.startsWith(data, "地 区")) {
				String dataResult = StringUtils.substringAfter(data, "：");
				String city = StringUtils.trim(StringUtils.substringAfter(dataResult, " "));
				String province = StringUtils.trim(StringUtils.substringBefore(dataResult, " "));

				bean.setCity(city);
				if (StringUtils.isNotBlank(province)) {
					bean.setProvince(province);
				} else {
					bean.setProvince(city);
				}

			}
			if (StringUtils.startsWith(data, "车 型")) {
				bean.setCar(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}

		try {
			bean.saveOnNotExist();
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {
		for (int i = 1; i <= 20; i++) {
			new BitautoBBSCommentFetch().run();
		}
	}

}
