package lolth.official.oppo;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.official.oppo.bean.OppoUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OppoForumPostUserFetch extends PageParseFetchTaskHandler{

	public OppoForumPostUserFetch(String taskQueueName) {
		super(taskQueueName);
	}
	
	public static void main(String[] args) throws Exception{
		String taskQueueName = OppoForumPostUserTaskProducer.OPPO_FORUM_POST_USER;
		OppoForumPostUserFetch fetch = new OppoForumPostUserFetch(taskQueueName);
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		OppoUserBean user = new OppoUserBean();
		
		Elements info = doc.select("div.infobox");
		if(info.size()>0){
			//名称
			Elements name = info.select("div.info_name span.name");
			if(name.size()>0){
				user.setName(name.first().text());
			}
			
			// 用户组
			Elements groupName = info.select("div.info_name span.qdlevel");
			if(groupName.size()>0){
				user.setGroupName(groupName.first().text());
			}
			
			//性别
			Elements sex = info.select("div.info_info span");
			if(sex.size()>0){
				String sexStr = sex.first().text();
				if("男生女生保密".contains(sexStr)){
					user.setSex(sexStr);
				}
			}
			
			//地区
			Elements area = info.select("div.info_info");
			if(area.size()>0){
				user.setArea(area.first().ownText());
			}
		}
		
		Elements ul = doc.select("ul.creditbox");
		if(ul.size()>0){
			Element ulElement = ul.first();
			
			Elements integral = ulElement.getElementsMatchingOwnText("积 分");
			if(integral.size()>0){
				String integralStr = integral.first().parent().ownText();
				integralStr = StringUtils.substringAfter(integralStr, "：");
				user.setIntegral(integralStr);
			}
			
			Elements oCoins = ulElement.getElementsMatchingOwnText("Ｏ 币");
			if(oCoins.size()>0){
				String oCoinsStr = oCoins.first().parent().ownText();
				oCoinsStr = StringUtils.substringAfter(oCoinsStr, "：");
				user.setOCoins(oCoinsStr);
			}
			
			Elements medel = ulElement.getElementsMatchingOwnText("勋 章");
			if(medel.size()>0){
				String medelStr = medel.first().parent().ownText();
				medelStr = StringUtils.substringAfter(medelStr, "：");
				user.setMedel(medelStr);
			}
			
			Elements achievement = ulElement.getElementsMatchingOwnText("成 就 值");
			if(achievement.size()>0){
				String achievementStr = achievement.first().parent().ownText();
				achievementStr = StringUtils.substringAfter(achievementStr, "：");
				user.setAchievement(achievementStr);
			}
			
			Elements contribution = ulElement.getElementsMatchingOwnText("贡 献");
			if(oCoins.size()>0){
				String contributionStr = contribution.first().parent().ownText();
				contributionStr = StringUtils.substringAfter(contributionStr, "：");
				user.setContribution(contributionStr);
			}
			
			Elements oppoIntegral = ulElement.getElementsMatchingOwnText("OPPO 积分");
			if(oppoIntegral.size()>0){
				String oppoIntegralStr = oppoIntegral.first().parent().ownText();
				oppoIntegralStr = StringUtils.substringAfter(oppoIntegralStr, "：");
				user.setOppoIntegral(oppoIntegralStr);
			}
		}
		
		Elements div = doc.select("div.profile_main");
		if(div.size()>0){
			Element divElement = div.first();
			
			Elements education = divElement.getElementsMatchingOwnText("学历：");
			if(education.size()>0){
				user.setEducation(education.first().nextElementSibling().text());
			}
			
			Elements marry = divElement.getElementsMatchingOwnText("感情状态：");
			if(marry.size()>0){
				user.setMarry(marry.first().nextElementSibling().text());
			}
			
			Elements onlineTimes = divElement.getElementsMatchingOwnText("累计在线时间长：");
			if(onlineTimes.size()>0){
				user.setOnlineTimes(onlineTimes.first().nextElementSibling().text());
			}
			
			Elements regTime = divElement.getElementsMatchingOwnText("注册时间：");
			if(regTime.size()>0){
				user.setRegTime(regTime.first().nextElementSibling().text());
			}
		}
		
		user.setUrl(task.getUrl());
		user.setId(task.getExtra());
		
		user.persistOnNotExist();
	}

}
