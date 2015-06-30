package lolth.hupu.bbs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.hupu.bbs.bean.UserBean;

public class HupuBBSUserFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		
		return "hupu_bbs_user";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if(StringUtils.isBlank(result)){
			return;
		}
		Document doc = Jsoup.parse(result);
		String url = task.getUrl();
		String uid = doc.select("#uid").attr("value");
		UserBean userBean = new UserBean();
		userBean.setId(uid);
		userBean.setUrl(url);
		
		Elements main = doc.select("#main");
		if(main.isEmpty()){
			return;
		}
		String name = main.select("h3.mpersonal > div").text();
		userBean.setUsername(name);
		parseUserInfo(main,userBean);
		//#main > div.person_set > div.brief.m5px
		userBean.setInterest(main.select("div.brief.m5px").text());
		userBean.persistOnNotExist();
	}

	private void parseUserInfo(Elements main, UserBean userBean) {
		Elements div = main.select("div.personalinfo");
		if(div.isEmpty()){
			return;
		}
		List<Node> childNodes = div.first().childNodes();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < childNodes.size(); i++) {
			Node node = childNodes.get(i);
			if (node instanceof Element) {
				Element tmp = (Element) node;
				if ("br".equals(tmp.tagName())) {
					sb.append("\t");
					continue;
				}
				if (StringUtils.isNotBlank(tmp.text())) {
					sb.append(StringUtils.deleteWhitespace(tmp.text()));
				}
			} else if (node instanceof TextNode) {
				TextNode tmp = (TextNode) node;
				if (StringUtils.isNotBlank(tmp.text())) {
					sb.append(StringUtils.deleteWhitespace(tmp.text()));
				}
			}
		}
		String [] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(sb.toString(), "\t");
		String [] tmp = null;
		sb.setLength(0);
		for (String token : tokens) {
			tmp = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, "：");
			if(tmp.length>1){
				if(tmp[0].indexOf("性")>-1){
					userBean.setSex(tmp[1]);
				}else if(tmp[0].indexOf("地")>-1){
					userBean.setAddress(tmp[1]);
				}else if(tmp[0].indexOf("主队")>-1){
					sb.append(tmp[1]).append("|");
				}else if(tmp[0].indexOf("团队")>-1){
					userBean.setTeam(tmp[1]);
				}else if(tmp[0].indexOf("职务")>-1){
					userBean.setDuty(tmp[1]);
				} else if(tmp[0].indexOf("等级")>-1){
					userBean.setLevel(tmp[1]);
				} else if(tmp[0].indexOf("卡")>-1) {
					userBean.setCalorie(tmp[1]);
				} else if(tmp[0].indexOf("在线")>-1){
					userBean.setOnlineTime(tmp[1]);
				} else if(tmp[0].indexOf("加入")>-1){
					userBean.setJoinedTime(tmp[1]);
				}else if(tmp[0].indexOf("登录")>-1){
					userBean.setRegistering(tmp[1]);
				}
			}
		}
		if(sb.length() > 1){
			userBean.setHomeTeam(sb.substring(0,sb.length()-1));
		}
	}

}
