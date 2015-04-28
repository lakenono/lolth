package lolth.xcar.k;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class XCarWordOfMouthListTaskProducer extends PagingFetchTaskProducer {
	private static final String XCAR_K_LIST_URL_TEMPLATE = "http://newcar.xcar.com.cn/{0}/review/0/0_{1}.htm";

	public static final String XCAR_K_LIST = "xcar_k_list";

	private String keyword;
	private String brandId;

	public XCarWordOfMouthListTaskProducer(String keyword, String brandId) {
		super(XCAR_K_LIST);
		this.keyword = keyword;
		this.brandId = brandId;
	}

	public static void main(String[] args) {
		// 雪佛兰
		String keyword = "chevrolet";
		/*
		 * 创酷 ：http://newcar.xcar.com.cn/2474/review/0.htm
		 * 北京现代：ix25http://newcar.xcar.com.cn/2511/review/0.htm 
		 * 别克昂科拉：http://newcar.xcar.com.cn/1733/review/0.htm
		 * 福特翼搏：http://newcar.xcar.com.cn/1405/review/0.htm 
		 * 标致2008：http://newcar.xcar.com.cn/2431/review/0.htm 
		 * 缤智：http://newcar.xcar.com.cn/2581/review/0.htm
		 */
		String[] brandIds = { "2474", "2511", "1733", "1405", "2431", "2581" };

		for (String id : brandIds) {
			log.info("Handler http://newcar.xcar.com.cn/{}/review/0.htm Start ! ", id);
			XCarWordOfMouthListTaskProducer producer = new XCarWordOfMouthListTaskProducer(keyword, id);
			log.info("Handler http://newcar.xcar.com.cn/{}/review/0.htm Finish ! ", id);
			producer.setSleep(1000);
			producer.run();
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			Elements pages = doc.select("div.pagers a");
			if (pages.size() == 1) {
				return 1;
			}

			if (pages.size() > 2) {
				int maxPageIdx = pages.size() - 2;
				String pageStr = pages.get(maxPageIdx).text();

				return Integer.parseInt(pageStr);
			}

		} catch (Exception e) {
			log.error("Get max page error!", e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(XCAR_K_LIST_URL_TEMPLATE, brandId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(XCAR_K_LIST);
		task.setUrl(url);
		task.setExtra(brandId);
		return task;
	}

}
