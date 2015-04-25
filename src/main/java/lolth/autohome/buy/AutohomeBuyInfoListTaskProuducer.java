package lolth.autohome.buy;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class AutohomeBuyInfoListTaskProuducer extends PagingFetchTaskProducer {
	private static final String AUTOHOME_BUY_INFO_LIST_URL_TEMPLATE = "http://jiage.autohome.com.cn/price/carlist/s-{0}-0-0-0-0-0-{1}";

	public static final String AUTOHOME_BUY_INFO_LIST = "autohome_buy_info_list";

	public AutohomeBuyInfoListTaskProuducer(String name, String forumId) {
		super(AUTOHOME_BUY_INFO_LIST);
		this.name = name;
		this.forumId = forumId;
	}

	private String name;
	private String forumId;

	public static void main(String[] args) {
		String name = "chevrolet";

		/*
		 * 创酷 http://jiage.autohome.com.cn/price/carlist/s-3335#pvareaid=103596
		 * 北京现代six25
		 * http://jiage.autohome.com.cn/price/carlist/s-3292#pvareaid=103596
		 * 别克昂科拉
		 * http://jiage.autohome.com.cn/price/carlist/s-2896#pvareaid=103596
		 * 福特翼搏
		 * http://jiage.autohome.com.cn/price/carlist/s-2871#pvareaid=103596
		 * 标致2008
		 * http://jiage.autohome.com.cn/price/carlist/s-3234#pvareaid=103596 缤智
		 * http://jiage.autohome.com.cn/price/carlist/s-3460#pvareaid=103596
		 */
		String[] forumIds = { "3335", "3292", "2896", "2871", "3234", "3460" };
		for (String id : forumIds) {
			log.info("Handler http://jiage.autohome.com.cn/price/carlist/s-{} start ! ", id);
			AutohomeBuyInfoListTaskProuducer producer = new AutohomeBuyInfoListTaskProuducer(name, id);
			producer.setSleep(1000);
			producer.run();
			log.info("Handler http://jiage.autohome.com.cn/price/carlist/s-{} finish !", id);
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));

			Elements pageE = doc.select("span.page-item-jump");
			if (pageE.isEmpty()) {
				return 0;
			}

			String pages = pageE.first().text();
			pages = StringUtils.substringBetween(pages, "共", "页");
			if (StringUtils.isNumeric(pages)) {
				return Integer.parseInt(pages);
			}
		} catch (Exception e) {
			log.error("Get max page error : ", e);
		}

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(AUTOHOME_BUY_INFO_LIST_URL_TEMPLATE, forumId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(name);
		task.setBatchName(AUTOHOME_BUY_INFO_LIST);
		task.setUrl(url);
		task.setExtra(forumId);
		return task;
	}

}
