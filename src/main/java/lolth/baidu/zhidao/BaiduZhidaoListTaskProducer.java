package lolth.baidu.zhidao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class BaiduZhidaoListTaskProducer extends PagingFetchTaskProducer {
	private static final String BAIDU_ZHIDAO_LIST_URL_TEMPLATE = "http://zhidao.baidu.com/search?word={0}&ie=gbk&site=-1&sites=0&date={1}&pn={2}";

	public static final String BAIDU_ZHIDAO_LIST = "baidu_zhidao_list";

	public BaiduZhidaoListTaskProducer(String name, String keyword, String dateType) throws UnsupportedEncodingException {
		super(BAIDU_ZHIDAO_LIST);
		this.name = name;
		this.keyword = keyword;
		this.dateType = dateType;

		this.keywordEncode = URLEncoder.encode(name, "gbk");
	}

	private String name;
	private String keyword;
	private String keywordEncode;

	/*
	 * 时间周期 0: 全部 2： 1周 3: 1月 4: 1年
	 */
	private String dateType;

	public static void main(String[] args) throws Exception {
		String name = "惠氏";
		String[] keywords = { "惠氏启赋","wyeth启赋","雅培菁致","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒" };
		String dateType = "4";

		for (String k : keywords) {
			log.info("{} start!", k);
			BaiduZhidaoListTaskProducer producer = new BaiduZhidaoListTaskProducer(name, k, dateType);
			producer.run();
			log.info("{} finish!", k);
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document document = GlobalComponents.fetcher.document(buildUrl(1));

			// 超过10页——查看尾页标签
			Elements pages = document.select("div.pager a.pager-last");
			if (!pages.isEmpty()) {
				String url = pages.first().absUrl("href");
				String maxPage = HttpURLUtils.getUrlParams(url, "").get("pn");
				if (StringUtils.isNumeric(maxPage)) {
					return Integer.parseInt(maxPage) / 10 + 1;
				} else {
					return 0;
				}
			}

			// 2~10 页——查看下一页标签
			pages = document.select("div.pager a.pager-next");
			if (!pages.isEmpty()) {
				String maxPage = pages.first().previousElementSibling().text();
				if (StringUtils.isNumeric(maxPage)) {
					return Integer.parseInt(maxPage);
				} else {
					return 0;
				}
			}

			// 1 页——根据记录数查看1
			Elements records = document.select("div.list-inner dl.dl");
			if (!records.isEmpty()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(BAIDU_ZHIDAO_LIST_URL_TEMPLATE, keywordEncode, dateType, String.valueOf((pageNum - 1) * 10));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(name);
		fetchTask.setBatchName(BAIDU_ZHIDAO_LIST);
		fetchTask.setUrl(url);
		fetchTask.setExtra(keyword);
		return fetchTask;
	}

}
