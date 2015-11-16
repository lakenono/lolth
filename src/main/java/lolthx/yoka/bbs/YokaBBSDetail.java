package lolthx.yoka.bbs;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.yoka.bbs.bean.YokaBBSBean;
import lolthx.yoka.bbs.bean.YokaBBSUserBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class YokaBBSDetail extends DistributedParser{

	@Override
	public String getQueueName() {
		return "yoka_bbs_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		try {
			String setTime = doc.select("div.con_TopArea div.con_TopR dl.con_FloorNum dd").text();
			String[] times = setTime.split(" ");
			String postTime = "";
			if(times.length >= 2){
				postTime = times[1];
				//if (!isTime(postTime,task.getStartDate(),task.getEndDate())) {
					//return;
				//}
				
				String[] urls = task.getUrl().split("-");
				
				String id = urls[1];
				String url = task.getUrl();
				
				String title = doc.select("div.con_Header div.con_Title h1").text();
				String text = doc.select("div.con_Info").text();
				{
					Elements els = doc.select("div.con_Info img");
					for (Element el : els) {
						String attr = el.attr("src");
						text = text + " " + attr;
					}
				}
				
				String views = doc.select("div.con_Header div.con_Title div table tr td em").text();
				String replys = doc.select("div.con_Header div.con_Title div table tr td.line span").text();
				
				
				YokaBBSBean bean = new YokaBBSBean();
				bean.setId(id);
				bean.setProjectName(task.getProjectName());
				bean.setKeyword(task.getExtra());
				bean.setUrl(url);
				bean.setTitle(title);
				bean.setPostTime(postTime);
				bean.setViews(views);
				bean.setReplys(replys);
				bean.setText(text);
				
				Element el = doc.select("div.con_TopArea div.con_Top dl.con_TopL").first();
				YokaBBSUserBean user = new YokaBBSUserBean();
				this.parseUser(user, el);
				
				bean.setAuthorId(user.getAuthorId());
				bean.setAuthorName(user.getAuthorName());
				
				bean.saveOnNotExist();
				
				user.saveOnNotExist();
				
				int maxpage = 1;
				Element pagesEl = doc.select("div.bbs_CtrlArea dl.bbs_Page").first();
				Elements pagesEls = pagesEl.select("dt a");
				if(pagesEls.size() != 0){
					maxpage = pagesEls.size();
				}
				
				String commentUrl = StringUtils.replace(bean.getUrl(), "-1-1.html", "-{0}-1.html");
				for (int pagenum = 1; pagenum <= maxpage; pagenum++) {
					String seUrl = this.buildUrl(commentUrl, pagenum);
					Task newTask = buildTask(seUrl, "yoka_bbs_comment", task);
					Queue.push(newTask);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("handle yoka bbs detail error : {}",e.getMessage(),e,task.getExtra());
		}
	}
	
	private String buildUrl(String url , int pagenum){
		return MessageFormat.format(url, String.valueOf(pagenum));
	}
	
	private void parseUser(YokaBBSUserBean user , Element el) throws Exception {
		String authorId = el.select("dd > div b a").attr("nickname");
		String authorName = el.select("dd > div b a").text();
		String url = el.select("dd > div b a").attr("href");
		
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
		
		String innerText = el.select("dd > dl > dd").last().text();
		String[] innerStr = innerText.split(" ");
		String post = "";
		String essence = "";
		String registTime = "";
		if(innerStr.length > 2){
			post = StringUtils.substringAfter(innerStr[0], "帖子：");
			essence = StringUtils.substringAfter(innerStr[1], "精华：");
			registTime = StringUtils.substringAfter(innerStr[2], "注册时间：");
		}
		
		user.setAuthorId(authorId);
		user.setAuthorName(authorName);
		user.setUrl(url);
		user.setWork(work);
		user.setCurrency(currency);
		user.setHonor(honor);
		user.setAddress(address);
		user.setAttention(attention);
		user.setFans(fans);
		user.setPost(post);
		user.setEssence(essence);
		user.setRegistTime(registTime);
		
	}

	private boolean isTime(String time,Date start,Date end) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
			return between(start, end, srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}
	
	public static void main(String args[]){
		for(int i = 1; i<=5;i++){
			new YokaBBSDetail().run();
		}
	}
}
