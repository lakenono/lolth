package lolth.mamanet;

import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.mamanet.bean.MamaBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

/**
 * 抓取妈妈网数据
 * 
 * @author hepeng.yang
 *
 */
@Slf4j
public class MamaNetSearchFetch extends PageParseFetchTaskHandler {
	private ArticleTextExtractor extractor = new ArticleTextExtractor();

	public MamaNetSearchFetch(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		// 获取页面10个结果，如果结果为空，返回
		Elements result_10 = doc.select("div.result");
		if (result_10.isEmpty()) {
			return;
		}
		List<MamaBean> beans = new ArrayList<>();
		MamaBean bean = null;
		for (Element element : result_10) {
			bean = new MamaBean();
			// 抽取title
			Elements title = element.select("h3 a");
			if (title != null) {
				log.debug("妈妈网抽取title：" + title.text());
				bean.setTilte(title.text());
				// URL
				String url = title.attr("href");
				log.debug("妈妈网抽取url：" + url);
				if (StringUtils.isNotBlank(url)) {
					bean.setUrl(url);
					// 爬取正文
					Document content_page = GlobalComponents.fetcher.document(url);
					JResult content = extractor.extractContent(content_page.html());
					if (content != null) {
						// content
						log.debug("妈妈网抽取content:" + content.getText());
						bean.setContent(content.getText());
					}
				}
			}
			// publishTime
			Elements publishTime = element.select("span.c-showurl");
			if (publishTime != null) {
				log.debug("妈妈网抽取publishTime:" + StringUtils.substringAfter(publishTime.text(), " "));
				bean.setPublishTime(StringUtils.substringAfter(publishTime.text(), " "));
			}
			bean.setName(task.getName());
			bean.setKeyword(task.getExtra());
			beans.add(bean);
		}
		if (beans.isEmpty()) {
			return;
		}

		for (MamaBean b : beans) {
			if (!b.exist()) {
				b.persist();
			}
		}
		beans.clear();
		beans = null;
	}

	public static void main(String[] args) {
		String taskQueueName = MamaNetSearchListProducer.MAMANET_SEARCH_LIST;
		MamaNetSearchFetch mamaNet = new MamaNetSearchFetch(taskQueueName);
		mamaNet.setSleep(2000);
		mamaNet.run();
	}

}
