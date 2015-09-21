package lolthx.suning.keysearch;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SuningSearchListFetch extends DistributedParser {

	
	
	@Override
	public String getQueueName() {
		return "suning_search_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		Elements itemAs = doc.select("ul.items.clearfix  li.item");
		for (Element item : itemAs) {
		
			String url = item.select("a.sellPoint").first().absUrl("href");
			String id = StringUtils.substringAfterLast(url, "/");
			id = StringUtils.substringBefore(id, ".");
			
			task.setExtra(id);
			
			//推送商品信息
			Task newTask = buildTask(url, "suning_search_detail", task);
			Queue.push(newTask);
			
			//推送商品评论
			String comment = item.select("div.i-stock a.comment span.com-cnt").first().text();
			String max = StringUtils.substringBefore(comment, "条");
			if(!max.equals("") && !max.equals("0")){
				Integer pages = Integer.valueOf(max) / 10 + 1;
				for(int i = 1 ; i <=pages ; i++){
					Task newCommentTask = buildTask(buildItemCommentUrl(id,i), "suning_comment_list", task);
					Queue.push(newCommentTask);
				}
			}
			
			//推送商品标题头
			Task newCommTitleTask = buildTask(buildItemCommTitleUrl(id), "suning_comment_title", task);
			Queue.push(newCommTitleTask);
			
		}
		
	}
	
	private String buildItemCommentUrl(String id,int page) {
		id = StringUtils.leftPad(id, 18, '0');
		return MessageFormat.format("http://review.suning.com/ajax/review_lists/general-{0}--total-{1}-default-10-----reviewList.htm", id,String.valueOf(page));
	}
	
	private String buildItemCommTitleUrl(String id){
		id = StringUtils.leftPad(id, 18, '0');
		return MessageFormat.format("http://review.suning.com/ajax/getreview_indivalides/general-{0}----commodityProperties.htm", id);
	}
	
	//suning 查询
	public static void main(String args[]){
		for(int i = 1 ; i<=10 ; i++){
			new SuningSearchListFetch().run();
		}
	}
	
}
