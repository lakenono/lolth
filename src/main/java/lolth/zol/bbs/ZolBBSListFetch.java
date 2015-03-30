package lolth.zol.bbs;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.zol.bbs.bean.ZolBBSPostBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZolBBSListFetch extends PageParseFetchTaskHandler {

	public ZolBBSListFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		String taskQueueName = ZolBBSListTaskProducer.ZOL_POST_LIST;
		ZolBBSListFetch fetch = new ZolBBSListFetch(taskQueueName);
		fetch.setSleep(3000);

		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements trElements = doc.select("table#bookList tbody tr.edition-topic");
		if (trElements.size() == 0) {
			return;
		}

		Element tr = trElements.first();
		while (tr.nextElementSibling() != null) {
			tr = tr.nextElementSibling();

			ZolBBSPostBean post = new ZolBBSPostBean();

			String id = tr.attr("id");
			id = StringUtils.substringAfter(id, "_");
			post.setId(id);

			// 是否热门
			Elements hot = tr.select("td.folder span.ico-hot");
			if (hot.size() > 0) {
				post.setType(hot.first().attr("title"));
			}

			// 名称地址
			Elements title = tr.select("td.title div a.topicurl");
			if (title.size() > 0) {
				post.setTitle(title.first().attr("title"));
				post.setUrl(title.first().absUrl("href"));
			}

			Elements belong = tr.select("td.title div span.iclass a");
			if (belong.size() > 0) {
				post.setBelong(belong.first().text());
			}

			// 是否有图片
			Elements pic = tr.select("td.title div span.pic");
			if (pic.size() > 0) {
				post.setHasImage("Y");
			} else {
				post.setHasImage("N");
			}

			// 回复查看
			Elements replyAndViews = tr.select("td.reply");
			if (replyAndViews.size() > 0) {
				String replyAndViewsText = replyAndViews.first().text();

				String[] data = StringUtils.split(replyAndViewsText, "/");
				if (data.length == 2) {
					post.setReplys(StringUtils.trim(data[0]));
					post.setViews(StringUtils.trim(data[1]));
				}
			}

			post.setKeyword(task.getName());
			// 持久化
			post.persistOnNotExist();
		}
	}

}
