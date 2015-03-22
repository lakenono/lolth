package lolth.tmall.detail.fetch;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.tmall.detail.bean.TmallShopBean;
import lolth.tmall.search.bean.TmallGoodsBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.ResultSetHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class TmallDetailFetch {
	private static final String TMALL_GOODS_DETAIL_URL_PREFIX = "http://detail.tmall.com/item.htm?id=";
	private long taskCount;

	public static void main(String[] args) throws Exception {
		new TmallDetailFetch().run();
	}

	private void run() throws Exception {
		while (true) {
			try {
				List<String[]> tasks = getTask();
				if (tasks.isEmpty()) {
					break;
				}

				for (String[] t : tasks) {
					try {
						TmallShopBean shop = parsePage(TMALL_GOODS_DETAIL_URL_PREFIX + t[0]);
						shop.setId(t[1]);
						shop.persist();

						taskCount++;
						Thread.sleep(10000);
					} catch (Exception e) {
						log.error("{} parse error : ", t[0], e);
					}
				}
			} catch (Exception e) {
				log.error("DB get task fail ! ", e);
				try {
					Thread.sleep(10000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		log.info("TmallDetailFetch finish : {}", taskCount);
	}

	private TmallShopBean parsePage(String url) throws IOException, InterruptedException {
		Document doc = GlobalComponents.fetcher.document(url);

		Elements shopExtra = doc.select("div#header div#headerCon div#shopExtra");
		if (shopExtra.size() == 0) {
			return null;
		}

		Element shop = shopExtra.first();
		TmallShopBean bean = new TmallShopBean();

		// name ,url
		Element nameElement = shop.select("div.slogo a.slogo-shopname").first();
		bean.setName(nameElement.text());
		bean.setUrl(nameElement.attr("href"));

		//
		// rate
		Elements textarea = shop.select("div.extra-info textarea.ks-datalazyload");
		if (textarea.size() > 0) {
			doc = Jsoup.parseBodyFragment(textarea.text());

			Elements desc = doc.getElementsMatchingOwnText("描述相符：");

			if (desc.size() > 0) {
				bean.setDescScore(desc.first().select("a em.count").text());
				bean.setDescLevel(desc.first().select("a span.rateinfo").text());
			}

			Elements service = doc.getElementsMatchingOwnText("服务态度：");
			if (service.size() > 0) {
				bean.setServiceScore(service.first().select("a em.count").text());
				bean.setServiceLevel(service.first().select("a span.rateinfo").text());
			}

			Elements deliver = doc.getElementsMatchingOwnText("发货速度：");
			if (deliver.size() > 0) {
				bean.setDeliverScore(deliver.first().select("a em.count").text());
				bean.setDeliverLevel(deliver.first().select("a span.rateinfo").text());

			}

			// company
			Elements company = doc.getElementsMatchingOwnText("公 司 名：");
			if (company.size() > 0) {
				bean.setCompany(company.first().nextElementSibling().text());
			}

			Elements area = doc.getElementsMatchingOwnText("所 在 地：");
			if (area.size() > 0) {
				bean.setArea(area.first().nextElementSibling().text());
			}
		}
		return bean;
	}

	private List<String[]> getTask() throws Exception {
		String sql = "select max(g.id) id,g.shopId shopId from {0} g where g.shopId not in (select s.id from {1} s) group by g.shopId limit 100";
		sql = MessageFormat.format(sql, BaseBean.getTableName(TmallGoodsBean.class), BaseBean.getTableName(TmallShopBean.class));

		List<String[]> todo = GlobalComponents.db.getRunner().query(sql, new ResultSetHandler<List<String[]>>() {

			@Override
			public List<String[]> handle(ResultSet rs) throws SQLException {
				List<String[]> rsList = new ArrayList<String[]>();
				while (rs.next()) {
					String[] rsStr = new String[2];
					rsStr[0] = rs.getString("id");
					rsStr[1] = rs.getString("shopId");
					rsList.add(rsStr);
				}
				return rsList;
			}
		});
		return todo;
	}
}
