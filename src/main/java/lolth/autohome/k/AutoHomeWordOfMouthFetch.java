package lolth.autohome.k;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lolth.autohome.k.bean.AutoHomeWordOfMouthBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 用户名
// 用户url
// 点评帖子url
// 点评帖子title
// 用户认证车型
// 购买车辆
// 购买地点
// 购车经销商
// 购买时间
// 裸车购买价
// 油耗
// 目前行驶 
// 评分-空间
// 评分-动力
// 评分-操控
// 评分-油耗
// 评分-舒适性
// 评分-外观
// 评分-内饰
// 评分-性价比
// 购车目的
// 发帖时间
// 帖子内容
// 浏览次数
// 点赞
public class AutoHomeWordOfMouthFetch {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String id;
	private String name;

	public AutoHomeWordOfMouthFetch(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public void run() throws Exception {
		int maxPage = this.getMaxPage();
		this.log.info("start job 0/{}", maxPage);

		for (int i = 1; i <= maxPage; i++) {
			String taskname = MessageFormat.format("autohome-koubei-{0}-{1}-{2}", this.name, maxPage, i);

			if (GlobalComponents.taskService.isCompleted(taskname)) {
				this.log.info("task {} is completed", taskname);
				continue;
			}

			this.log.info("start job {}/{}", i, maxPage);

			String url = this.buildUrl(this.id, i);

			try {
				this.parse(url);
			} catch (Exception e) {
				e.printStackTrace();
			}

			GlobalComponents.taskService.success(taskname);
		}
	}

	private void parse(String url) throws Exception {
		Document document = GlobalComponents.dynamicFetch.document(url);

		Elements elements = document.select("div.mouthcon.js-koubeidataitembox");
		for (Element element : elements) {
			try {
				AutoHomeWordOfMouthBean bean = new AutoHomeWordOfMouthBean();

				// 用户名
				String username = element.select("div.name-text p a").first().ownText();
				bean.setUsername(username);

				// 用户url
				String userUrl = element.select("div.name-text p a").first().attr("href");
				bean.setUserUrl(userUrl);

				// 无帖子信息
				if (element.select("div.usercont-main.fn-clear div.main-text").first().ownText().equals("--")) {
					bean.setSourceUrl("none");
					bean.setSourceTitle("none");
				} else {
					// 帖子url
					String sourceUrl = element.select("div.usercont-main.fn-clear div.main-text p.text-list a").first().attr("href");
					bean.setSourceUrl(sourceUrl);

					// 帖子title
					String sourceTitle = element.select("div.usercont-main.fn-clear div.main-text p.text-list a").first().ownText();
					bean.setSourceTitle(sourceTitle);
				}

				// 无认证车
				if (element.getElementsMatchingOwnText("认证的车：").size() == 0) {
					bean.setAuthCar("none");
				} else {
					String authCar = element.getElementsMatchingOwnText("认证的车：").first().siblingElements().first().text();
					bean.setAuthCar(authCar);
				}

				// 购买车型
				String carType = element.getElementsMatchingOwnText("购买车型").first().siblingElements().first().text();
				bean.setCarType(carType);

				// 购买地点
				String purchasedFrom = element.getElementsMatchingOwnText("购买地点").first().siblingElements().first().text();
				bean.setPurchasedFrom(purchasedFrom);

				// 购车经销商
				if (element.getElementsMatchingOwnText("购车经销商").size() == 0) {
					bean.setDealer("none");
				} else {
					String dealer = element.getElementsMatchingOwnText("购车经销商").first().siblingElements().first().text();
					bean.setDealer(dealer);
				}

				// 购买时间
				String buyTime = element.getElementsMatchingOwnText("购买时间").first().siblingElements().first().text();
				bean.setBuyTime(buyTime);

				// 裸车购买价
				String price = element.getElementsMatchingOwnText("裸车购买价").first().siblingElements().first().text();
				bean.setPrice(price);

				// 油耗 目前行驶
				if (element.getElementsMatchingOwnText("升/百公里").size() == 0) {
					bean.setFuelConsumption("none");
					bean.setKilometre("none");
				} else {
					try {
						String fuelConsumption = element.getElementsMatchingOwnText("升/百公里").first().parent().ownText();
						bean.setFuelConsumption(fuelConsumption);

						String kilometre = element.getElementsMatchingOwnText("升/百公里").first().parent().nextElementSibling().ownText();
						bean.setKilometre(kilometre);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 评分
				Elements gradeElements = element.select("div.position-r");
				for (Element gradeElement : gradeElements) {
					// 0
					if (gradeElement.getElementsMatchingOwnText("空间").size() != 0) {
						String interspaceGrade = gradeElement.getElementsMatchingOwnText("空间").first().siblingElements().first().text();
						bean.setInterspaceGrade(interspaceGrade);
					}

					// 1
					if (gradeElement.getElementsMatchingOwnText("动力").size() != 0) {
						String powerGrade = gradeElement.getElementsMatchingOwnText("动力").first().siblingElements().first().text();
						bean.setPowerGrade(powerGrade);
					}

					// 2
					if (gradeElement.getElementsMatchingOwnText("操控").size() != 0) {
						String manipulationGrade = gradeElement.getElementsMatchingOwnText("操控").first().siblingElements().first().text();
						bean.setManipulationGrade(manipulationGrade);
					}

					// 3
					if (gradeElement.getElementsMatchingOwnText("油耗").size() != 0) {
						String fuelConsumptionGrade = gradeElement.getElementsMatchingOwnText("油耗").first().siblingElements().first().text();
						bean.setFuelConsumptionGrade(fuelConsumptionGrade);
					}

					// 4
					if (gradeElement.getElementsMatchingOwnText("舒适性").size() != 0) {
						String comfortGrade = gradeElement.getElementsMatchingOwnText("舒适性").first().siblingElements().first().text();
						bean.setComfortGrade(comfortGrade);
					}

					// 5
					if (gradeElement.getElementsMatchingOwnText("外观").size() != 0) {
						String appearanceGrade = gradeElement.getElementsMatchingOwnText("外观").first().siblingElements().first().text();
						bean.setAppearanceGrade(appearanceGrade);
					}

					// 6
					if (gradeElement.getElementsMatchingOwnText("内饰").size() != 0) {
						String innerDecorationGrade = gradeElement.getElementsMatchingOwnText("内饰").first().siblingElements().first().text();
						bean.setInnerDecorationGrade(innerDecorationGrade);
					}

					// 7
					if (gradeElement.getElementsMatchingOwnText("性价比").size() != 0) {
						String performancePriceGrade = gradeElement.getElementsMatchingOwnText("性价比").first().siblingElements().first().text();
						bean.setPerformancePriceGrade(performancePriceGrade);
					}

				}

				// 购车目的
				String aims = element.getElementsMatchingOwnText("购车目的").first().siblingElements().first().text();
				bean.setAims(aims);

				// 发帖时间
				String publishTime = element.select("span.time").first().text();
				publishTime = StringUtils.replace(publishTime, " 发表", "");
				bean.setPublishTime(publishTime);

				// 帖子内容
				Elements texts = element.select("div.text-con.height-list div.text-cont");
				if (!texts.isEmpty()) {
					String text = texts.first().html();
					parseComment(text, bean);
				}

				// 追加
				Elements appends = element.select("dd.add-dl-text div.text-height");
				if (!appends.isEmpty()) {
					String appendText = appends.first().html();
					parseAppendComment(appendText, bean);
				}

				// 浏览次数
				String views = element.getElementsMatchingOwnText("人看过").first().child(0).text();
				bean.setViews(views);

				// 点赞
				String likes = element.getElementsMatchingOwnText("人支持该口碑").first().child(0).text();
				bean.setLikes(likes);

				bean.persistOnNotExist();
			} catch (Exception e) {
				this.log.error("解析错误...", e);
			}
		}
	}

	private void parseComment(String text, AutoHomeWordOfMouthBean bean) {
		String[] dataArray = StringUtils.split(text, "\n<br>");
		String key = null;
		StringBuffer content = null;
		for (String data : dataArray) {
			if (StringUtils.startsWith(data, "【")) {
				writeCommentAttribute(key, content, bean);
				key = StringUtils.substringBetween(data, "【", "】");
				content = new StringBuffer();
				content.append(StringUtils.substringAfter(data, "】"));
			} else {
				// 追加内容
				if (content != null) {
					content.append(data);
				}
			}
		}
		// 写最后一项
		writeCommentAttribute(key, content, bean);
	}

	private void writeCommentAttribute(String key, StringBuffer content, AutoHomeWordOfMouthBean bean) {
		if (key == null || content == null) {
			return;
		}

		if (StringUtils.equals(key, "最满意的一点")) {
			bean.setSatisfactoryComment(content.toString());
		}

		if (StringUtils.equals(key, "最不满意的一点")) {
			bean.setUnsatisfactoryComment(content.toString());
		}
		if (StringUtils.equals(key, "空间")) {
			bean.setInterspaceComment(content.toString());
		}
		if (StringUtils.equals(key, "动力")) {
			bean.setPowerComment(content.toString());
		}
		if (StringUtils.equals(key, "操控")) {
			bean.setManipulationComment(content.toString());
		}
		if (StringUtils.equals(key, "油耗")) {
			bean.setFuelConsumptionComment(content.toString());
		}
		if (StringUtils.equals(key, "舒适性")) {
			bean.setComfortComment(content.toString());
		}
		if (StringUtils.equals(key, "外观")) {
			bean.setAppearanceComment(content.toString());
		}
		if (StringUtils.equals(key, "内饰")) {
			bean.setInnerDecorationComment(content.toString());
		}
		if (StringUtils.equals(key, "性价比")) {
			bean.setPerformancePriceComment(content.toString());
		}
		if (StringUtils.equals(key, "其它描述")) {
			bean.setOtherComment(content.toString());
		}
		if (StringUtils.equals(key, "为什么最终选择这款车")) {
			bean.setBuyReasonComment(content.toString());
		}
	}

	private void writeAppendAttribute(String key, StringBuffer content, AutoHomeWordOfMouthBean bean) {
		if (key == null || content == null) {
			return;
		}

		if (StringUtils.equals(key, "油耗")) {
			bean.setFuelConsumptionAppend(content.toString());
		}

		if (StringUtils.equals(key, "保养")) {
			bean.setMaintenanceAppand(content.toString());
		}

		if (StringUtils.equals(key, "故障")) {
			bean.setFaultAppend(content.toString());
		}

		if (StringUtils.equals(key, "吐槽")) {
			bean.setTuCaoAppend(content.toString());
		}
	}

	private void parseAppendComment(String appendText, AutoHomeWordOfMouthBean bean) {
		String[] dataArray = StringUtils.split(appendText, "\n<br>");
		String key = null;
		StringBuffer content = null;
		for (String data : dataArray) {
			if (StringUtils.startsWith(data, "【")) {
				writeAppendAttribute(key, content, bean);
				key = StringUtils.substringBetween(data, "【", "】");
				content = new StringBuffer();
				content.append(StringUtils.substringAfter(data, "】"));
			} else {
				// 追加内容
				if (content != null) {
					content.append(data);
				}
			}
		}
		// 写最后一项
		writeAppendAttribute(key, content, bean);
	}

	private int getMaxPage() throws Exception {
		String url = this.buildUrl(id, 1);
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);
		String ownText = document.select("span.page-item-info").first().ownText();
		String pageNum = StringUtils.substringBetween(ownText, "共", "页");
		return Integer.parseInt(pageNum);
	}

	public String buildUrl(String id, int page) {
		String baseUrl = "http://k.autohome.com.cn/{0}/index_{1}.html";
		return MessageFormat.format(baseUrl, id, String.valueOf(page));
	}

	public static void main(String[] args) throws Exception {
		try {
			// 东风风度MX6 3637 -- 目前没有口碑
			// new WordOfMouth("3637").run();

			// 哈弗H6 2123 http://club.autohome.com.cn/bbs/forum-c-2123-1.html
			// new AutoHomeWordOfMouthFetch("2123", "哈弗H6").run();
			//
			// // 奔腾X80 3000
			// // http://club.autohome.com.cn/bbs/forum-c-3000-1.html
			// new AutoHomeWordOfMouthFetch("3000", "奔腾X80").run();
			//
			// // 长安CS75 3204
			// // http://club.autohome.com.cn/bbs/forum-c-3204-1.html
			// new AutoHomeWordOfMouthFetch("3204", "长安CS75").run();
			//
			// // 传祺GS5 2560
			// // http://club.autohome.com.cn/bbs/forum-c-2560-1.html
			// new AutoHomeWordOfMouthFetch("2560", "传祺GS5").run();

			// 创酷 http://club.autohome.com.cn/bbs/forum-c-3335-1.html
			new AutoHomeWordOfMouthFetch("3335", "创酷").run();
			// 北京现代ix25 http://club.autohome.com.cn/bbs/forum-c-3292-1.html
			new AutoHomeWordOfMouthFetch("3292", "北京现代ix25").run();
			// 别克昂科拉 http://club.autohome.com.cn/bbs/forum-c-2896-1.html
			new AutoHomeWordOfMouthFetch("2896", "别克昂科拉").run();
			// 福特翼搏 http://club.autohome.com.cn/bbs/forum-c-2871-1.html
			new AutoHomeWordOfMouthFetch("2871", "福特翼搏").run();
			// 标致2008 http://club.autohome.com.cn/bbs/forum-c-3234-1.html
			new AutoHomeWordOfMouthFetch("3234", "标致2008").run();
			// 缤智 http://club.autohome.com.cn/bbs/forum-c-3460-1.html
			new AutoHomeWordOfMouthFetch("3460", "缤智").run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
