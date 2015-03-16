package lolth.baidu.news;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.baidu.news.bean.BaiduNewsBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

public class BaiduNewsContentFetch {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private ArticleTextExtractor extractor = new ArticleTextExtractor();

	private List<String> getUrls() throws Exception {
		List<String> urls = GlobalComponents.db.getRunner().query(
				"select baiduCacheUrl from "
						+ BaseBean.getTableName(BaiduNewsBean.class)
						+ " where text = ''", new ColumnListHandler<String>());
		return urls;
	}

	private void updateText(String text, String baiduCacheUrl) throws Exception {
		GlobalComponents.db.getRunner().update(
				" update " + BaseBean.getTableName(BaiduNewsBean.class)
						+ " set text=? where baiduCacheUrl = ?", text,
				baiduCacheUrl);
	}

	private String process(String url) throws Exception {
		try {
			Document document = GlobalComponents.fetcher.document(url);
			JResult res = this.extractor.extractContent(document.html());

			String result = res.getText();
			if (StringUtils.isEmpty(result)) {
				result = document.text();
			}

			this.log.debug("text: {}", StringUtils.substring(result, 0, 50));
			return result;

		} catch (Exception e) {
			log.error("{} get content error :", url, e);
		}
		return null;
	}

	public void run() throws Exception {
		List<String> urls = getUrls();

		for (String u : urls) {
			try {
				String text = process(u);
				if (text != null) {
					updateText(text, u);
					log.info("{} success !", u);
				} else {
					log.info("{} can not parse !", u);
				}

			} catch (Exception e) {
				log.error("{} error : ", u, e);
			}

		}
		log.info("all finish!");

	}

	public static void main(String[] args) throws Exception {
		new BaiduNewsContentFetch().run();
	}
}
