package lolth.tmall.comment.fetch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.DB;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolth.tmall.comment.bean.TmallGoodsCommentBean;
import lolth.tmall.comment.task.bean.TmallCommentTaskBean;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class TmallGoodsCommentFetch {

	public static void main(String[] args) throws Exception {
		new TmallGoodsCommentFetch().run();
	}

	private void run() throws Exception {
		while (true) {
			try {
				TmallCommentTaskBean task = getTask();
				if (task == null) {
					Thread.sleep(60000);
					continue;
				}
				
				if(!isFinish(task)){
					String url = task.getUrl();
					String content = GlobalComponents.fetcher.document(url).text();
					if (content != null) {
						List<TmallGoodsCommentBean> goods = parseJson(content);
						for (TmallGoodsCommentBean b : goods) {
							b.setGoodsId(task.getGoodsId());
							b.persist();
						}
					}
					task.updateSuccess();
					Thread.sleep(10000);
				}else{
					log.info("{} has finish!");
				}
			} catch (Exception e) {
				log.error("{} task fail!",e);
				try {
					Thread.sleep(10000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public TmallCommentTaskBean getTask() {
		String json = GlobalComponents.jedis.lpop(BaseBean.getTableName(TmallCommentTaskBean.class));

		if (json == null) {
			return null;
		}

		TmallCommentTaskBean bean = JSON.parseObject(json, TmallCommentTaskBean.class);
		return bean;
	}

	private List<TmallGoodsCommentBean> parseJson(String content) throws IOException, InterruptedException {
		String json = "{" + content + "}";
		List<TmallGoodsCommentBean> beanList = new ArrayList<>();

		JSONObject rateDetail = JSON.parseObject(json).getJSONObject("rateDetail");
		JSONArray rateList = rateDetail.getJSONArray("rateList");

		for (int i = 0; i < rateList.size(); i++) {
			TmallGoodsCommentBean bean = new TmallGoodsCommentBean();
			JSONObject rate = rateList.getJSONObject(i);

			bean.setId(rate.getString("id"));
			bean.setComment(rate.getString("rateContent"));
			bean.setCommentTime(rate.getString("rateDate"));
			bean.setReplay(rate.getString("reply"));
			bean.setAppendComment(rate.getString("appendComment"));
			bean.setServiceComment(rate.getString("serviceRateContent"));

			// user
			bean.setUser(rate.getString("displayUserNick"));
			bean.setUserVipLevel(rate.getString("tamllSweetLevel"));

			beanList.add(bean);
		}
		return beanList;
	}
	
	private boolean isFinish(TmallCommentTaskBean task) throws SQLException {
		@SuppressWarnings("unchecked")
		long count = (long) GlobalComponents.db.getRunner().query("select count(*) from " + BaseBean.getTableName(TmallCommentTaskBean.class) + " where url=?  and status='success' ", DB.scaleHandler, task.getUrl());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
