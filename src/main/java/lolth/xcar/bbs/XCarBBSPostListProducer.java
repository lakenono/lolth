package lolth.xcar.bbs;

import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 列表页解析推送
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class XCarBBSPostListProducer extends PagingFetchTaskProducer {
	public static final String XCAR_BBS_POST_LIST = "xcar_bbs_post_list";

	private static final String XCAR_BBS_POST_LIST_URL_TEMPLATE = "http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&page={1}";

	private String keyword;
	private String brandId;

	public XCarBBSPostListProducer(String keyword, String brandId) {
		super(XCAR_BBS_POST_LIST);
		this.keyword = keyword;
		this.brandId = brandId;
	}

	public static void main(String[] args) {
		String keyword = "chevrolet";
		/*
		 * 创酷：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=1105
		 * 北京现代ix25：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=1135
		 * 别克昂科拉：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=974
		 * 福特翼搏：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=970
		 * 标致2008：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=1013
		 * 缤智：http://www.xcar.com.cn/bbs/forumdisplay.php?fid=1145
		 */
		String[] brandIds = { "1105", "1135", "974", "970", "1013", "1145" };

		for (String id : brandIds) {
			log.info("Handler http://www.xcar.com.cn/bbs/forumdisplay.php?fid={} Start ! ", id);
			XCarBBSPostListProducer producer = new XCarBBSPostListProducer(keyword, id);
			log.info("Handler http://www.xcar.com.cn/bbs/forumdisplay.php?fid={} Finish ! ", id);

			producer.run();
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));

			Elements pages = doc.select("div.fn_0209 a");
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
		return MessageFormat.format(XCAR_BBS_POST_LIST_URL_TEMPLATE, brandId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(XCAR_BBS_POST_LIST);
		task.setUrl(url);
		task.setExtra(brandId);

		return task;
	}

}
