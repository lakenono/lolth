package lolthx.zhihu.search;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.fetcher.Fetcher;
import lolthx.zhihu.bean.ZhihuKwAnswersBean;
import lolthx.zhihu.bean.ZhihuKwQuestionBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZhihuKwQuestionFetch extends DistributedParser{
	
	
	@Override
	public String getQueueName() {
		return "zhihu_kw_question";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		String projectName = task.getProjectName();
		String[] extras = task.getExtra().split(":");
		String keyword = extras[0];
		String likes = extras[1]; 
		String url = task.getUrl();
		String id = StringUtils.substringAfter(url, "question/");
		
		String labels = doc.select("div.zm-tag-editor.zg-section").text();
		String title = doc.select("div#zh-question-title").text();
		
		Element contEl = doc.select("div#zh-question-detail").first();
		String content = contEl.text();
		{
			Elements els = contEl.select("img");
			for (Element el : els) {
				String attr = el.attr("src");
				content = content + " " + attr;
			}
		}
		
		String replysText = doc.select("div.zm-meta-panel a.toggle-comment.meta-item").first().text();
		String replys = StringUtils.substringBefore(replysText, " 条评论");
		if(replysText.indexOf("添加评论") >= 0){
			replys = "0";
		}
		
		String answersText = doc.select("h3#zh-question-answer-num").text();
		String answers = StringUtils.substringBefore(answersText, " 个回答");
		
		ZhihuKwQuestionBean questionBean = new ZhihuKwQuestionBean();
		questionBean.setId(id);
		questionBean.setProjectName(projectName);
		questionBean.setKeyword(keyword);
		questionBean.setUrl(url);
		questionBean.setTitle(title);
		questionBean.setContent(content);
		questionBean.setLabels(labels);
		questionBean.setReplys(replys);
		questionBean.setAnswers(answers);
		questionBean.setLikes(likes);
		
		questionBean.saveOnNotExist();
		
		Elements els = doc.select("div.zm-item-answer.zm-item-expanded");
		for(Element el : els){
			ZhihuKwAnswersBean answersBean = new ZhihuKwAnswersBean();
			try {
				answersBean.setId(id);
				answersBean.setProjectName(projectName);
				answersBean.setKeyword(keyword);
				this.parseAnsers(el, answersBean);
				answersBean.saveOnNotExist();
				
				String userUrl = "http://www.zhihu.com/people/" + answersBean.getAuthorId();
				String userquene = "zhihu_kw_user";
				Task userTask = buildTask(userUrl, userquene , task);
				Queue.push(userTask);
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}
	
	public void parseAnsers(Element el , ZhihuKwAnswersBean answersBean){
		Element answersEl = el.select("div.zm-editable-content.clearfix").first();
		String answers = answersEl.text();
		{
			Elements els = answersEl.select("img");
			for (Element ele : els) {
				String attr = ele.attr("src");
				answers = answers + " " + attr;
			}
		}
		
		String likes = el.select("span.count").text();
		String answerId = el.attr("data-atoken");
		String postText = el.select("span.answer-date-link-wrap a").text();
		String answersHref = el.select("span.answer-date-link-wrap a").attr("href");
		String answersUrl = "http://www.zhihu.com" + answersHref ;
		String postTime = postText.split(" ")[1];
		String authorUrl = el.select("div.zm-item-answer-author-info a.author-link").attr("href");
		String authorName = el.select("div.zm-item-answer-author-info a.author-link").text();
	
		String authorId = StringUtils.substringAfter(authorUrl, "people/");

		if(postTime.indexOf("昨天") >= 0){
			Calendar cal  =  Calendar.getInstance();
			cal.add(Calendar.DATE,  -1);
			postTime = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
		}else if(postTime.indexOf(":") >= 0){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			postTime = df.format(new Date());
		}
		
		answersBean.setUrl(answersUrl);
		answersBean.setAnswerId(answerId);
		answersBean.setAnswers(answers);
		answersBean.setAuthorId(authorId);
		answersBean.setAuthorName(authorName);
		answersBean.setLikes(likes);
		answersBean.setPostTime(postTime);
		
	}
	
	@Override
	protected String fetch(Fetcher fetcher, String cookies, String charset, Task task) throws Exception {
		//cookies = "_za=2c955307-fb58-4c84-94c5-24da564e2dd3; __utma=51854390.805777264.1448247422.1448247422.1448265703.2; __utmz=51854390.1448247422.1.1.utmcsr=sogou|utmccn=(organic)|utmcmd=organic|utmctr=dota; q_c1=e77f47cf779f4384940b6b847877b32c|1447988112000|1447988112000; cap_id=ZWU5Y2IwNzYxNGMyNDQxOTgyNjg0NDNlNjhhY2NlYzA=|1447989375|b8ab64b7e50510f823f09942b8ba866057644f99; z_c0=QUJDTXQ5SEhDQWtYQUFBQVlRSlZUWWdmZGxZdXZfSEZhamJXNzEyNFlqeWtBcDVuQmFiMVJRPT0=|1447989896|dcffa0c4fa063fc35cb5127062cd72f60154ad27; _xsrf=1ebc66155d695a58f812959688f01ba0; __utmv=51854390.100-2|2=registration_date=20151120=1^3=entry_date=20151120=1; __utmb=51854390.2.10.1448265703; __utmc=51854390; __utmt=1";
		return super.fetch(fetcher, cookies, charset, task);
	}
	
	@Override
	protected String getCookieDomain() {
		return "zhihu.com";
	}
	
	public static void main(String[] args){
		new ZhihuKwQuestionFetch().run();
	}

}
