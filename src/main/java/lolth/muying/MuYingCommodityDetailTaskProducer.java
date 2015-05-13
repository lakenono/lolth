package lolth.muying;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
@Slf4j
public class MuYingCommodityDetailTaskProducer extends PageParseFetchTaskHandler {

	public static final String MUYING_SHOP_LIST_DETAIL = "muying_shop_list_detail";

	private FetchTaskProducer postDetailProducer = new FetchTaskProducer(MUYING_SHOP_LIST_DETAIL);

	public MuYingCommodityDetailTaskProducer(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Set<String> detailUrls = new TreeSet<>();
		Elements elements = doc.select("#SubCategoryPrice > li > dl > dd > p.goods_name > a");
		for (Element element : elements) {
			String url = element.attr("href");
			detailUrls.add(url);
		}
		// 推送
		for (String url : detailUrls) {
			if (StringUtils.isBlank(url)) {
				continue;
			}
			task.setBatchName(MUYING_SHOP_LIST_DETAIL);
			task.setUrl(url);
			postDetailProducer.saveAndPushTask(task);
			log.debug(task.toString());
		}
		///////////////////
		detailUrls.clear();
		detailUrls = null;
		task = null;
		doc = null;

	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.fetcher.document(task.getUrl());
		parsePage(doc, task);
	}
	
	public static void main(String[] args) {
		String taskQueueName = MuYingCommoditySearchList.MUYING_SHOP_LIST;
		MuYingCommodityDetailTaskProducer detailTaskProducer = new MuYingCommodityDetailTaskProducer(taskQueueName);
		detailTaskProducer.setSleep(5000);
		detailTaskProducer.run();
		
	}
}
