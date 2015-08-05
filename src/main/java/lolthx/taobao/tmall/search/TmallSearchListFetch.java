package lolthx.taobao.tmall.search;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolthx.taobao.tmall.item.bean.TmallItemBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TmallSearchListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "tmall_item_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		TmallItemBean bean = null;
		Elements divElements = doc.select("div#J_ItemList div.product");
		for (Element div : divElements) {

			// id
			String id = div.attr("data-id");
			if (StringUtils.isBlank(id)) {
				continue;
			}
			bean = new TmallItemBean();
			bean.setProjectName(task.getProjectName());
			bean.setKeyword(task.getExtra());
			bean.setItemId(id);

			// title
			// #J_ItemList > div:nth-child(5) > div > p.productTitle > a
			Element title = div.select("p.productTitle > a").first();
			bean.setTitle(title.attr("title"));
			bean.setUrl("http://detail.tmall.com/item.htm?id=" + id);
			// #J_ItemList > div:nth-child(1) > div > div.productShop > a
			String detailUrl = div.select("div.productShop > a").first().attr("href");
			String userId = HttpURLUtils.getUrlParams(detailUrl, "GBK").get("user_number_id");
			bean.setUserId(userId);

			// price #J_ItemList > div:nth-child(1) > div > p.productPrice > em
			String price = div.select("p.productPrice em").attr("title");
			bean.setPrice(price);

			// 月销售
			// #J_ItemList > div:nth-child(1) > div > p.productStatus
			String monthSales = div.select("p.productStatus em").first().text();
			monthSales = StringUtils.substringBefore(monthSales, "笔");
			bean.setMonthSales(monthSales);

			// 评价
			Element comment = div.select("p.productStatus a").first();
			if (comment != null) {
				String comments = comment.text();
				// comments = StringUtils.substringAfter(comments, "评价:");
				bean.setComments(comments);
			}

			try {
				task.setExtra("1");
				if (bean.persistOnNotExist()) {
					Task buildTask = buildTask("https:" + detailUrl, "taobao_item_shop", task);
					Queue.push(buildTask);
				}

				if (StringUtils.isNotBlank(bean.getComments()) && !"0".equals(bean.getComments())) {
					parseComment(bean.getItemId(), bean.getUserId(), bean.getComments(), task);
				}
			} catch (Exception e) {

			}

		}
	}

	private static final String TAMLL_COMMENT_URL = "http://rate.tmall.com/list_detail_rate.htm?itemId={0}&sellerId={1}&currentPage={2}";
	private static final int COMMENT_PAGE_SIZE = 20;

	private void parseComment(String itemId, String userId, String comments, Task task) {
		int count = Integer.parseInt(comments);
		if (count == 0) {
			return;
		}
		// 计算总页数
		int pages = count / COMMENT_PAGE_SIZE + 1;
		// 天猫最多提供99页
		if (pages > 99) {
			pages = 99;
		}

		for (int i = 1; i <= pages; i++) {
			Task buildTask = buildTask(MessageFormat.format(TAMLL_COMMENT_URL, itemId, userId, i), "taobao_item_commons", task);
			Queue.push(buildTask);
		}
	}

}
