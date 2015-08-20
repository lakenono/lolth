package lolthx.bitauto.k;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.bitauto.bean.BitautoWordOfMouthBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoWordOfMouthFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "bitauto_kb_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Date start = task.getStartDate();
		Date end = task.getEndDate();
		
		Document doc = Jsoup.parse(result);
		BitautoWordOfMouthBean bean = null;
		Elements elements = doc.select("div.postscontent div.postslist_xh");

		// 循环列表元素 循环数据 根据elements先存储一部分数据到数据库
		for (Element element : elements) {
			try {
				String postTime = element.select("li.zhhf").first().text();
				if (!isTime(postTime,start,end)) {
					continue;
				}

				bean = new BitautoWordOfMouthBean();
				bean.setPostTime(postTime);

				// title
				String title = element.select("li.bt a span").text().trim();
				bean.setTitle(title);

				// 类型，是否为精品帖子 等
				String type = element.select("li.tu a").attr("class");
				bean.setType(type);

				// url
				String url = element.select("li.bt a").attr("href");
				bean.setUrl(url);

				// 是否必须带thread？主ID
				String id = StringUtils.substringBetween(url, "-", ".html");
				bean.setId(id);

				// 作者
				String author = element.select("li.zz a").text().trim();
				bean.setAuthor(author);

				// 作者url
				String authorUrl = element.select("li.zz a").attr("href");
				String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");

				bean.setAuthorId(authorId);
				bean.setProjectName(task.getProjectName());
				bean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
				bean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));

				Thread.sleep(2000);
				String html = GlobalComponents.fetcher.fetch(bean.getUrl());
				if (StringUtils.isBlank(html)) {
					return;
				}
				Document docDetail = Jsoup.parse(html);

				Elements postRights = docDetail.select("div.post_fist div.postright");
				if (postRights.isEmpty()) {
					return;
				}
				Element postRight = postRights.first();

				BitautoWordOfMouthBean mouthBean = new BitautoWordOfMouthBean();

				// views 点击 and 回复
				String views_replys = docDetail.select("div.title_box span").text();

				// 点击率
				String views = StringUtils.substringAfter(views_replys, "/").trim();
				bean.setViews(views);

				// 回复率
				String replys = StringUtils.substringBefore(views_replys, "/").trim();
				bean.setReplys(replys);

				Elements content = postRight.select("div.post_width");
				if (!content.isEmpty()) {
					bean.setContent(content.first().text());
				}

				carDescription(postRight, bean);

				parseKoubeiScore(postRight, bean);

				parseKoubeComment(postRight, bean);

				bean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	private boolean isTime(String time,Date start,Date end){
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

	public void carDescription(Element postRight, BitautoWordOfMouthBean post) {
		Elements carDess = postRight.select("div.koubei_jia ul li");

		if (carDess.isEmpty()) {
			return;
		}

		for (Element dl : carDess) {
			String data = dl.text();
			if (StringUtils.startsWith(data, "裸车价：")) {
				post.setPrice(StringUtils.trim(StringUtils.substringBetween(data, "：", "|")));
			}
			if (StringUtils.endsWith(data, "购车|")) {
				post.setBuyTime(StringUtils.trim(StringUtils.substringBefore(data, "购车")));
			}
		}
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

	public static void main(String args[]) {
		new BitautoWordOfMouthFetch().run();
	}

}
