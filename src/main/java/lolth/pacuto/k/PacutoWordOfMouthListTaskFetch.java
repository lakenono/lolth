package lolth.pacuto.k;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.pacuto.k.bean.PacutoWordOfMouthPostBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 解析列表页，获取口碑信息，用户信息入库
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class PacutoWordOfMouthListTaskFetch extends PageParseFetchTaskHandler {

	public PacutoWordOfMouthListTaskFetch() {
		super(PacutoWordOfMouthListTaskProducer.PACUTO_K_POST_LIST);
	}

	public static void main(String[] args) {
		PacutoWordOfMouthListTaskFetch fetch = new PacutoWordOfMouthListTaskFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements mainTables = doc.select("div.main_table.clearfix");
		for (Element mainTable : mainTables) {
			try {
				PacutoWordOfMouthPostBean bean = new PacutoWordOfMouthPostBean();
				bean.setForumId(task.getExtra());
				
				Elements lefts = mainTable.select("div.main_table_left");
				if (!lefts.isEmpty()) {
					parseLeft(lefts.first(), bean);
				}

				Elements rights = mainTable.select("div.main_table_right");
				if (!rights.isEmpty()) {
					parseRight(rights.first(), bean);
				}

				bean.persistOnNotExist();
				log.debug("post : " + bean);
			} catch (Exception e) {
				log.warn("post parse error : ", e);
			}

		}
	}
	
	private void parseLeft(Element left, PacutoWordOfMouthPostBean bean) {
		// user
		Elements userInfo = left.select("div.info>a");
		if (!userInfo.isEmpty()) {
			String url = userInfo.first().absUrl("href");

			String name = userInfo.first().text();

			String userId = url;
			if (StringUtils.endsWith(userId, "/")) {
				userId = StringUtils.removeEnd(userId, "/");
			}
			userId = StringUtils.substringAfterLast(userId, "/");

			bean.setAuthorId(userId);
			bean.setAuthorName(name);
			bean.setAuthorUrl(url);
		}

		// postTime
		Elements postTime = left.select("div.info>p>a");
		if (!postTime.isEmpty()) {
			String postTimeStr = postTime.first().text();
			postTimeStr = StringUtils.substringBefore(postTimeStr, "发表");
			postTimeStr = StringUtils.trim(postTimeStr);

			bean.setPostTime(postTimeStr);
			
			String url = postTime.first().absUrl("href");
			bean.setUrl(url);
			
			String id = StringUtils.substringBetween(url, "view_", ".html");
			bean.setId(id);
		}

		// 评论信息
		Elements trs = left.select("div.car div.tr");
		for (Element tr : trs) {
			String trClass = tr.attr("class");
			// 购车信息
			if (StringUtils.equals(trClass, "tr")) {
				String data = tr.text();
				if (StringUtils.startsWith(data, "购买车型")) {
					bean.setCar(StringUtils.trim(StringUtils.substringAfter(data, "购买车型")));
				}
				if (StringUtils.startsWith(data, "购买时间")) {
					bean.setBuyTime(StringUtils.trim(StringUtils.substringAfter(data, "购买时间")));
				}
				if (StringUtils.startsWith(data, "购买地点")) {
					String buyArea = StringUtils.trim(StringUtils.substringAfter(data, "购买地点"));
					String[] pAndC = StringUtils.splitByWholeSeparator(buyArea, " ", 2);
					if (pAndC.length == 1) {
						bean.setBuyProvince(pAndC[0]);
						bean.setBuyCity(pAndC[0]);
					}
					if (pAndC.length == 2) {
						bean.setBuyProvince(pAndC[0]);
						bean.setBuyCity(pAndC[1]);
					}
				}
				if (StringUtils.startsWith(data, "购买商家")) {
					bean.setSeller(StringUtils.trim(StringUtils.substringAfter(data, "购买商家")));
				}
				if (StringUtils.startsWith(data, "裸车价格")) {
					bean.setPrice(StringUtils.trim(StringUtils.substringAfter(data, "裸车价格")));
				}
				if (StringUtils.startsWith(data, "平均油耗")) {
					bean.setAverageOilCost(StringUtils.trim(StringUtils.substringAfter(data, "平均油耗")));
				}
				if (StringUtils.startsWith(data, "行驶里程")) {
					bean.setRunMiles(StringUtils.trim(StringUtils.substringAfter(data, "行驶里程")));
				}
			}

			// 追加信息
			// if (StringUtils.equals(trClass, "tr zjbg")) {
			// String data = tr.text();
			// System.out.println(data);
			// if (StringUtils.startsWith(data, "平均油耗")) {
			// bean.setAverageOilCost(StringUtils.trim(StringUtils.substringAfter(data,
			// "平均油耗")));
			// }
			// }

			// 综合评分
			if (StringUtils.equals(trClass, "tr clearfix")) {
				Elements scores = tr.select("div.scoreAreaTr");

				for (Element score : scores) {
					String data = score.text();
					if (StringUtils.startsWith(data, "外观")) {
						bean.setExteriorScores(StringUtils.trim(StringUtils.substringAfter(data, "外观")));
					}
					if (StringUtils.startsWith(data, "内饰")) {
						bean.setInteriorScores(StringUtils.trim(StringUtils.substringAfter(data, "内饰")));
					}
					if (StringUtils.startsWith(data, "空间")) {
						bean.setSpaceScores(StringUtils.trim(StringUtils.substringAfter(data, "空间")));
					}
					if (StringUtils.startsWith(data, "配置")) {
						bean.setConfigScores(StringUtils.trim(StringUtils.substringAfter(data, "配置")));
					}
					if (StringUtils.startsWith(data, "动力")) {
						bean.setPowerScores(StringUtils.trim(StringUtils.substringAfter(data, "动力")));
					}
					if (StringUtils.startsWith(data, "越野")) {
						bean.setOffRoadScores(StringUtils.trim(StringUtils.substringAfter(data, "越野")));
					}
					if (StringUtils.startsWith(data, "油耗")) {
						bean.setOilCostScores(StringUtils.trim(StringUtils.substringAfter(data, "油耗")));
					}
					if (StringUtils.startsWith(data, "舒适")) {
						bean.setComfortScores(StringUtils.trim(StringUtils.substringAfter(data, "舒适")));
					}
				}
			}
		}
	}

	private void parseRight(Element right, PacutoWordOfMouthPostBean bean) {
		// 车主印象
		Element impression = right.getElementById("czyxsx");
		if (impression != null) {
			bean.setImpression(impression.text());
		}

		// 追加评价
		Elements appendDivs = right.select("div.zjdp_text div.section");
		for (Element append : appendDivs) {
			String data = append.text();
			if (StringUtils.startsWith(data, "油耗：")) {
				bean.setOilCostAppend(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "保养：")) {
				bean.setMaintenanceAppend(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "售后：")) {
				bean.setCustomerServiceAppend(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "质量：")) {
				bean.setQualityAppend(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "其他：")) {
				bean.setOtherAppend(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}

		// 评价
		Elements commentDivs = right.select("div.table_text div.section");
		for (Element comment : commentDivs) {
			String data = comment.text();
			if (StringUtils.startsWith(data, "优点：")) {
				bean.setAdvantage(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "缺点：")) {
				bean.setShortcoming(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "外观：")) {
				bean.setExteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "内饰：")) {
				bean.setInteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "空间：")) {
				bean.setSpaceComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "配置：")) {
				bean.setConfigComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "动力：")) {
				bean.setPowerComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "越野：")) {
				bean.setOffRoadComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "油耗：")) {
				bean.setOilCostComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
			if (StringUtils.startsWith(data, "舒适：")) {
				bean.setComfortComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}

	}
}
