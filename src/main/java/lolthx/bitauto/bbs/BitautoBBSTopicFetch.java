package lolthx.bitauto.bbs;

import java.sql.SQLException;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.bitauto.bean.BitautoBBSBean;
import lolthx.bitauto.bean.BitautoBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoBBSTopicFetch extends DistributedParser  {

	
	
	//获取上一个task未完成的任务
	@Override
	public String getQueueName() {
		return "bitauto_bbs_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		
		BitautoBBSBean bean  = new BitautoBBSBean();
		
		String id =  StringUtils.substringBetween(task.getUrl(),  "-", ".html");	
		bean.setId(id);
		
		// views 点击 and 回复
		String views_replys = doc.select("div.title_box span").text();
		
		//点击率
		String views = StringUtils.substringAfter(views_replys, "/");
		bean.setViews(views);
		
		//回复率
		String replys = StringUtils.substringBefore(views_replys, "/");
		bean.setReplys(replys);
		
		//System.out.println();
		
		String text = doc.select("div.post_text_sl div.post_width").first().text();
		
		{
			Elements elements = doc.select("div.post_text_sl div.post_width img");
			for (Element element : elements) {
				String attr = element.attr("src");
				text = text + " " + attr;
			}
		}
		
		// 车主信息
		bean.setText(text);
		try {
			bean.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		parseUser(doc);
		
	}
	
	//存储用户信息
	private void parseUser(Document doc) {
		//获取左上角用户信息
		Element topicElement = doc.select("div#postleft1").first();
		BitautoBBSUserBean bean = new BitautoBBSUserBean();
		
		//获取用户名称
		String name = topicElement.select("div.user_name a").text();
		bean.setName(name);
		
		//用户url
		String authorUrl = topicElement.select("div.user_name a").attr("href");
		bean.setAuthorUrl(authorUrl);
		
		//用户id
		String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");
		bean.setId(authorId);
		
		Elements lis = doc.select("div#postleft1 div.user_info li");
		
		String value;
		int split = 3;
		for (Element li : lis) {
			value = li.text();
			//System.out.println(">>>>>>>>>>>>>>>>>>>>" + li.text());
			if (StringUtils.startsWith(value, "地 区： ")) {	
				bean.setArea(StringUtils.trim(StringUtils.substringAfter(value, "：")));
			}else if (StringUtils.startsWith(value, "车 型：")) {
				bean.setCar(StringUtils.trim(StringUtils.substringAfter(value, "：")));
			}
		}
		try {
			bean.persistOnNotExist();
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		for(int i= 0;i < 40 ; i++){
			new BitautoBBSTopicFetch().run();
		}
	}
	
}
