package lolthx.zhihu.search;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.fetcher.Fetcher;
import lolthx.zhihu.bean.ZhihuKwUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZhihuKwUserFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "zhihu_kw_user";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		String url = task.getUrl();
		String id = StringUtils.substringAfter(url, "people/");
		String authorName = doc.select("div.top span.name").text();
		String selfIntroduction = doc.select("div.top span.bio").text();
		String address = doc.select("span.location.item").text();
		String industry = doc.select("span.business.item").text();
		String university = doc.select("span.education.item").text();
		String department =doc.select("span.education-extra.item").text();
		String brief = doc.select("span.fold-item span.content").text();
		String approval = doc.select("span.zm-profile-header-user-agree strong").text();
		String thanks = doc.select("span.zm-profile-header-user-thanks strong").text();
		String questions = "";
		String answers = "";
		String article = "";
		
		Elements els = doc.select("div.profile-navbar.clearfix a.item");
		for(Element el : els){
			String text = el.text();
			if (text.startsWith("提问")) {
				questions = StringUtils.substring(text, 3);
			} else if (text.startsWith("回答")) {
				answers = StringUtils.substring(text, 3);
			} else if (text.startsWith("专栏文章")) {
				article = StringUtils.substring(text, 5);
			}
		}
		
		ZhihuKwUserBean userBean = new ZhihuKwUserBean();
		userBean.setId(id);
		userBean.setUrl(url);
		userBean.setAuthorName(authorName);
		userBean.setSelfIntroduction(selfIntroduction);
		userBean.setAddress(address);
		userBean.setIndustry(industry);
		userBean.setUniversity(university);
		userBean.setDepartment(department);
		userBean.setBrief(brief);
		userBean.setApproval(approval);
		userBean.setThanks(thanks);
		userBean.setQuestions(questions);
		userBean.setAnswers(answers);
		userBean.setArticle(article);
		userBean.saveOnNotExist();
		
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
	
	public static void main(String[] args) throws InterruptedException{
		for(int i = 0 ; i <= 30 ; i++){
			Thread.sleep(30000);
			new ZhihuKwUserFetch().run();
		}
	}
	
}
