package lolth.tmall.search.task;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.tmall.search.task.bean.TmallSearchListTaskBean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

@AllArgsConstructor
@Slf4j
public class TmallSearchListTaskBuilder {

	private static final int PAGE_SIZE = 84;
	private static final String SEARCH_URL = "http://list.tmall.com/search_product.htm?s={0}&q={1}&sort=s&style=l";
	private String[] keywords;

	public static void main(String[] args) {
//		String[] keywords = { "火锅调料", "火锅底料", "酸菜鱼调料" };
		String[] keywords = {"惠氏启赋","wyeth启赋","雅培菁致","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒"};
		try {
			new TmallSearchListTaskBuilder(keywords).run();
			log.info("TmallSearchListTaskBuilder add task finish!");
		} catch (Exception e) {
			log.error("TmallSearchListTaskBuilder add task fail!", e);
		}
		log.info("push task finish!");
	}

	private void run() throws Exception {
		cleanMQ();

		for (String k : keywords) {
			buildDB(k);
			pushMQ(k);
		}

	}

	private void buildDB(String keyword) throws Exception {
		int pages = getMaxPage(keyword);
		log.info("{} 共 {} 页", keyword, pages);

		for (int i = 1; i <= pages; i++) {
			TmallSearchListTaskBean task = new TmallSearchListTaskBean();

			task.setUrl(buildUrl(keyword, i));
			task.setKeyword(keyword);
			task.setStatus("todo");
			task.setType("tmall_search_list");

			task.persist();
		}
		log.info("{} buildDB fnish ", keyword);
	}

	private void pushMQ(String keyword) throws Exception {
		// 清空
		List<TmallSearchListTaskBean> tasks = GlobalComponents.db.getRunner().query("select * from " + BaseBean.getTableName(TmallSearchListTaskBean.class) + " where keyword=? and status='todo'", new BeanListHandler<TmallSearchListTaskBean>(TmallSearchListTaskBean.class), keyword);

		for (TmallSearchListTaskBean bean : tasks) {
			// push redis
			log.info("push task {}", bean.toString());
			GlobalComponents.jedis.lpush(BaseBean.getTableName(TmallSearchListTaskBean.class), JSON.toJSONString(bean));
		}
		log.info("{} pushMQ fnish ", keyword);
	}

	private void cleanMQ() throws Exception {
		GlobalComponents.jedis.del(BaseBean.getTableName(TmallSearchListTaskBean.class));
		log.info("TmallSearchListTaskBuilder clean MQ");
	}

	public int getMaxPage(String keyword) throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(keyword, 1));

		Elements pages = doc.select("div.ui-page div.ui-page-wrap b.ui-page-skip form");
		if (pages.size() == 0) {
			return 0;
		} else {
			String text = pages.first().ownText();
			text = StringUtils.substringBetween(text, "共", "页");
			return Integer.parseInt(text.trim());
		}
	}

	public String buildUrl(String keyword, int page) throws Exception {
		int start = (page - 1) * PAGE_SIZE;
		return MessageFormat.format(SEARCH_URL, start + "", URLEncoder.encode(keyword, "GBK"));
	}

}
