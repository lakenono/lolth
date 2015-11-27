package lolthx.zhihu.search;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpRequestMethod;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lakenono.fetch.adv.utils.CookiesUtils;
import lakenono.fetcher.Fetcher;
import lolthx.zhihu.bean.ZhihuKwAnswersBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class ZhihuKwAnswersFetch extends DistributedParser{
	
	
	@Override
	public String getQueueName() {
		return "zhihu_kw_answers";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		JSONObject rateDetail = JSON.parseObject(result);
		JSONArray rateList = rateDetail.getJSONArray("msg");
		
		String[] extras = task.getExtra().split(":");
		
		String keyword = extras[0];
		String id = extras[1];
		String projectName = task.getProjectName();
		
		for (int i = 0; i < rateList.size(); i++) {
			Document doc = Jsoup.parse(rateList.getString(i));
			
			Element el = doc.select("div.zm-item-answer.zm-item-expanded").first();
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
			
			ZhihuKwAnswersBean answersBean = new ZhihuKwAnswersBean();
			
			answersBean.setId(id);
			answersBean.setProjectName(projectName);
			answersBean.setKeyword(keyword);
			answersBean.setUrl(answersUrl);
			answersBean.setAnswerId(answerId);
			answersBean.setAnswers(answers);
			answersBean.setAuthorId(authorId);
			answersBean.setAuthorName(authorName);
			answersBean.setLikes(likes);
			answersBean.setPostTime(postTime);
			answersBean.saveOnNotExist();
			
			if(answersBean.getAuthorId() != null && !"".equals(answersBean.getAuthorId()) ){
				String userUrl = "http://www.zhihu.com/people/" + answersBean.getAuthorId();
				String userquene = "zhihu_kw_user";
				Task userTask = buildTask(userUrl, userquene , task);
				Queue.push(userTask);
			}
			
		}

	}
	
	@Override
	protected String fetch(Fetcher fetcher, String cookies, String charset, Task task) throws Exception {
		HttpClientFetcher hcFetcher = new HttpClientFetcher();
		
		String taskurl = task.getUrl();
		String url = StringUtils.substringBefore(taskurl, "&questionId");

		HttpRequest request = new HttpRequest(url);
		request.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
		//cookies = "_za=2c955307-fb58-4c84-94c5-24da564e2dd3; __utma=51854390.805777264.1448247422.1448247422.1448265703.2; __utmz=51854390.1448247422.1.1.utmcsr=sogou|utmccn=(organic)|utmcmd=organic|utmctr=dota; q_c1=e77f47cf779f4384940b6b847877b32c|1447988112000|1447988112000; cap_id=ZWU5Y2IwNzYxNGMyNDQxOTgyNjg0NDNlNjhhY2NlYzA=|1447989375|b8ab64b7e50510f823f09942b8ba866057644f99; z_c0=QUJDTXQ5SEhDQWtYQUFBQVlRSlZUWWdmZGxZdXZfSEZhamJXNzEyNFlqeWtBcDVuQmFiMVJRPT0=|1447989896|dcffa0c4fa063fc35cb5127062cd72f60154ad27; _xsrf=1ebc66155d695a58f812959688f01ba0; __utmv=51854390.100-2|2=registration_date=20151120=1^3=entry_date=20151120=1; __utmb=51854390.2.10.1448265703; __utmc=51854390; __utmt=1";
		request.setCookies(CookiesUtils.getCookies(cookies));
		request.setRedirectsEnabled(false);
		request.setNeedContent(true);
		
		String html = "";
		String questionId = StringUtils.substringBetween(taskurl, "questionId=","&offset");
		String offset = StringUtils.substringAfter(taskurl, "&offset=");
		String params = "{\"url_token\":" + questionId + ",\"pagesize\":50,\"offset\":" + offset + "}";
		
		Map<String, String> maps = new HashMap<String, String>(3);
		maps.put("method", "next");
		maps.put("params", params);
		maps.put("chart", "utf-8");
		request.setParams(maps);
		request.setMethod(HttpRequestMethod.POST);
		
		HttpResponse resp = hcFetcher.run(request);
		html = new String(resp.getContent(), "UTF-8");
		if (StringUtils.isBlank(html)) {
			log.error("知乎 post提交返回数据为空！");
			hcFetcher.close();
			return "";
		}
		
		hcFetcher.close();
		return html;
	}
	
	@Override
	protected String getCookieDomain() {
		return "zhihu.com";
	}
	
	public static void main(String[] args) throws InterruptedException{
		for(int i = 1 ;i <= 30 ; i++){
			new ZhihuKwAnswersFetch().run();
			Thread.sleep(30000);
		}
		
	}
	
}
