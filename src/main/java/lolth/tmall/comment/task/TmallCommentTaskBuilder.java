package lolth.tmall.comment.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.tmall.comment.task.bean.TmallCommentTaskBean;
import lolth.tmall.search.bean.TmallGoodsBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class TmallCommentTaskBuilder {
	private static final int COMMENT_PAGE_SIZE = 20;
	private static final String TMALL_GOODS_COMMENT_URL_TEMPLATE = "http://rate.tmall.com/list_detail_rate.htm?itemId={0}&sellerId={1}&currentPage={2}";

	public static void main(String[] args) {
		try {
			log.info("TmallCommentTaskBuilder build task start!");
			new TmallCommentTaskBuilder().run();
		} catch (Exception e) {
			log.error("TmallCommentTaskBuilder build task error ", e);
		}
	}

	private void run() throws Exception {
		cleanMQ();
		log.info("TmallCommentTaskBuilder cleanMQ Finish!");
		buildTask();
		log.info("TmallCommentTaskBuilder buildDB Finish!");
	}

	private void buildTask() throws Exception {
		/**
		 * tasks[0] : goodsId tasks[1] : sellerId;
		 */
		List<String[]> tasks = getTask();

		for (String[] t : tasks) {
			String goodsId = t[0];
			String sellerId = t[1];

			if (StringUtils.isNoneBlank(goodsId) && StringUtils.isNoneBlank(sellerId)) {
				try {
					int page = getPages(goodsId, sellerId);

					for (int i = 1; i <= page; i++) {
						try {
							TmallCommentTaskBean task = new TmallCommentTaskBean();
							task.setGoodsId(goodsId);
							task.setUrl(buildUrl(goodsId, sellerId, i + ""));
							task.setStatus("todo");

							task.persist();
						} catch (Exception e) {
							log.error("{},{},{} build task fail", goodsId, sellerId, i, e);
						}
					}

					pushMQ(goodsId);
					Thread.sleep(10000);
				} catch (Exception e) {
					log.error("{},{} get max page fail", goodsId, sellerId, e);
				}

			} else {
				log.info("{},{} can not build task", goodsId, sellerId);
			}

		}
	}

	private List<String[]> getTask() throws Exception {
		String sql = "select id,shopId from " + BaseBean.getTableName(TmallGoodsBean.class) + " where comments is not null";

		List<String[]> todo = GlobalComponents.db.getRunner().query(sql, new ResultSetHandler<List<String[]>>() {

			@Override
			public List<String[]> handle(ResultSet rs) throws SQLException {
				List<String[]> rsList = new ArrayList<String[]>();
				while (rs.next()) {
					String[] rsStr = new String[2];
					rsStr[0] = rs.getString("id");
					rsStr[1] = rs.getString("shopId");
					rsList.add(rsStr);
				}
				return rsList;
			}
		});
		return todo;
	}

	private int getPages(String goodsId, String sellerId) throws Exception {
		String content = GlobalComponents.fetcher.document(buildUrl(goodsId, sellerId, "1")).text();
		if (StringUtils.isBlank(content)) {
			return 0;
		}

		String json = "{" + content + "}";
		JSONObject rateDetail = JSON.parseObject(json).getJSONObject("rateDetail");
		int totalItem = rateDetail.getJSONObject("rateCount").getIntValue("total");

		if (totalItem == 0) {
			return 0;
		}

		// 计算总页数
		int pages = totalItem / COMMENT_PAGE_SIZE + 1;

		// 天猫最多提供99页
		if (pages > 99) {
			pages = 99;
		}

		return pages;
	}

	private String buildUrl(String goodsId, String sellerId, String page) {
		return MessageFormat.format(TMALL_GOODS_COMMENT_URL_TEMPLATE, goodsId, sellerId, page);
	}

	private void pushMQ(String goodsId) throws Exception {
		List<TmallCommentTaskBean> tasks = GlobalComponents.db.getRunner().query("select * from " + BaseBean.getTableName(TmallCommentTaskBean.class) + " where goodsId=? and status='todo'", new BeanListHandler<TmallCommentTaskBean>(TmallCommentTaskBean.class), goodsId);

		for (TmallCommentTaskBean bean : tasks) {
			// push redis
			log.info("push task {}", bean.toString());
			GlobalComponents.jedis.lpush(BaseBean.getTableName(TmallCommentTaskBean.class), JSON.toJSONString(bean));
		}
	}

	private void cleanMQ() throws Exception {
		GlobalComponents.jedis.del(BaseBean.getTableName(TmallCommentTaskBean.class));
		log.info("TmallCommentTaskBuilder clean MQ");
	}

}
