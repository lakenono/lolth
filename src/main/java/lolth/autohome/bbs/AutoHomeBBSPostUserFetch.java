package lolth.autohome.bbs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.autohome.bbs.bean.AutoHomeBBSPostBean;
import lolth.autohome.bbs.bean.AutoHomeBBSUserBean;

import org.apache.commons.dbutils.ResultSetHandler;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoHomeBBSPostUserFetch {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws Exception {
		while (true) {
			List<String[]> todo = getTodoList();
			if (todo.isEmpty()) {
				log.info("all finish!");
				break;
			}

			for (String url[] : todo) {
				try {
					String html = GlobalComponents.fetcher.fetch(url[0]);

					AutoHomeBBSUserBean bean = this.parse(html);
					bean.setAuthorUrl(url[1]);

					bean.persistOnNotExist();

					updatePost(url[0], "success");
					log.info("{} success ", bean);
				} catch (HttpStatusException e1) {
					e1.printStackTrace();
					updatePost(url[0], e1.getMessage());
				} catch (Exception e) {
					this.log.error("{}", url[0], e);
				}
			}
		}
	}

	private List<String[]> getTodoList() throws Exception {
		List<String[]> todo = GlobalComponents.db
				.getRunner()
				.query("select url,authorUrl from "
						+ BaseBean.getTableName(AutoHomeBBSPostBean.class)
						+ " where postTime >= '2014-04-01' and comment_status='' limit 1000",
						new ResultSetHandler<List<String[]>>() {

							@Override
							public List<String[]> handle(ResultSet rs)
									throws SQLException {
								List<String[]> rsList = new ArrayList<String[]>();
								while (rs.next()) {
									String[] rsStr = new String[2];
									rsStr[0] = rs.getString("url");
									rsStr[1] = rs.getString("authorUrl");
									rsList.add(rsStr);
								}
								return rsList;
							}
						});
		return todo;
	}

	private void updatePost(String url, String status) throws SQLException {
		GlobalComponents.db.getRunner().update(
				"update " + BaseBean.getTableName(AutoHomeBBSPostBean.class)
						+ " set comment_status = ? where url = ?", status, url);
	}

	public AutoHomeBBSUserBean parse(String html) {
		Document document = Jsoup.parse(html);
		AutoHomeBBSUserBean bean = new AutoHomeBBSUserBean();

		Element topicElement = document.select("div#maxwrap-maintopic").first();
		Element ulElement = topicElement.select("ul.leftlist").first();

		if (ulElement.getElementsMatchingOwnText("来自：").size() != 0) {
			String area = ulElement.getElementsMatchingOwnText("来自：").first()
					.child(0).text();
			bean.setArea(area);
		}

		if (ulElement.getElementsMatchingOwnText("关注：").size() != 0) {
			String concern = ulElement.getElementsMatchingOwnText("关注：")
					.first().child(0).text();
			bean.setConcern(concern);
		}
		if (ulElement.getElementsMatchingOwnText("爱车：").size() != 0) {
			String car = ulElement.getElementsMatchingOwnText("爱车：").first()
					.child(0).text();
			bean.setCar(car);
		}

		return bean;
	}

	public static void main(String[] args) throws Exception {
		new AutoHomeBBSPostUserFetch().run();
	}
}
