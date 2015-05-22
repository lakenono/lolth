package lolth.zol.bbs;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskProducer;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.zol.bbs.ZolBBSUserFetch.ZolBBSUserTaskProducer;
import lolth.zol.bbs.bean.ZolBBSPostBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class ZolBBSDetailFetch extends PageParseFetchTaskHandler {
	private ZolBBSUserTaskProducer producer = null;

	public ZolBBSDetailFetch() {
		super(ZolBBSDetailTaskProducer.ZOL_DETAIL_PAGE);
		producer = new ZolBBSUserTaskProducer();
	}

	public static void main(String[] args) throws Exception {
		ZolBBSDetailFetch fetch = new ZolBBSDetailFetch();
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
			
			try {
				producer.push(post.getUserId(), task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updatePostBean(ZolBBSPostBean post) throws SQLException {
		log.debug("Update userId={},postTime={}", post.getUserId(), post.getPostTime());
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(ZolBBSPostBean.class) + " SET userId=? , content = ? , postTime = ? where url=? ", post.getUserId(), post.getContent(), post.getPostTime(), post.getUrl());
	}

	public static class ZolBBSDetailTaskProducer extends FetchTaskProducer {

		public static final String ZOL_DETAIL_PAGE = "zol_bbs_post_detail";

		public ZolBBSDetailTaskProducer() {
			super(ZOL_DETAIL_PAGE);
		}

		protected FetchTask buildTask(String url, FetchTask frontTask) {
			FetchTask fetchTask = new FetchTask();
			fetchTask.setName(frontTask.getName());
			fetchTask.setBatchName(ZOL_DETAIL_PAGE);
			fetchTask.setUrl(url);

			return fetchTask;
		}

		public void push(String url, FetchTask frontTask) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
			saveAndPushTask(buildTask(url, frontTask));
		}
	}

}
