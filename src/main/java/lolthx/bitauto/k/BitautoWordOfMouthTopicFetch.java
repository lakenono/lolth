package lolthx.bitauto.k;

import java.sql.SQLException;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.bitauto.bean.BitautoBBSUserBean;
import lolthx.bitauto.bean.BitautoWordOfMouthBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoWordOfMouthTopicFetch extends DistributedParser  {
	
	@Override
	public String getQueueName() {
		return "bitauto_kb_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		
		Elements postRights = doc.select("div.post_fist div.postright");
		if (postRights.isEmpty()) {
			return;
		}
		Element postRight = postRights.first();
		
		BitautoWordOfMouthBean bean  = new BitautoWordOfMouthBean();
		
		String id = StringUtils.substringBetween(task.getUrl(), "-", ".html");
		bean.setId(id);
		
		// views 点击 and 回复
		String views_replys = doc.select("div.title_box span").text();
		
		//点击率
		String views = StringUtils.substringAfter(views_replys, "/").trim();
		bean.setViews(views);
				
		//回复率
		String replys = StringUtils.substringBefore(views_replys, "/").trim();
		bean.setReplys(replys);
		
		Elements content = postRight.select("div.post_width");
		if (!content.isEmpty()) {
			bean.setContent(content.first().text());
		}
		
		carDescription(postRight, bean);
		
		parseKoubeiScore(postRight, bean);

		parseKoubeComment(postRight, bean);
		
		try {
			bean.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//parseUser(doc);
		
	}
	
	public void carDescription(Element postRight,BitautoWordOfMouthBean post){
		Elements carDess = postRight.select("div.koubei_jia ul li");
		
		if(carDess.isEmpty()){
			return;
		}
		
		for (Element dl : carDess) {
			String data = dl.text();
			if (StringUtils.startsWith(data, "裸车价：")) {
				post.setPrice(StringUtils.trim(StringUtils.substringBetween(data,  "：", "|")));
			}
			if(StringUtils.endsWith(data, "购车|")){
				post.setBuyTime(StringUtils.trim(StringUtils.substringBefore(data, "购车")));
			}
		}	
	}
	
	
	private void parseKoubeComment(Element postRight, BitautoWordOfMouthBean post) {
		Elements kbTable = postRight.select("table.kb_compare3");
		if (kbTable.isEmpty()) {
			return;
		}

		Element kbScore = kbTable.first();

		// 外观
		Element exteriorscores = kbScore.getElementById("exteriorscores");
		if (exteriorscores != null) {
			post.setExteriorScores(exteriorscores.attr("value"));
		}

		// 内饰
		Element interiorscores = kbScore.getElementById("interiorscores");
		if (interiorscores != null) {
			post.setInteriorScores(interiorscores.attr("value"));
		}

		// 空间
		Element spacescores = kbScore.getElementById("spacescores");
		if (spacescores != null) {
			post.setSpaceScores(spacescores.attr("value"));
		}

		// 动力
		Element powerscores = kbScore.getElementById("powerscores");
		if (powerscores != null) {
			post.setPowerScores(powerscores.attr("value"));
		}

		// 操控
		Element operationscores = kbScore.getElementById("operationscores");
		if (operationscores != null) {
			post.setOperationScores(operationscores.attr("value"));
		}

		// 配置
		Element configscores = kbScore.getElementById("configscores");
		if (configscores != null) {
			post.setConfigScores(configscores.attr("value"));
		}

		// 性价比
		Element costperformancescores = kbScore.getElementById("costperformancescores");
		if (costperformancescores != null) {
			post.setCostperformanceScores(costperformancescores.attr("value"));
		}

		// 舒适度
		Element comfortscores = kbScore.getElementById("comfortscores");
		if (comfortscores != null) {
			post.setComfortScores(comfortscores.attr("value"));
		}

	}

	private void parseKoubeiScore(Element postRight, BitautoWordOfMouthBean post) {
		Elements kbDivs = postRight.select("div.koubeilist");
		if (kbDivs.isEmpty()) {
			return;
		}

		Elements dls = kbDivs.first().getElementsByTag("dl");
		for (Element dl : dls) {
			String data = dl.text();

			if (StringUtils.startsWith(data, "外观：")) {
				post.setExteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "内饰：")) {
				post.setInteriorComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "空间：")) {
				post.setSpaceComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "动力：")) {
				post.setPowerComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "操控：")) {
				post.setOperationComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "配置：")) {
				post.setConfigComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "性价比：")) {
				post.setCostperformanceComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "舒适度：")) {
				post.setComfortComment(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "购车时间：")) {
				post.setBuyTime(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "裸车价格：")) {
				post.setPrice(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}

			if (StringUtils.startsWith(data, "当前里程：")) {
				post.setCurrentMiles(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}
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
	
	public static void main(String args[]){
		for(int i = 1; i<=35;i++){
			new BitautoWordOfMouthTopicFetch().run();
		}
	}
}
