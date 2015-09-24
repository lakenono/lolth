package lolthx.jumei.fetch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.jumei.bean.JMGoodsBean;
import lolthx.jumei.bean.JMKoubeiBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class JMKoubeiFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return JMGoodSearchFetch.KOUBEI_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		JSONObject parseObject = JSON.parseObject(result);
		Object object = parseObject.get("product_reviews_url");
		if (null == object) {
			return;
		}
		// 抓取口碑列表url
		String url = object.toString();
		log.debug("fetch jumei koubei url is {}", url);
		Document doc = fetchHTML(url);
		// 获取商品分数等
		String star = doc.select("div.rp_score > span").text();
		star = StringUtils.substringBefore(star, "/");
		Elements select = doc.select("div.score_des a");
		String koubei = "";
		String duanping = "";
		if (!select.isEmpty()) {
			koubei = select.first().text();
			duanping = select.last().text();
		}
		Elements names = doc.select("dl.rp_histogram span.txt");
		Elements sores = doc.select("dl.rp_histogram dt");
		StringBuilder function = new StringBuilder();
		if (names.size() == sores.size()) {
			for (int i = 0; i < names.size(); i++) {
				function.append(names.get(i).text()).append(":").append(sores.get(i).text());
			}
		}
		// 更新商品表
		updateJMGoods(star, koubei, duanping, function.toString(), task);
		// 抓取口碑详情
		fetchKoubei(doc, task);
		Elements pages = doc.select("div.pageSplit a");
		if (null != pages && !pages.isEmpty()) {
			String page = pages.get(pages.size() - 2).text();
			// 多页循环抓取
			for (int i = 2; i <= Integer.parseInt(page); i++) {
				String page_url = StringUtils.substringBeforeLast(url, ".") + "-0-0-0-all-0-" + i + ".html";
				log.debug("koubei fetch page_url is {}", page_url);
				Document document = fetchHTML(page_url);
				fetchKoubei(document, task);
			}
		}
	}

	private Document fetchHTML(String url) throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(url);
		return Jsoup.parse(html);
	}

	private void fetchKoubei(Document doc, Task task) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Elements elements = doc.select("li.pfTrends");
		List<JMKoubeiBean> beans = new ArrayList<JMKoubeiBean>();
		for (Element element : elements) {
			JMKoubeiBean jm = new JMKoubeiBean();
			String about_user = element.select("span.user_attr").text();
			String star = element.select("div.value").attr("style");
			star = StringUtils.substringBetween(star, ":", "p");
			String title = element.select("a.tit").text();
			String text = element.select("div.desc a").text();
			String txtL = element.select("div.txtL").text();
			String time = StringUtils.substringBefore(txtL, "天");
			String readers = StringUtils.substringBetween(txtL, "前", "阅").trim();
			String replys = StringUtils.substringBetween(txtL, "|", "回").trim();
			String usings = StringUtils.substringAfterLast(txtL, "|");
			usings = StringUtils.substringBefore(usings, "有").trim();
			jm.setId(task.getExtra());
			jm.setAbout_user(about_user);
			jm.setReaders(readers);
			jm.setReplys(replys);
			jm.setStar(star);
			jm.setText(text);
			jm.setTime(time);
			jm.setTitle(title);
			jm.setUsings(usings);
			beans.add(jm);
		}
		log.debug("fetch koubei list is :{}", beans.size());
		for (JMKoubeiBean jmKoubeiBean : beans) {
			jmKoubeiBean.saveOnNotExist();
		}
	}

	private void updateJMGoods(String star, String koubei, String duanping, String function, Task task) throws SQLException {
		GlobalComponents.db.getRunner().update("update " + DBBean.getTableName(JMGoodsBean.class) + " set star=? ,koubei=? ,duanping=? ,function=? where id=?", star, koubei, duanping, function, task.getExtra());
		log.debug("{} : 星级{},口碑数{},短评数{},功能{}", task.getExtra(), star, koubei, duanping, function);
	}

}
