package lolthx.onlylady.bbs.search;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.utils.HttpURLUtils;
import lolthx.onlylady.bbs.bean.OnlyladyBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlyladyBBSTopicFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "onlylady_bbs_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		String url = task.getUrl();
		String id = HttpURLUtils.getUrlParams(url, "gbk").get("tid");
		if (StringUtils.isBlank(id)) {
			log.info("onlylady tid is empty : " + url);
			return;
		}
		Document doc = Jsoup.parse(result);
		
		Elements divs = doc.select("div.pl.bm > div");
		if(divs.isEmpty()){
			log.error("获取帖子楼层出错了");
			return;
		}
		OnlyladyBean bean = new OnlyladyBean();
		bean.setId(id);
		bean.setUrl(url);
		bean.setProjectName(task.getProjectName());
		bean.setKeyword(task.getExtra());
		Elements tmp = doc.select("div.ts");
		String title = tmp.text();
		bean.setTitle(title);
		tmp = doc.select("td.pls.ptm.pbm.pview > div > span.xi1");
		String vi = tmp.get(0).text();
		bean.setViews(vi);
		String reply = tmp.get(1).text();
		bean.setReply(reply);
		
		Element div = divs.first();
		String time = "";
		tmp = div.select("td.plc div.authi > em > span");
		if (tmp.isEmpty()) {
			tmp = div.select("td.plc div.authi > em");
			time = tmp.text().substring(4);
		} else {
			time = tmp.attr("title");
		}
		bean.setDatetime(time);
		tmp = div.select("div.ptg.mbm");
		for (int i = 0; i < tmp.size(); i++) {
			if (i == 0) {
				bean.setKeyword1(tmp.get(i).text());
			} else if (i == 1) {
				bean.setKeyword2(tmp.get(i).text());
			}
		}
		tmp = div.select("td.t_f");
		String context="";
		if(tmp.isEmpty()){
			context="提示: 作者被禁止或删除 内容自动屏蔽";
		}else{
			context = tmp.text();
		}
		bean.setText(context);
		tmp = div.select("td.pls div.authi > a");
		Element a = tmp.first();
		String userurl = a.attr("href");
		bean.setUid(StringUtils.substringBetween(userurl, "uid-", ".html"));
		bean.setUserName(a.text());
		
		bean.saveOnNotExist();
//		System.out.println(bean.toString());
		
		Task newTask = buildTask(userurl, "onlylady_bbs_user", task);
		Queue.push(newTask);

	}

	public static void main(String[] args) throws Exception {
//		String url = "http://bbs.onlylady.com/forum.php?mod=viewthread&tid=3362175&highlight=%C3%C0%D7%B1";
//		String fetch = GlobalComponents.jsoupFetcher.fetch(url);
//
//		String id = HttpURLUtils.getUrlParams(url, "gbk").get("tid");
//
//		Document doc = Jsoup.parse(fetch);
//		
//		Elements divs = doc.select("div.pl.bm > div");
//		if(divs.isEmpty()){
//			log.error("获取帖子楼层出错了");
//			return;
//		}
//		
//		OnlyladyBean bean = new OnlyladyBean();
//		bean.setId(id);
//		bean.setUrl(url);
//		Elements tmp = doc.select("div.ts");
//		String title = tmp.text();
//		bean.setTitle(title);
//		tmp = doc.select("td.pls.ptm.pbm.pview > div > span.xi1");
//		String vi = tmp.get(0).text();
//		bean.setViews(vi);
//		String reply = tmp.get(1).text();
//		bean.setReply(reply);
//		
//		Element div = divs.first();
//		String time = "";
//		tmp = div.select("td.plc div.authi > em > span");
//		if (tmp.isEmpty()) {
//			tmp = div.select("td.plc div.authi > em");
//			time = tmp.text().substring(4);
//		} else {
//			time = tmp.attr("title");
//		}
//		bean.setDatetime(time);
//		tmp = div.select("div.ptg.mbm");
//		for (int i = 0; i < tmp.size(); i++) {
//			if (i == 0) {
//				bean.setKeyword1(tmp.get(i).text());
//			} else if (i == 1) {
//				bean.setKeyword2(tmp.get(i).text());
//			}
//		}
//		tmp = div.select("td.t_f");
//		String context="";
//		if(tmp.isEmpty()){
//			context="提示: 作者被禁止或删除 内容自动屏蔽";
//		}else{
//			context = tmp.text();
//		}
//		bean.setText(context);
//		tmp = div.select("td.pls div.authi > a");
//		Element a = tmp.first();
//		String userurl = a.attr("href");
//		bean.setUid(StringUtils.substringBetween(userurl, "uid-", ".html"));
//		bean.setUserName(a.text());
//		System.out.println(bean.toString());
		Random r =new Random();
		
		for(int i = 0;i<10;i++){
			int nextInt = r.nextInt(9999);
			System.out.println(nextInt);
			new OnlyladyBBSTopicFetch().run();
			Thread.sleep(nextInt);
		}
	}

}
