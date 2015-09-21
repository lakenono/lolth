package lolthx.suning.keysearch;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.suning.bean.SuningItemCommentBean;

public class SuningSearchCommentFetch extends DistributedParser  {

	@Override
	public String getQueueName() {
		return "suning_comment_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		String json = StringUtils.removeStart(result, "reviewList(");
		json = StringUtils.removeEnd(json, ")"); 
		
		JSONObject jsonObj = JSON.parseObject(json);
		
		JSONArray commentList = jsonObj.getJSONArray("commodityReviews");
		for (int i = 0; i < commentList.size(); i++) {
			SuningItemCommentBean comment = new SuningItemCommentBean();

			comment.setProjectName(task.getProjectName());
			comment.setKeyword(task.getExtra());
			
			JSONObject commentObj = commentList.getJSONObject(i);
			comment.setId(commentObj.getString("commodityReviewId"));
			comment.setContent(commentObj.getString("content"));
			comment.setPublishTime(commentObj.getString("publishTime"));
			comment.setQualityStar(commentObj.getString("qualityStar"));
			
			JSONObject shopInfo = commentObj.getJSONObject("shopInfo");
			comment.setShopName(shopInfo.getString("shopName"));
			
			JSONObject commodityInfo = commentObj.getJSONObject("commodityInfo");
			comment.setCommodityName(commodityInfo.getString("commodityName"));

			JSONObject userInfo = commentObj.getJSONObject("userInfo");
			comment.setUserName(userInfo.getString("nickName"));
			comment.setUserLevelId(userInfo.getString("levelId"));
			comment.setUserLevelName(userInfo.getString("levelName"));
			
			comment.saveOnNotExist();
		}
		
		
		
	}

	public static void main(String args[]){
		new SuningSearchCommentFetch().run();
	}
}
