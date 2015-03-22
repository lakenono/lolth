package lolth.tmall.search.fetch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolth.tmall.search.bean.TmallGoodsBean;
import lolth.tmall.search.task.bean.TmallSearchListTaskBean;
import lolth.weibo.task.bean.WeiboTaskBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

@Slf4j
public class TmallSearchListFetch {
	private static final String TMALL_GOODS_DETAIL_URL_PREFIX = "http://detail.tmall.com/item.htm?id=";
	private long tasks;

	public static void main(String[] args) {
		new TmallSearchListFetch().run();
	}

	private void run() {
		while (true) {
			try {
				TmallSearchListTaskBean task = getTask();
				if (task == null) {
					break;
				}

				try {
					if(!isFinish(task)){
						List<TmallGoodsBean> goodsBeanList = parsePage(task.getUrl());

						for (TmallGoodsBean goods : goodsBeanList) {
							try {
								goods.setKeyword(task.getKeyword());
								// 保存
								goods.persist();
								// 产生新的抓取任务
							} catch (Exception e) {
								log.error("", e);
							}
						}

						tasks++;
						task.updateSuccess();
						Thread.sleep(10000);
						log.info("{} success", task.toString());
					}else{
						log.info("task has finish!",task);
					}
				} catch (Exception e) {
					log.error("{} parse error:{}", task.getUrl(), e.getMessage(), e);
				}
			} catch (Exception e) {
				log.error("TmallSearchListFetch get task error : ", e);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		log.info("TmallSearchListFetch task finish ! handle {} task", tasks);
	}

	private List<TmallGoodsBean> parsePage(String url) throws IOException, InterruptedException {
		List<TmallGoodsBean> beanList = new ArrayList<>();

		Document doc = GlobalComponents.fetcher.document(url);

		Elements divElements = doc.select("div#J_ItemList div.product");
		for (Element div : divElements) {
			TmallGoodsBean bean = new TmallGoodsBean();

			// id
			String id = div.attr("data-id");
			bean.setId(id);

			// title
			Element title = div.select("div.productMain div.productInfo div.productTitle h4.proInfo-title a").first();
			bean.setTitle(title.attr("title"));
			bean.setUrl(TMALL_GOODS_DETAIL_URL_PREFIX + id);

			String detailUrl = title.attr("href");
			String shopId = HttpURLUtils.getUrlParams(detailUrl, "GBK").get("user_id");
			bean.setShopId(shopId);

			// price
			String price = div.select("em.proSell-price").first().text();
			bean.setPrice(price);

			// 月销售
			String monthSales = div.select("p.productStatus em").first().text();
			monthSales = StringUtils.substringBefore(monthSales, "笔");
			bean.setMonthSales(monthSales);

			// 评价
			Element comment = div.select("p.productStatus a").first();
			if (comment != null) {
				String comments = comment.text();
				comments = StringUtils.substringAfter(comments, "评价:");
				bean.setComments(comments);
			}

			beanList.add(bean);

		}
		return beanList;
	}

	private TmallSearchListTaskBean getTask() {
		String json = GlobalComponents.jedis.lpop(BaseBean.getTableName(TmallSearchListTaskBean.class));

		if (json == null) {
			return null;
		}

		TmallSearchListTaskBean bean = JSON.parseObject(json, TmallSearchListTaskBean.class);
		return bean;
	}

	private boolean isFinish(TmallSearchListTaskBean task) throws SQLException {
		@SuppressWarnings("unchecked")
		long count = (long)GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallSearchListTaskBean.class) + " where keyword=? and url=? and type=? and status='success'", DB.scaleHandler, task.getKeyword(), task.getUrl(), task.getType());

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
