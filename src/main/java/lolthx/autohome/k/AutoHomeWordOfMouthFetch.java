package lolthx.autohome.k;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.autohome.k.bean.AutoHomeWordOfMouthBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoHomeWordOfMouthFetch extends DistributedParser  {
	
	private static Date start = null;
	private static Date end = null;

	static {
		try {
			start = DateUtils.parseDate("2014-07-22", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-07-21", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public String getQueueName() {
		return "autohome_kb_list";
	}

	@Override
	//获取页面所有数据，然后保存
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		
		
		Elements elements = doc.select("div.mouthcon.js-koubeidataitembox");
		for (Element element : elements) {
				AutoHomeWordOfMouthBean bean = new AutoHomeWordOfMouthBean();

				// 用户名
				String username = element.select("div.name-text p a").first().ownText();
				bean.setUsername(username);

				// 用户url
				String userUrl = element.select("div.name-text p a").first().attr("href");
				bean.setUserUrl(userUrl);

				// 帖子名称
				if (element.select("div.mouth-main div.cont-title.fn-clear span.mr-20").text().trim().equals("")) {
					bean.setSourceTitle("none");
				} else {
					// 帖子title
					String sourceTitle =element.select("div.mouth-main div.cont-title.fn-clear span.mr-20 a").text().trim();
					bean.setSourceTitle(sourceTitle);
				}

				// 帖子url
				String sourceUrl = element.select("div.mouth-main div.cont-title.fn-clear span.time a").attr("href");
				bean.setSourceUrl(StringUtils.substringBefore(sourceUrl, "?"));
				
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

				bean.saveOnNotExist();
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

	public static void main(String args[]){
		//for(int i =0;i<5;i++){
		AutoHomeWordOfMouthFetch fetch = new AutoHomeWordOfMouthFetch();
		//fetch.useDynamicFetch();
		fetch.run();
		//}
	}

}
