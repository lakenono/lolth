package lolthx.taobao.comment;

import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolthx.taobao.comment.bean.TaobaoCommentBean;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TaobaoCommentsFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "taobao_item_commons";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		List<TaobaoCommentBean> beans = null;
		if ("1".equals(task.getExtra())) {
			beans = parseTmallComment(result, task);
		} else {
			beans = parseTaobaoComment(result, task);
		}

		if (beans != null && !beans.isEmpty()) {
			for (TaobaoCommentBean commentBean : beans) {
				try {
					commentBean.persistOnNotExist();
				} catch (Exception e) {

				}
			}
		}

	}

	private List<TaobaoCommentBean> parseTaobaoComment(String result, Task task) {
		JSONObject rateDetail = JSON.parseObject(result).getJSONObject("rateDetail");
		JSONArray rateList = rateDetail.getJSONArray("rateList");
		List<TaobaoCommentBean> beans = new ArrayList<TaobaoCommentBean>(rateList.size());
		String itemId = HttpURLUtils.getUrlParams(task.getUrl(), "GBK").get("itemId");
		TaobaoCommentBean bean = null;
		for (int i = 0; i < rateList.size(); i++) {
			bean = new TaobaoCommentBean();
			bean.setType(task.getExtra());
			bean.setItemId(itemId);
			try {
				JSONObject rate = rateList.getJSONObject(i);
				bean.setId(rate.getString("id"));
				bean.setConmment(rate.getString("rateContent"));
				bean.setCommentTime(rate.getString("rateDate"));
				bean.setReply(rate.getString("reply"));

				String appendCommentJson = rate.getString("appendComment");
				if (!StringUtils.isBlank(appendCommentJson) && StringUtils.startsWith(appendCommentJson, "{")) {
					JSONObject appendComment = JSON.parseObject(appendCommentJson);
					bean.setAppandConmment(appendComment.getString("content"));

				} else {
					bean.setAppandConmment(appendCommentJson);
				}

				bean.setServiceComment(rate.getString("serviceRateContent"));

				// user
				bean.setNick(rate.getString("displayUserNick"));
				bean.setVip(rate.getString("tamllSweetLevel"));

				beans.add(bean);
			} catch (Exception e) {

			}
		}
		return beans;
	}

	private List<TaobaoCommentBean> parseTmallComment(String result, Task task) {

		String json = result.substring(1, result.length() - 1);
		JSONObject parseObject = JSON.parseObject(json);
		JSONArray jsonArray = parseObject.getJSONArray("comments");
		List<TaobaoCommentBean> beans = new ArrayList<TaobaoCommentBean>(jsonArray.size());
		JSONObject jsonObject = null;
		TaobaoCommentBean bean = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			bean = new TaobaoCommentBean();
			bean.setType(task.getExtra());
			try {
				jsonObject = jsonArray.getJSONObject(i);
				String aucNumId = jsonObject.getJSONObject("auction").getString("aucNumId");
				bean.setItemId(aucNumId);
				String rateId = jsonObject.getString("rateId");
				bean.setId(rateId);
				String content = jsonObject.getString("content");
				bean.setConmment(content.trim());
				String vip = jsonObject.getJSONObject("user").getString("vip");
				bean.setVip(vip);
				String nick = jsonObject.getJSONObject("user").getString("nick");
				bean.setNick(nick);
				String day = jsonObject.getString("date");
				bean.setCommentTime(day);
				JSONObject jsonTmp = jsonObject.getJSONObject("reply");

				if (jsonTmp != null) {
					String reply = jsonTmp.getString("content");
					bean.setReply(reply.trim());
				}
				jsonTmp = jsonObject.getJSONObject("append");
				if (jsonTmp != null) {
					String append = jsonTmp.getString("content");
					bean.setAppandConmment(append.trim());
				}
				beans.add(bean);
			} catch (Exception e) {

			}
		}
		return beans;
	}

}
