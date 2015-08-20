package lolthx.baidu.zhidao;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.utils.HttpURLUtils;

public class BaiduZhidaoListProducer extends Producer {

	private static final String BAIDU_ZHIDAO_LIST_URL = "http://zhidao.baidu.com/search?word={0}&ie=gbk&site=-1&sites=0&date={1}&pn={2}";

	private String keyword;
	/*
	 * 时间周期 0: 全部 2： 1周 3: 1月 4: 1年
	 */
	private String dateType;

	public BaiduZhidaoListProducer(String projectName, String keyword, String dateType) {
		super(projectName);
		this.keyword = keyword;
		this.dateType = dateType;
	}

	@Override
	public String getQueueName() {
		return "baidu_zhidao_list";
	}

	@Override
	protected int parse() throws Exception {
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1));
		Document document = Jsoup.parse(html);

		Elements pages = document.select("div.pager a.pager-last");
		if (!pages.isEmpty()) {
			String url = pages.first().attr("href");
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

	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(BAIDU_ZHIDAO_LIST_URL, URLEncoder.encode(keyword, "gbk"), dateType, String.valueOf((pageNum - 1) * 10));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String args[]) {
		String projectName = "baidu zhidao test";
		// String[] ids = {"电动汽车"};
		String[] keywords = { "电动汽车" };
		String dateType = "2";
		for (int i = 0; i < keywords.length; i++) {
			try {
				new BaiduZhidaoListProducer(projectName, keywords[i], dateType).run();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

}
