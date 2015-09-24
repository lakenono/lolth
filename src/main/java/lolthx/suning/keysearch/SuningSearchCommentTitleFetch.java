package lolthx.suning.keysearch;

import java.io.IOException;
import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.suning.bean.SuningCommentTitleBean;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SuningSearchCommentTitleFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "suning_comment_title";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		SuningCommentTitleBean bean = new SuningCommentTitleBean();
		
		String id = task.getExtra();
		
		bean.setId(id);
		bean.setProjectName(task.getProjectName());
		bean.setKeywords(id);

		boolean isSave = false;
		
		String propertiesJson = StringUtils.removeStart(result, "commodityProperties(");
		propertiesJson = StringUtils.removeEnd(propertiesJson, ")");
		
		JSONObject jsonProperties = JSON.parseObject(propertiesJson);
		JSONArray propertiesList = jsonProperties.getJSONArray("elements");
		if(propertiesList != null){
			isSave = true;
			for (int i = 0; i < propertiesList.size(); i++) {
				if (i == 0) {
					JSONObject proObj = propertiesList.getJSONObject(i);
					bean.setProperty1(proObj.getString("propertyName"));
	
					JSONArray items = proObj.getJSONArray("items");
					String itemStr = "";
					for (int n = 0; n < items.size(); n++) {
						JSONObject proItem = items.getJSONObject(n);
						itemStr = itemStr + proItem.getString("value") + ":" +proItem.getString("percent") + "%" + ",";
					}
					bean.setDescription1(itemStr);
					
				}else if(i==1){
					JSONObject proObj = propertiesList.getJSONObject(i);
					bean.setProperty2(proObj.getString("propertyName"));
	
					JSONArray items = proObj.getJSONArray("items");
					String itemStr = "";
					for (int n = 0; n < items.size(); n++) {
						JSONObject proItem = items.getJSONObject(n);
						itemStr = itemStr + proItem.getString("value") + ":" + proItem.getString("percent") + "%" + ",";
					}
					bean.setDescription2(itemStr);
				}
			}
		}
		
		String labesUrl = buildItemCommentLabesUrl(id);
		String labesHtml = GlobalComponents.jsoupFetcher.fetch(labesUrl);
		String labelsJson = StringUtils.removeStart(labesHtml, "commodityrLabels(");
		labelsJson = StringUtils.removeEnd(labelsJson, ")");
		JSONObject jsonObjects = JSON.parseObject(labelsJson);
		JSONArray labelsList = jsonObjects.getJSONArray("commodityLabelCountList");
		String labelsStr = "";
		if(labelsList != null){
			isSave = true;
			for (int i = 0; i < labelsList.size(); i++) {
				JSONObject proObj = labelsList.getJSONObject(i);
				labelsStr = labelsStr + proObj.getString("labelName") + ":" + proObj.getString("labelCnt") + ",";
			}
			bean.setLabelDescriptions(labelsStr);
		}
		
		String satisfyUrl = buildItemCommentsatisfy(id);
		String satisfyHtml = GlobalComponents.jsoupFetcher.fetch(satisfyUrl);
		String satisfyJson = StringUtils.removeStart(satisfyHtml, "satisfy(");
		satisfyJson = StringUtils.removeEnd(satisfyJson, ")");
		
		JSONObject jsonObjsatisfys = JSON.parseObject(satisfyJson);
		if(jsonObjsatisfys != null){
			
			JSONObject objSatis = jsonObjsatisfys.getJSONArray("reviewCounts").getJSONObject(0);
			
			String oneStarCount = objSatis.getString("oneStarCount");
			String twoStarCount = objSatis.getString("twoStarCount");
			String threeStarCount = objSatis.getString("threeStarCount");
			String fourStarCount = objSatis.getString("fourStarCount");
			String fiveStarCount = objSatis.getString("fiveStarCount");
			String againCount = objSatis.getString("againCount");
			String bestCount = objSatis.getString("bestCount");
			String picFlagCount = objSatis.getString("picFlagCount");
			String totalCount = objSatis.getString("totalCount");
			String qualityStar = objSatis.getString("qualityStar");
			
			bean.setOneStarCount(oneStarCount);
			bean.setTwoStarCount(twoStarCount);
			bean.setThreeStarCount(threeStarCount);
			bean.setFourStarCount(fourStarCount);
			bean.setFiveStarCount(fiveStarCount);
			bean.setAgainCount(againCount);
			bean.setBestCount(bestCount);
			bean.setPicFlagCount(picFlagCount);
			bean.setTotalCount(totalCount);
			bean.setQualityStar(qualityStar);

		}
		
		if(isSave == true){
			bean.saveOnNotExist();
		}
		
	}
	//http://review.suning.com/ajax/getreview_indivalides/general-{0}----commodityProperties.htm
	private String buildItemCommentLabesUrl(String id) {
		id = StringUtils.leftPad(id, 18, '0');
		return MessageFormat.format("http://review.suning.com/ajax/getreview_labels/general-{0}------commodityrLabels.htm", id);
	}
	
	private String buildItemCommentsatisfy(String id){
		id = StringUtils.leftPad(id, 18, '0');
		return MessageFormat.format("http://review.suning.com/ajax/review_satisfy/general-{0}------satisfy.htm", id);
		
	}
	
	public static void main(String args[]) {
		for(int i = 1; i<=50;i++){
			new SuningSearchCommentTitleFetch().run();
		}
	}
	
}
