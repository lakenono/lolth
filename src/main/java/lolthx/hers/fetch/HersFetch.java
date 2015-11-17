package lolthx.hers.fetch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedPageParser;
import lakenono.base.Task;
import lolthx.hers.bean.HersBbsComment;
import lolthx.hers.bean.HersBbsMainText;
import lolthx.hers.bean.HersBbsWeibo;
import lolthx.hers.task.HersSearchTask;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HersFetch extends DistributedPageParser {
	public static String COMMEN_QUEUE = "hers_comment";
	String id;

	@Override
	public String getQueueName() {
		return HersSearchTask.QUEUE_BBS;
	}

	@Override
	public int getMaxPage(String result, Task task) throws Exception {
		String tmp = task.getExtra();
		String [] tmps = tmp.split(":");
		id=tmps[0];
		Document doc = Jsoup.parse(result);
		Elements select = doc.select("label > span[title]");
		if(select.isEmpty()){
			return 0;
		}
		String page = select.first().text();
		if(!StringUtils.isBlank(page)){
			page = StringUtils.substringBetween(page, "/", "页").trim();
			task.setExtra(tmps[0]);
			return Integer.parseInt(page) - 1;
		}else{
		return 0;
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		int num = pageNum +1;
		return "http://bbs.hers.com.cn/thread-"+id+"-"+num+"-1.html";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		String tmp = task.getExtra();
		String [] tmps = tmp.split(":");
		id=tmps[0];
		// 抽取主贴
		extrctMainText(doc, task,tmps[0],tmps[1]);
		// 抽取评论
		extactComment(doc, task,tmps[0]);
		//抽取微博
		extactWeibo(doc, task,tmps[0],tmps[1]);
	}

	private void extactWeibo(Document doc, Task task,String id,String keyword) throws IllegalAccessException, SQLException {
		Elements wb = doc.select("div.wc_list > div");
		if(wb.isEmpty()){
			return;
		}
		List<HersBbsWeibo> beans = new ArrayList<HersBbsWeibo>();
		for (Element element : wb) {
			HersBbsWeibo weiboBean = new HersBbsWeibo();
			String time = element.select("span.pub_time").text();
			String content = element.select("div.content_txt").text();
			String weiboName =  element.select("a.user_name").text();
			String weiboUrl = element.select("a.user_name").attr("href");
//			weiboBean.setId(task.getExtra());
			weiboBean.setId(id);
			weiboBean.setContent(content);
			weiboBean.setTime(time);
			weiboBean.setWeiboName(weiboName);
			weiboBean.setWeiboUrl(weiboUrl);
			beans.add(weiboBean);
		}
		if (!beans.isEmpty()) {
			for (HersBbsWeibo hersBbsWeibo : beans) {
				hersBbsWeibo.saveOnNotExist();
			}
		}
		beans.clear();
	}
	
	@Override
	protected Task buildTask(String url, Task perTask) {
//		Task t = new Task();
//		t.setProjectName(perTask.getProjectName());
//		t.setExtra(perTask.getExtra());
//		t.setUrl(url);
//		t.setQueueName(COMMEN_QUEUE);
		Task task = super.buildTask(url, perTask);
		task.setQueueName(COMMEN_QUEUE);
		return task;
	}

	public void extactComment(Document doc, Task task,String id) throws IllegalAccessException, SQLException {
		Elements elements = doc.select("div[id^=post_] > table");
		if (elements.size() < 1) {
			return;
		}
		// 从第二个元素开始获取评论
		List<HersBbsComment> beans = new ArrayList<>();
		for (int i = 1; i < elements.size(); i++) {
			Element element = elements.get(i);
			String comment = element.select("td.t_f").text();
			String time = element.select("em[id]").text();
			time = StringUtils.remove(time, "发表于").trim();
			String userName = element.select("div.authi a.xw1").text();
			String text = element.select("dl.pil.cl.dn").text();
			String area = "";
			String lastOnline = "";
			String onlineTime = "";
			if(!StringUtils.isBlank(text)){
				 area = StringUtils.substringBetween(text, "P", "在").trim();
				 lastOnline = StringUtils.substringBetween(text, "录", "主").trim();
				 onlineTime = StringUtils.substringBetween(text, "间", "小").trim();
			}
			HersBbsComment bean = new HersBbsComment();
//			bean.setId(task.getExtra());
			bean.setId(id);
			bean.setArea(area);
			bean.setComment(comment);
			bean.setLastOnline(lastOnline);
			bean.setOnlineTime(onlineTime);
			bean.setTime(time);
			bean.setUserName(userName);
			beans.add(bean);
		}
		if (!beans.isEmpty()) {
			for (HersBbsComment hersBbsComment : beans) {
				hersBbsComment.saveOnNotExist();
			}
		}
		beans.clear();
	}

	private void extrctMainText(Document doc, Task task,String id,String keyword) throws Exception {
		// 标题
		String title = doc.select("h1 > a#thread_subject").text();
		// 查看数 回复数
		Elements select = doc.select("span.xi_1");
		String readers = "";
		String replies = "";
		if (select.size() == 2) {
			readers = select.get(0).text();
			replies = select.get(1).text();
		}
		Elements t = doc.select("div.ptg.mbm > a");
		StringBuilder sb = new StringBuilder();
		for (Element element : t) {
			sb.append(element.text()).append(",");
		}
		// tags 收藏
		String tags = sb.toString();
		String store = doc.select("span#favoritenumber").text();
		// 内容 时间
		String text = doc.select("td.t_f[id]").first().text();
		String time = doc.select("em[id]").first().text();
		time = StringUtils.remove(time, "发表于").trim();
		HersBbsMainText hers = new HersBbsMainText();
//		hers.setId(task.getExtra());
		hers.setId(id);
		hers.setUrl(task.getUrl());
		hers.setKeyword(keyword);
		hers.setProjectName(task.getProjectName());
		hers.setReaders(readers);
		hers.setReplies(replies);
		hers.setStore(store);
		hers.setTags(tags);
		hers.setText(text);
		hers.setTime(time);
		hers.setTitle(title);
		hers.saveOnNotExist();
	}
	public static void main(String[] args) {
		new HersFetch().run();
	}
}
