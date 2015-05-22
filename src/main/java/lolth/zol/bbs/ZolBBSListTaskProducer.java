package lolth.zol.bbs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class ZolBBSListTaskProducer extends PagingFetchTaskProducer {
	private static final String ZOL_POST_LIST_URL_TEMPLATE = "http://bbs.zol.com.cn/{0}/{1}_p{2}.html";

	public static final String ZOL_POST_LIST = "zol_bbs_post_list";

	private String keyword = null;
	private String bbsType = null;
	private String bbsId = null;
	private String bbsName = null;

	public static void main(String[] args) {
		// http://bbs.zol.com.cn/sjbbs/d1673_p0.html
		String keyword = "oppo";

		List<BBSInfoBean> infos = new ArrayList<BBSInfoBean>();
		infos.add(new BBSInfoBean("sjbbs", "x392008", "X5Max"));
		infos.add(new BBSInfoBean("sjbbs", "x376453", "Xshot"));
		infos.add(new BBSInfoBean("sjbbs", "x399773", "P8"));
		infos.add(new BBSInfoBean("sjbbs", "x367005", "Note4"));


		for (BBSInfoBean info : infos) {
			log.info("{} start ! ", info);
			ZolBBSListTaskProducer producer = new ZolBBSListTaskProducer(keyword, info.getBbsType(), info.getBbsId(), info.getBbsName(), ZOL_POST_LIST);
			producer.setSleep(1000);
			producer.run();
			log.info("{} finish ! ", info);
		}

	}

	public ZolBBSListTaskProducer(String keyword, String bbsType, String bbsId, String bbsName, String taskQueueName) {
		super(taskQueueName);
		if (StringUtils.isBlank(bbsType) && StringUtils.isBlank(bbsName)) {
			throw new IllegalArgumentException("bbsType,bbsName can not be null!");
		}
		this.keyword = keyword;
		this.bbsType = bbsType;
		this.bbsId = bbsId;
		this.bbsName = bbsName;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(ZOL_POST_LIST_URL_TEMPLATE, bbsType, bbsId, String.valueOf(pageNum));
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(0));
			Elements pageElements = doc.select("div.page");
			if (pageElements.size() > 0) {
				// 分页部分
				Elements nextPage = pageElements.first().getElementsMatchingOwnText("下一页");
				if (nextPage.size() > 0) {
					String page = nextPage.first().previousElementSibling().text();
					if (StringUtils.isNumeric(page)) {
						return Integer.parseInt(page);
					}
				}
			} else {
				Elements trElements = doc.select("table#bookList tbody tr.edition-topic");
				if (trElements.size() > 0) {
					if (trElements.first().nextSibling() != null) {
						return 1;
					}
				}
			}
		} catch (Exception e) {
			log.error("Get max page error!", e);
		}

		return 0;
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(keyword);
		fetchTask.setBatchName(ZOL_POST_LIST);
		fetchTask.setUrl(url);
		fetchTask.setExtra(bbsName);

		return fetchTask;
	}

	@Data
	@AllArgsConstructor
	static class BBSInfoBean {
		private String bbsType;
		private String bbsId;
		private String bbsName;
	}

}
