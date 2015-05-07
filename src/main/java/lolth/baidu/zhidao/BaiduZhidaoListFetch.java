package lolth.baidu.zhidao;

import java.util.LinkedHashSet;
import java.util.Set;

import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 列表页解析
 * 
 * @author shi.lei
 *
 */
public class BaiduZhidaoListFetch extends PageParseFetchTaskHandler {
	public static final String BAIDU_ZHIDAO_DETAIL = "baidu_zhidao_detail";

	private FetchTaskProducer producer = null;

	public static void main(String[] args) {
		BaiduZhidaoListFetch fetch = new BaiduZhidaoListFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	public BaiduZhidaoListFetch() {
		super(BaiduZhidaoListTaskProducer.BAIDU_ZHIDAO_LIST);
		producer = new FetchTaskProducer(BAIDU_ZHIDAO_DETAIL);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements as = doc.select("div.list-inner dl.dl dt a");

		Set<String> urlSet = new LinkedHashSet<>();

		for (Element a : as) {
			String url = a.absUrl("href");
			if (StringUtils.contains(url, "?")) {
				url = StringUtils.substringBefore(url, "?");
			}

			if (StringUtils.isNotBlank(url)) {
				urlSet.add(url);
			}
		}

		for (String url : urlSet) {
			producer.saveAndPushTask(buildTask(url, task));
		}
	}

	private FetchTask buildTask(String url, FetchTask task) {
		task.setUrl(url);
		task.setBatchName(BAIDU_ZHIDAO_DETAIL);
		return task;
	}

}
