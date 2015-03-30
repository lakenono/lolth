package lolth.zol.bbs;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.zol.bbs.bean.ZolBBSPostBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ZolBBSDetailFetch extends PageParseFetchTaskHandler {

	public ZolBBSDetailFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		String queueName = ZolBBSDetailTaskProducer.ZOL_DETAIL_PAGE;
		ZolBBSDetailFetch fetch = new ZolBBSDetailFetch(queueName);
		fetch.setSleep(2000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		// 帖子：
		Elements postTable = doc.select("html body div.wrapper table.post-list.host-post.box-shadow");
		if (postTable.size() > 0) {
			ZolBBSPostBean post = new ZolBBSPostBean();

			post.setUserId(postTable.first().attr("data-id"));

			// 发帖时间
			Elements postTime = postTable.first().select("tbody tr td.post-title span.publish-time");
			if (postTime.size() > 0) {
				String time = postTime.first().text();
				time = StringUtils.substringAfter(time, "发表于");

				post.setPostTime(time);
			}

			// 內容
			Elements content = postTable.first().select("tbody tr td.post-main div#bookContent");
			if (content.size() > 0) {
				post.setContent(content.first().text());
			}

			post.setUrl(task.getUrl());

			updatePostBean(post);
		}
	}

	private void updatePostBean(ZolBBSPostBean post) throws SQLException {
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(ZolBBSPostBean.class) + " SET userId=? , content = ? , postTime = ? where url=? ", post.getUserId(), post.getContent(), post.getPostTime(), post.getUrl());
	}

}
