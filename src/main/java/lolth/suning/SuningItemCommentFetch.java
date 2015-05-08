package lolth.suning;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.FetchTaskHandler;
import lolth.suning.bean.SuningItemCommentBean;
import lolth.suning.bean.SuningUserBean;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SuningItemCommentFetch extends FetchTaskHandler {

	public SuningItemCommentFetch() {
		super(SuningItemCommentListTaskProducer.Producer.SUNING_ITEM_COMMENT_LIST);
	}

	public static void main(String[] args) {
		SuningItemCommentFetch fetch = new SuningItemCommentFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void handleTask(FetchTask task) throws Exception {
		String json = GlobalComponents.fetcher.document(task.getUrl()).text();

		if (StringUtils.isBlank(json)) {
			return;
		}

		json = StringUtils.removeStart(json, "getItem(");
		json = StringUtils.removeEnd(json, ")");

		JSONObject jsonObj = JSON.parseObject(json);
		if (!jsonObj.getBoolean("success")) {
			return;
		}

		// 解析数据
		JSONArray commentList = jsonObj.getJSONObject("data").getJSONArray("reviews");

		for (int i = 0; i < commentList.size(); i++) {
			SuningItemCommentBean comment = new SuningItemCommentBean();

			JSONObject commentObj = commentList.getJSONObject(i);

			comment.setId(commentObj.getString("id"));
			comment.setContent(commentObj.getString("content"));
			comment.setPublishTime(commentObj.getString("publishTime"));
			comment.setScore(commentObj.getString("score"));

			StringBuffer labels = new StringBuffer();

			// 标签
			JSONArray labelList = commentObj.getJSONArray("labels");

			for (int j = 0; j < labelList.size(); j++) {
				labels.append(labelList.getJSONObject(j).getString("name")).append(",");
			}

			if (labels.length() != 0) {
				labels.deleteCharAt(labels.length() - 1);
				comment.setLabels(labels.toString());
			}

			// 其他
			comment.setPublisherId(commentObj.getString("userId"));

			comment.setPublishIp(commentObj.getString("publishIp"));
			comment.setOrderTime(commentObj.getString("orderTime"));
			comment.setReplyCount(commentObj.getString("replyCount"));
			comment.setUsefulVoteCount(commentObj.getString("usefulVoteCount"));

			// 用户
			try {
				JSONObject userObj = commentObj.getJSONObject("user");
				SuningUserBean user = new SuningUserBean();
				user.setId(userObj.getString("id"));
				user.setNickName(userObj.getString("nickName"));
				user.setProvince(userObj.getString("province"));
				user.setBirthday(userObj.getString("birthday"));
				user.setGender(userObj.getString("gender"));
				user.setLevelId(userObj.getString("levelId"));
				user.setLevelName(userObj.getString("levelName"));

				user.persistOnNotExist();
			} catch (Exception e) {
			}

			comment.setItemId(task.getExtra());
			comment.persistOnNotExist();
		}
	}

}
