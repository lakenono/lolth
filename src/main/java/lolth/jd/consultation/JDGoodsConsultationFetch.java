package lolth.jd.consultation;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.jd.consultation.bean.JDGoodsConsultationBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDGoodsConsultationFetch {
	private static final Logger log = LoggerFactory.getLogger(JDGoodsConsultationFetch.class);
	/**
	 * url地址： {0}: goodsId {1}: 页数 {2}： 咨询类型：1 全部，2 商品自选，3 库存配送，4 支付 5 发票保修
	 */
	private static final String urlTemplate = "http://club.jd.com/allconsultations/{0}-{1}-{2}.html";

	private String goodsId;
	private String consultationType;

	private int sleep = 180;

	public JDGoodsConsultationFetch(String goodsId, String consultationType) {
		this.goodsId = goodsId;
		this.consultationType = consultationType;
	}

	public static void main(String[] args) {
		String[] goodsIds = { "323578", "189083" };
		for (String id : goodsIds) {
			// 抓取分类评论
			for (int i = 2; i <= 5; i++) {
				try {
					new JDGoodsConsultationFetch(id, i + "").run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 抓取全部评论
			try {
				new JDGoodsConsultationFetch(id, "1").run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("all task finish!");
	}

	private void run() throws IOException, InterruptedException {
		int maxPage = getMaxPage();

		for (int i = 1; i <= maxPage; i++) {
			String url = buildUrl(this.goodsId, i + "", this.consultationType);
			try {
				List<JDGoodsConsultationBean> beanList = parsePage(url);
				for (JDGoodsConsultationBean bean : beanList) {
					bean.setGoodsId(goodsId);
					bean.setType(consultationType);
					bean.persist();
				}
				log.info("{} success", url);

				Thread.sleep(sleep * 1000);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("{} error", url, e);
			}

		}
	}

	private List<JDGoodsConsultationBean> parsePage(String url) throws IOException, InterruptedException {
		List<JDGoodsConsultationBean> beanList = new ArrayList<>();

		Document doc = GlobalComponents.fetcher.document(url);

		Elements consultationDiv = doc.select("div.right div.Refer_List div.refer");
		
		for (Element e : consultationDiv) {
			JDGoodsConsultationBean bean = new JDGoodsConsultationBean();

			// 用户
			bean.setUser(e.select("div.r_info a").text());
			bean.setUserUrl(e.select("div.r_info a").attr("href"));

			String askTime = e.select("div.r_info").first().ownText();
			askTime = StringUtils.substringAfter(askTime, "网友：");
			bean.setAskTime(askTime);

			// 咨询内容
			bean.setAsk(e.select("dl.ask dd a").text());
			bean.setUrl(e.select("dl.ask dd a" ).attr("href"));

			// 京东回复
			bean.setAnswer(e.select("dl.answer dd").text());

			beanList.add(bean);
		}

		return beanList;
	}

	private int getMaxPage() throws IOException, InterruptedException {
		String url = buildUrl(this.goodsId, "1", this.consultationType);
		Document doc = GlobalComponents.fetcher.document(url);

		Element pageDiv = doc.select("div.Pagination").first();
		Elements nextPage = pageDiv.getElementsMatchingOwnText("下一页");

		if (nextPage.size() != 0) {
			String page = nextPage.first().previousElementSibling().text();
			return Integer.parseInt(page);
		}
		return 1;
	}

	private String buildUrl(String goodsId, String page, String type) {
		return MessageFormat.format(urlTemplate, goodsId, page, type);
	}

}
