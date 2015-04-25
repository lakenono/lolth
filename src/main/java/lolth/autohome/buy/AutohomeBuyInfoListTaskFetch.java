package lolth.autohome.buy;

import java.io.IOException;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.selenium.SeleniumHttpFetcher;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.autohome.buy.bean.AutohomeBuyInfoBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class AutohomeBuyInfoListTaskFetch extends PageParseFetchTaskHandler {

	public AutohomeBuyInfoListTaskFetch() {
		super(AutohomeBuyInfoListTaskProuducer.AUTOHOME_BUY_INFO_LIST);
	}

	public static void main(String[] args) throws Exception {
		AutohomeBuyInfoListTaskFetch fetch = new AutohomeBuyInfoListTaskFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements lis = doc.select("li.price-item");

		for (Element li : lis) {
			AutohomeBuyInfoBean bean = new AutohomeBuyInfoBean();
			bean.setUrl(task.getUrl());
			bean.setForumId(task.getExtra());

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

			// 发表时间
			Elements postTime = li.select("div.user-name span");
			if (!postTime.isEmpty()) {
				bean.setPostTime(StringUtils.trim(StringUtils.substringBefore(postTime.first().text(), "发表")));
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
					// Elements sellerAddress = dataLi.select("em.phone-num");

				}
				if (StringUtils.startsWith(data, "购买感受")) {
					bean.setBuyFeeling(StringUtils.trim(StringUtils.substringAfter(data, "：")));
				}
			}

			log.debug("Bean : {}", bean);

			bean.persistOnNotExist();
		}
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		HttpFetcher httpFetcher = new SeleniumHttpFetcher();
		byte[] content = httpFetcher.run(task.getUrl());

		Document doc = Jsoup.parse(new String(content), "UTF-8");

		parsePage(doc, task);

		httpFetcher.close();
	}

}
