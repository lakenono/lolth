package lolth.bitauto.k;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.bitauto.bbs.BitautoBBSDetailTaskProducer;
import lolth.bitauto.bean.BitautoBBSUserBean;
import lolth.bitauto.bean.BitautoWordOfMouthBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 口碑页解析
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class BitautoWordOfMouthDetialFetch extends PageParseFetchTaskHandler {

	public BitautoWordOfMouthDetialFetch() {
		super(BitautoBBSDetailTaskProducer.BITAUTO_K_POST_DETAIL);
	}

	public static void main(String[] args) {
		BitautoWordOfMouthDetialFetch fetch = new BitautoWordOfMouthDetialFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		// 解析帖子
		BitautoWordOfMouthBean post = parseWordOfMouth(doc);

		// 解析用户
		BitautoBBSUserBean user = parseUser(doc);

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

	private BitautoBBSUserBean parseUser(Document doc) {
		Elements postLefts = doc.select("div.post_fist div.postleft");
		if (postLefts.isEmpty()) {
			return null;
		}
		BitautoBBSUserBean user = new BitautoBBSUserBean();

		Element userElement = postLefts.first();

		// 用户名相关信息
		Elements username = userElement.select("a.mingzi");
		if (!username.isEmpty()) {
			user.setName(username.first().text());

			String url = username.first().absUrl("href");
			user.setUrl(url);

			String id = url;
			if (StringUtils.endsWith(id, "/")) {
				id = StringUtils.removeEnd(id, "/");
			}
			id = StringUtils.substringAfterLast(id, "/");
			user.setId(id);
		}

		// 用戶信息
		Elements userInfoLis = userElement.select("div.user_info li");
		for (Element li : userInfoLis) {
			String data = li.text();

			if (StringUtils.startsWith(data, "等 级：")) {
				user.setLevel(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "帖 子：")) {
				String postStr = StringUtils.trim(StringUtils.substringAfter(data, "："));

				user.setPosts(StringUtils.trim(StringUtils.substringBefore(postStr, "(")));
				user.setElites(StringUtils.substringBetween(postStr, "(", "精华)"));
			}

			if (StringUtils.startsWith(data, "地 区：")) {
				String area = StringUtils.trim(StringUtils.substringAfter(data, "："));
				String[] pAndC = StringUtils.splitByWholeSeparator(area, " ");

				if (pAndC.length == 1) {
					user.setProvince(pAndC[0]);
				}
				if (pAndC.length == 2) {
					user.setProvince(pAndC[0]);
					user.setCity(pAndC[1]);
				}
			}

			if (StringUtils.startsWith(data, "车 型： ")) {
				user.setCar(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "注 册：")) {
				user.setRegTime(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}

		return user;
	}

	// 口碑解析
	private BitautoWordOfMouthBean parseWordOfMouth(Document doc) {
		Elements postRights = doc.select("div.post_fist div.postright");
		if (postRights.isEmpty()) {
			return null;
		}

		BitautoWordOfMouthBean post = new BitautoWordOfMouthBean();

		Element postRight = postRights.first();
		// title
		Elements title = postRight.select("div.title_box>h1");
		if (!title.isEmpty()) {
			post.setTitle(title.first().text());
		}

		// views and replys
		Elements vAndR = postRight.select("div.title_box>span");
		if (!vAndR.isEmpty()) {
			String vAndRStr = vAndR.first().text();
			post.setViews(StringUtils.substringBefore(vAndRStr, "/"));
			post.setReplys(StringUtils.substringAfter(vAndRStr, "/"));
		}

		// post time
		Elements postTime = postRight.select("div.time_box>span[role=postTime]");
		if (!postTime.isEmpty()) {
			String postTimeStr = postTime.first().text();
			post.setPostTime(StringUtils.substringAfter(postTimeStr, "发表于"));
		}

		// content
		Elements content = postRight.select("div.post_width");
		if (!content.isEmpty()) {
			post.setContent(content.first().text());
		}

		// 口碑部分----------------------------------------------------------------------
		parseKoubeiScore(postRight, post);

		parseKoubeComment(postRight, post);

		return post;
	}

	private void parseKoubeComment(Element postRight, BitautoWordOfMouthBean post) {
		Elements kbTable = postRight.select("table.kb_compare3");
		if (kbTable.isEmpty()) {
			return;
		}

		Element kbScore = kbTable.first();

		// 外观
		Element exteriorscores = kbScore.getElementById("exteriorscores");
		if (exteriorscores != null) {
			post.setExteriorScores(exteriorscores.attr("value"));
		}

		// 内饰
		Element interiorscores = kbScore.getElementById("interiorscores");
		if (interiorscores != null) {
			post.setInteriorScores(interiorscores.attr("value"));
		}

		// 空间
		Element spacescores = kbScore.getElementById("spacescores");
		if (spacescores != null) {
			post.setSpaceScores(spacescores.attr("value"));
		}

		// 动力
		Element powerscores = kbScore.getElementById("powerscores");
		if (powerscores != null) {
			post.setPowerScores(powerscores.attr("value"));
		}

		// 操控
		Element operationscores = kbScore.getElementById("operationscores");
		if (operationscores != null) {
			post.setOperationScores(operationscores.attr("value"));
		}

		// 配置
		Element configscores = kbScore.getElementById("configscores");
		if (configscores != null) {
			post.setConfigScores(configscores.attr("value"));
		}

		// 性价比
		Element costperformancescores = kbScore.getElementById("costperformancescores");
		if (costperformancescores != null) {
			post.setCostperformanceScores(costperformancescores.attr("value"));
		}

		// 舒适度
		Element comfortscores = kbScore.getElementById("comfortscores");
		if (comfortscores != null) {
			post.setComfortScores(comfortscores.attr("value"));
		}

	}

	private void parseKoubeiScore(Element postRight, BitautoWordOfMouthBean post) {
		Elements kbDivs = postRight.select("div.koubeilist");
		if (kbDivs.isEmpty()) {
			return;
		}

		Elements dls = kbDivs.first().getElementsByTag("dl");
		for (Element dl : dls) {
			String data = dl.text();

			if (StringUtils.startsWith(data, "外观：")) {
				post.setExteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "内饰：")) {
				post.setInteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "空间：")) {
				post.setSpaceComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "动力：")) {
				post.setPowerComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "操控：")) {
				post.setOperationComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "配置：")) {
				post.setConfigComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "性价比：")) {
				post.setCostperformanceComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "舒适度：")) {
				post.setComfortComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "购车时间：")) {
				post.setBuyTime(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "裸车价格：")) {
				post.setPrice(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "当前里程：")) {
				post.setCurrentMiles(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}
	}
}
