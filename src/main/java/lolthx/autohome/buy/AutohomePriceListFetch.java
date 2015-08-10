package lolthx.autohome.buy;

import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.autohome.buy.bean.AutohomePriceInfoBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutohomePriceListFetch extends DistributedParser {

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
		return "autohome_price_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		Elements lis = doc.select("li.price-item");

		AutohomePriceInfoBean bean = new AutohomePriceInfoBean();

		for (Element li : lis) {

			try {
				Elements postTimeEl = li.select("div.user-name span");
				String postTime = "";
				if (!postTimeEl.isEmpty()) {
					postTime = StringUtils.trim(StringUtils.substringBefore(postTimeEl.first().text(), "发表").replaceAll(" ", ""));

					if (!isTime(postTime)) {
						continue;
					}
				}
				bean.setPostTime(postTime);
				bean.setUrl(task.getUrl());
				bean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
				bean.setProjectName(task.getProjectName());
				bean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));

				// post id
				Elements id = li.select("div.price-share a.share");
				if (!id.isEmpty()) {
					String idStr = id.first().attr("data-target");
					idStr = StringUtils.substringAfterLast(idStr, "_");
					if (StringUtils.isBlank(idStr)) {
						continue;
					}

					bean.setId(idStr);
				}

				// 用户：
				Elements user = li.select("div.user-name a");
				if (!user.isEmpty()) {
					String userUrl = user.first().absUrl("href");
					String userId = StringUtils.substringAfterLast(userUrl, "/");
					String userName = user.first().text();

					bean.setUserId(userId);
					bean.setUserUrl(userUrl);
					bean.setUserName(userName);
				}

				Elements dataLis = li.select("div.price-item-bd li");
				for (Element dataLi : dataLis) {
					String data = dataLi.text();

					if (StringUtils.startsWith(data, "购买车型")) {
						bean.setCar(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "裸车价")) {
						bean.setPrice(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "指导价")) {
						bean.setGuidePrice(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "合计价格")) {
						bean.setTotalPrice(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "购置税")) {
						bean.setPurchaseTax(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "商业保险")) {
						bean.setCommercialInsurance(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}

					if (StringUtils.startsWith(data, "车船使用税")) {
						bean.setVehicleUseTax(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
					if (StringUtils.startsWith(data, "交强险")) {
						bean.setCompulsoryInsurance(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
					if (StringUtils.startsWith(data, "上牌费用")) {
						bean.setLicenseFee(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
					if (StringUtils.startsWith(data, "促销套餐")) {
						bean.setPromotion(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
					if (StringUtils.startsWith(data, "购车时间")) {
						bean.setBuyTime(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
					if (StringUtils.startsWith(data, "购车地点")) {
						String area = StringUtils.trim(StringUtils.substringAfter(data, "："));
						String[] pAndC = StringUtils.splitByWholeSeparator(area, ",", 2);

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
						Elements level = dataLi.select("span.level");
						// 商家评价
						if (!level.isEmpty()) {
							bean.setSellerComment(level.first().text());
						}

						// 商家信息
						Elements seller = dataLi.select("a.title");
						if (!seller.isEmpty()) {
							String sellerUrl = seller.first().absUrl("href");
							String sellerName = seller.first().text();
							String sellerId = StringUtils.substringAfterLast(sellerUrl, "/");

							bean.setSellerId(sellerId);
							bean.setSellerName(sellerName);
							bean.setSellerUrl(sellerUrl);
						}

						// 商家电话
						Elements sellerPhone = dataLi.select("em.phone-num");
						if (!sellerPhone.isEmpty()) {
							bean.setSellerPhone(sellerPhone.first().text());
						}

						// 商家地址
						// Elements sellerAddress =
						// dataLi.select("em.phone-num");

					}
					if (StringUtils.startsWith(data, "购买感受")) {
						bean.setBuyFeeling(StringUtils.trim(StringUtils.substringAfter(data, "：")));
					}
				}
				bean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
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

	public static void main(String args[]) {
		for (int i = 1; i <= 20; i++) {
			new AutohomePriceListFetch().run();
		}
	}

}
