package lolth.xcar.k;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.xcar.k.bean.XCarWordOfMouthBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 爱卡口碑爬取
 * 
 * @author shi.lei
 *
 */
public class XCarWordOfMouthListFetch extends PageParseFetchTaskHandler {

	public static void main(String[] args) throws Exception {
		String taskQueue = XCarWordOfMouthListProducer.XCAR_K_LIST;

		XCarWordOfMouthListFetch fetch = new XCarWordOfMouthListFetch(taskQueue);
		 fetch.setSleep(1000);
		 fetch.run();
	}

	public XCarWordOfMouthListFetch(String taskQueueName) {
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements dls = doc.select("div.review_comments_dl dl");
		if (dls.size() > 0) {
			for (Element dl : dls) {
				XCarWordOfMouthBean bean = new XCarWordOfMouthBean();

				bean.setKeyword(task.getName());
				bean.setBrandId(task.getExtra());

				Elements dts = dl.getElementsByTag("dt");
				if (dts.size() > 0) {
					Element dt = dts.first();

					// 点评类型
					String comment = dt.ownText();
					comment = StringUtils.substringBetween(comment, "【", "】");
					bean.setComment(comment);

					// 点评角度
					Elements commentItem = dt.select("em>a");
					if (commentItem.size() > 0) {
						bean.setCommentItem(commentItem.first().text());
					}

					// 点评
					Elements title = dt.select(">a");
					if (title.size() > 0) {
						String postId = title.first().attr("href");
						bean.setPostId(StringUtils.substringAfter(postId, "tid="));
						bean.setTitle(title.first().text());
					}
				}

				Elements dds = dl.getElementsByTag("dd");
				if (dds.size() > 0) {
					Element dd = dds.first();
					// 内容
					bean.setContent(dd.ownText());

					// 感觉有用人数
					Elements usefulCount = dd.select("div>i>span");
					if (usefulCount.size() > 0) {
						bean.setUsefulCount(usefulCount.text());
					}

					// 来源
					Elements from = dd.select("p>a");
					if (from.size() > 0) {
						bean.setFrom(from.text());
					}
				}

				bean.persistOnNotExist();
			}
		}
	}

}
