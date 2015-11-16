package lolthx.yoka.bbs;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.yoka.bbs.bean.YokaBBSCommentBean;
import lolthx.yoka.bbs.bean.YokaBBSUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class YokaBBSComment extends DistributedParser {

	@Override
	public String getQueueName() {	
		return "yoka_bbs_comment";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		Elements elements = doc.select("div.con_Lst");
		
		String[] urls = task.getUrl().split("-");
		String id = "";
		if(urls.length == 4){
			id = urls[1];
		}
		String url = task.getUrl();
		String projectName = task.getProjectName();
		String title = doc.select("div.con_Header div.con_Title h1").text();
		String keyword = task.getExtra();
		
		for (Element element : elements) {
			try {
				YokaBBSCommentBean commentBean = new YokaBBSCommentBean();
				Element ele = element.select("dl.con_FloorNum dd").first();
				String[] floorDetail = ele.select("dd").text().replaceAll("发表于 ", "").split(" ");
				String commentTime = "";
				String floor = "";
				
				if(floorDetail.length > 1){
					commentTime = floorDetail[0];
					floor = floorDetail[1];
				}
				String comment = element.select("td.con_content").text();
				
				commentBean.setId(id);
				commentBean.setProjectName(projectName);
				commentBean.setUrl(url);
				commentBean.setFloor(floor);
				commentBean.setCommentTime(commentTime);
				commentBean.setTitle(title);
				commentBean.setKeyword(keyword);
				commentBean.setComment(comment);
				
				Element el1 = element.select("td.con_LstL dl.con_Uname a").first();
				Element el2 = element.select("td.con_LstL.con_LstLBtm dl.con_Uinfo dl.blue dd").first();
				YokaBBSUserBean userBean = new YokaBBSUserBean();
				this.parseUser(userBean, el1 , el2);
				
				commentBean.setAuthorId(userBean.getAuthorId());
				commentBean.setAuthorName(userBean.getAuthorName());
				
				commentBean.saveOnNotExist();
				
				userBean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle yoka bbs comment error : {}",e.getMessage(),e,task.getExtra());
			}
		}
	}

	private void parseUser(YokaBBSUserBean userBean , Element el1 , Element el2) throws Exception {
		String authorId = el1.attr("nickname");
		String authorName= el1.text();
		String url = el1.attr("href");
		
		
		String sendUrl = "http://space.yoka.com/services/get_bjc_userinfo_bbs.php?type=bbs&uid=" + authorId;
		String html = GlobalComponents.jsoupFetcher.fetch(sendUrl);
		
		html = StringUtils.substringBetween(html, "new bjc_UserInfo(", ");");
		
		String[] details = html.replaceAll("'", "").split(",");
		String work = "";
		String currency = "";
		String honor = "";
		String address = "";
		String attention = "";
		String fans = "";
		if(details.length > 8){
			work = details[1];
			address = details[2];
			honor =details[3];
			currency = details[5];
			attention = details[7];
			fans = details[8];
		}
		
		String innerText = el2.text();
		String[] innerStr = innerText.split(" ");
		String post = "";
		String essence = "";
		String registTime = "";
		
		if(innerStr.length > 2){
			post = StringUtils.substringAfter(innerStr[0], "帖子：");
			essence = StringUtils.substringAfter(innerStr[1], "精华：");
			registTime = StringUtils.substringAfter(innerStr[2], "注册时间：");
		}
		
		userBean.setAuthorId(authorId);
		userBean.setAuthorName(authorName);
		userBean.setUrl(url);
		userBean.setWork(work);
		userBean.setCurrency(currency);
		userBean.setHonor(honor);
		userBean.setAddress(address);
		userBean.setAttention(attention);
		userBean.setFans(fans);
		userBean.setPost(post);
		userBean.setEssence(essence);
		userBean.setRegistTime(registTime);
		
	}
	
	
	public static void main(String[] args){
		for(int i = 1 ; i <= 5; i++){
			new YokaBBSComment().run();
		}
	}
	
	
}
