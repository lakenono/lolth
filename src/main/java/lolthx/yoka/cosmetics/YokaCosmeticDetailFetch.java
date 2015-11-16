package lolthx.yoka.cosmetics;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.yoka.cosmetics.bean.YokaCosmeticCommentBean;
import lolthx.yoka.cosmetics.bean.YokaCosmeticDetailBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YokaCosmeticDetailFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "yoka_cosmetic_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result); 
		
		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "commentreply", ".htm");
		String extra = task.getExtra();
		String[] extras = extra.split(":");
		String forumId = "";
		String keyword = "";
		String comment = "";
		if(extras.length >= 3){
			forumId = extras[0];
			keyword = extras[1];
			comment = extras[2];
		}
		
		String authorUrl = doc.select("div#hzp-login div.info.xinde > dl > dd > h2 > a").attr("href");
		String authorId = StringUtils.substringAfter(authorUrl, "cosmetics/");
		String authorName =  doc.select("div#hzp-login div.info.xinde > dl > dd > h2 > a").text();
		
		Element titleEl =  doc.select("div.box_xinde div.title_xinde").first();
		String title = titleEl.select("h1").text();
		String scores = titleEl.select("dl dt i").text();
		String postTime = titleEl.select("dl > dd > em").text();
		String beOfUsed = titleEl.select("ul#xinde_handle2 li span#availNum").text();
		String collection = titleEl.select("ul#xinde_handle2 li span#collectNum").text();
		String projectName = task.getProjectName();
		
		Element textEl = doc.select("div.xinde_contentBox").first();
		String text = textEl.text();
		{
			Elements els = textEl.select("img");
			for (Element el : els) {
				String attr = el.attr("src");
				text = text + " " + attr;
			}
		}
		
		String replyText = doc.select("div.comment_title").text();
		String reply = StringUtils.substringBetween(replyText, "共", "条");
		
		YokaCosmeticDetailBean detailBean = new YokaCosmeticDetailBean();
		detailBean.setId(id);
		detailBean.setProjectName(projectName);
		detailBean.setForumId(forumId);
		detailBean.setKeyword(keyword);
		detailBean.setAuthorId(authorId);
		detailBean.setAuthorName(authorName);
		detailBean.setScores(scores);
		detailBean.setUrl(url);
		detailBean.setTitle(title);
		detailBean.setPostTime(postTime);
		detailBean.setBeOfUsed(beOfUsed);
		detailBean.setCollection(collection);
		detailBean.setText(text);
		detailBean.setReply(reply);
		detailBean.setComment(comment);
		detailBean.saveOnNotExist();
		
		YokaCosmeticCommentBean commentBean = null;
		Elements commentEls = doc.select("div.comment_list div.comment_item");
		for(Element el : commentEls){
			commentBean = new YokaCosmeticCommentBean();
			commentBean.setId(id);
			commentBean.setProjectName(projectName);
			commentBean.setForumId(forumId);
			commentBean.setKeyword(keyword);
			commentBean.setUrl(url);
			this.parseComment(el , commentBean );
			
			String userUrl = "http://g.yoka.com/cosmetics/" + commentBean.getAuthorId();
			Task userTask = buildTask(userUrl, "yoka_cosmetic_user", task);
			Queue.push(userTask);
			
			commentBean.saveOnNotExist();
		}
	}
	
	
	public void parseComment(Element el , YokaCosmeticCommentBean commentBean){
		String userUrl = el.select("span.comment_user_icon a.imgborder").attr("href");
		String authorId = StringUtils.substringAfter(userUrl, "cosmetics/");
		String str1 = el.select("div.comment_infos div.comment_info_con div span").text();
		String[] str1s = str1.split(" ");
		String postTime = "";
		String floor = "";	
		if(str1s.length >= 3){
			postTime = str1s[0] + str1s[1];
			floor = str1s[2];
		}
		
		String authorName = el.select("div.comment_infos div.comment_info_con div a.cblue").text();
		String comment = el.select("div.comment_infos div.comment_info_con p").text();
		{
			Elements els = el.select("div.comment_infos div.comment_info_con p img");
			for (Element ele : els) {
				String attr = ele.attr("src");
				comment = comment + " " + attr;
			}
		}
		
		commentBean.setFloor(floor);
		commentBean.setPostTime(postTime);
		commentBean.setAuthorId(authorId);
		commentBean.setAuthorName(authorName);
		commentBean.setComment(comment);
	}
	
	public static void main(String[] args){
		for(int i = 1; i <= 1000 ; i++){
			new YokaCosmeticDetailFetch().run();
		}

	}
	
	
}
