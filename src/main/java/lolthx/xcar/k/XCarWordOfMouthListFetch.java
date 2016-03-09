package lolthx.xcar.k;

import java.net.URL;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.xcar.bean.XCarWordOfMouthBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XCarWordOfMouthListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "xcar_kb_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		Elements dls = doc.select("div.review_comments_dl dl");
		XCarWordOfMouthBean bean = null;
		if (dls.size() > 0) {
			for (Element dl : dls) {

				try {
					bean = new XCarWordOfMouthBean();

					bean.setProjectName(task.getProjectName());
					bean.setFormId(StringUtils.substringBefore(task.getExtra(), ":"));
					bean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));
					
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

						String idStr = dd.select("div.useful").attr("id");
						String id = StringUtils.substringAfter(idStr, "ding_div_");
						bean.setId(id);
					}

					bean.saveOnNotExist();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

	}

	@Override
	protected String getCharset() {
		return "gb2312";
	}

	public static void main(String args[]) {
		for(int i = 1 ; i <= 20 ; i++){
			new XCarWordOfMouthListFetch().run();
		}
	}

}
