package lolth.double5.bbs;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lolth.double5.bbs.bean.Double5BBSPostBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 55BBS爬取列表
 * 
 * @author shi.lei
 *
 */
public class Double5BBSListFetch {
	private static final Logger log = LoggerFactory
			.getLogger(Double5BBSListFetch.class);

	private String key;

	public Double5BBSListFetch(String key) throws Exception {
		this.key = URLEncoder.encode(key, "UTF-8");
	}

	public static void main(String[] args) throws Exception {
		String[] keys = { "chanel 香水" };
		for (String key : keys) {
			new Double5BBSListFetch(key).run();
			log.info("55BBS list finish :　{}", key);
		}

	}

	private void run() throws Exception {
		String nextPageUrl = processPage(getUrl(0));

		while (StringUtils.isNotBlank(nextPageUrl)) {
			nextPageUrl = "http://so.55bbs.com/" + nextPageUrl;
			log.debug("start : {}", nextPageUrl);
			nextPageUrl = processPage(nextPageUrl);
		}

		log.info("55bbs listFetch success!");
	}

	private String getUrl(int page) {
		String urlTemplate = "http://so.55bbs.com/search.php?q={0}&p={1}&s=5946671531345126826&nsid=1";
		return MessageFormat.format(urlTemplate, key, page + "");
	}

	private String processPage(String listPageUrl) throws IOException,
			InterruptedException, IllegalArgumentException,
			IllegalAccessException, InstantiationException, SQLException {

		Document doc = GlobalComponents.fetcher.document(listPageUrl);

		// 解析数据
		Elements titleElements = doc.select("h3.c-title a");
		for (Element e : titleElements) {
			String url = e.attr("href");
			if (StringUtils.startsWith(url, "http://bbs.55bbs.com/thread")) {
				String title = e.text();

				Double5BBSPostBean bean = new Double5BBSPostBean();
				bean.setUrl(url);
				bean.setTitle(title);

				bean.persist();
				log.debug("{} | {}", url, title);
			}
		}

		log.info("{} finish !", listPageUrl);

		Elements nextPage = doc.select("a.pager-next-foot.n");
		if (nextPage.size() == 0) {
			return null;
		}
		return nextPage.attr("href");
	}

}
