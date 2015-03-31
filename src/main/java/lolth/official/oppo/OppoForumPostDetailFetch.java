package lolth.official.oppo;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.official.oppo.bean.OppoPostBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OppoForumPostDetailFetch extends PageParseFetchTaskHandler {

	public OppoForumPostDetailFetch(String taskQueueName) {
		super(taskQueueName);
	}

	public static void main(String[] args) throws Exception {
		String taskQueueName = OppoForumPostDetailTaskProducer.OPPO_FORUM_POST_DETAIL;
		OppoForumPostDetailFetch fetch = new OppoForumPostDetailFetch(taskQueueName);

		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements contentDiv = doc.select("div#postlist div.h_post");

		if (contentDiv.size() > 0) {
			OppoPostBean postBean = new OppoPostBean();

			Element mainPost = contentDiv.first();
			// 内容

			Elements table = mainPost.select("table div.t_fsz table");
			if (table.size() > 0) {
				postBean.setContent(table.text());
			} else {
				postBean.setContent("");
			}

			// 赞美

			Elements praise = mainPost.select("a#praise");
			if (praise.size() > 0) {
				String praiseStr = praise.first().text();
				praiseStr = StringUtils.substringBetween(praiseStr, "(", ")");
				postBean.setPraise(praiseStr);
			}

			// 贡献
			Elements collection = mainPost.select("a#k_favorite");
			if (collection.size() > 0) {
				String collectionStr = collection.first().text();
				collectionStr = StringUtils.substringBetween(collectionStr, "(", ")");
				postBean.setCollections(collectionStr);
			}

			postBean.setUrl(task.getUrl());

			updatePostBean(postBean);
		}

	}

	private void updatePostBean(OppoPostBean post) throws SQLException {
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(OppoPostBean.class) + " SET content = ? , praise = ? , collections = ? WHERE url = ?", post.getContent(), post.getPraise(), post.getCollections(), post.getUrl());
	}

}
