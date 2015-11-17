package lolthx.hers.task;

import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HersSearchTask{
	private String keyword;
	private String projectName;
	public static final String QUEUE_PAGE = "hers_page";
	public static final String QUEUE_BBS = "hers_bbs";
	private String base_url = "http://bbs.hers.com.cn/search.php?srchtxt={0}&searchsubmit=yes";
	
	public HersSearchTask(String projectName, String keyword) {
		this.projectName = projectName;
		this.keyword = keyword;
	}

	public void run() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl());
		String pages = document.select("span[title]").text();
		String href = "";
		try{
			href = document.select("div.pg > a ").first().attr("href");
		}catch(NullPointerException e){
			
		}
		
		if(!StringUtils.isBlank(pages)){
			pages = StringUtils.substringBetween(pages, "/", "页").trim();
		}
		//发送帖子队列
		sendHersBbs(document,this.keyword);
		//发送页面队列
		if(StringUtils.isBlank(href)){
			return;
		}
		int p = Integer.parseInt(pages);
		href = href.substring(0, href.length()-1);
		for(int i = 2;i<=p;i++){
			//发送task
			String url = "http://bbs.hers.com.cn/"+href+i;
			System.out.println(url);
			buildTask(url,QUEUE_PAGE,this.keyword);
		}
	}

	public void sendHersBbs(Document document,String keywordStr) {
		Elements elements = document.select("li.pbw[id]");
		for (Element element : elements) {
			String id = element.attr("id");
			String url = "http://bbs.hers.com.cn/thread-"+id+"-1-1.html";
			buildTask(url,QUEUE_BBS,id+":"+keywordStr);
		}
	}

	private void buildTask(String url,String queueName,String id) {
		Task task = new Task();
		task.setQueueName(queueName);
		task.setUrl(url);
		task.setProjectName(this.projectName);
		task.setExtra(id);
		Queue.push(task);
	}

	private String buildUrl() throws Exception {
		return MessageFormat.format(base_url, URLDecoder.decode(keyword, "utf-8"));
	}
	
	public static void main(String[] args) {
		String [] keywords = {"曼苏丽尔"};
//		String[] keywords = {"LaMer","海蓝之谜","HR","赫莲娜","CHANEL","香奈儿","Dior","迪奥","EsteeLauder","雅诗兰黛","SK-II","LANCOME","兰蔻","ElizabethArden","伊丽莎白雅顿","Clarins","娇韵诗","Sisley","希思黎","Guerlain","娇兰","Clinique","倩碧","Maybelline","美宝莲","Avene","雅漾","Mentholatum","曼秀雷敦","SHISEIDO","资生堂","Biotherm","碧欧泉","Olay","玉兰油","L'OREAL","巴黎欧莱雅","AUPRES","欧珀莱","VICHY","薇姿","NIVEA","妮维雅","kiehl's","契尔氏","Garnier","卡尼尔","DHC","蝶翠诗","Za","姬芮","ARTISTRY","雅姿","Charmzone","婵真","Mamonde","梦妆","hera","赫拉","Innisfree","悦诗风吟","SKINFOOD","THE FACE SHOP","谜尚","MISSHA","it`s skin","伊思","IOPE","LANEIGE","兰芝","Sulwhasoo","雪花秀","whoo后","ohui","欧蕙","ETUDE HOUSE","爱丽小屋","相宜本草","LouisVuitton","路易威登","Gucci","古驰","CHANL","香奈儿","小香","PRADA","普拉达","HERMES","爱马仕","Dior","迪奥","bottegaveneta","葆蝶家","Burberry","博柏利","巴宝莉","chloe","蔻依","克洛伊","AlexanderMqueen","亚历山大·麦昆","marcjacobs","马克·雅可布","MansurGavriel","曼苏丽尔","versace","范思哲","CELINE","思琳","赛琳","FENDI","芬迪","MiuMiu","缪缪","givenchy","纪梵希","marcbymarcjacobs","Coach","蔻驰","寇驰","MichaelKors","迈克.科尔斯","Katespade","凯特·丝蓓","ToryBurch","汤丽柏琦","MCM","Furla","芙拉","GerardDarel","杰哈·达黑勒"};
//		String [] ss = {"CHANEL","Dior","EsteeLauder","HERMES","HR","LaMer","LANCOME","PRADA","SK-II","兰蔻","小香","普拉达","海蓝之谜","爱马仕","赫莲娜","迪奥","雅诗兰黛","香奈儿"};
		String projectName = "华扬联众演讲数据-hers-20151116";
//		
//		Set<String> set = new HashSet<String>();
//		for(String s:ss){
//			set.add(s);
//		}
		
		for(String keyword:keywords){
//			if(!set.contains(keyword)){
				System.out.println(keyword);
				try {
					new HersSearchTask(projectName,keyword).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//			}
			
		}
	}
}
