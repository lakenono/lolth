package lolthx.yoka.cosmetics;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.yoka.cosmetics.bean.YokaCosmeticBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class YokaCosmeticResolveFetch  extends DistributedParser{
	
	@Override
	public String getQueueName() {
		return "yoka_cosmetic_resolve";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result);
		
		String keywordRegex = "data = '\\[(.*?)\\]'";
		
		Pattern p = Pattern.compile(keywordRegex);
		Matcher m = p.matcher(result.toString());
		
		String compositeScore = "";
		String yearScore = "";
		String skinScore ="";
		
		String[] extras = task.getExtra().split(":");
		String id = extras[0];
		String keyword = extras[1];
		String projectName = task.getProjectName();
		String url = task.getUrl();
		
		int i = 1;
		while (m.find()) {
			try {
				String key = m.group(1);
				if( i == 1){
					compositeScore = key.substring(1,key.length()-1); 
				}
				if( i == 2){
					yearScore = key.substring(1,key.length()-1); 
				}
				if( i == 3){
					skinScore = key.substring(1,key.length()-1); 
					break;
				}
				i++;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle yoka cosmetic reslove error : {}",e.getMessage(),e);
				continue;
			}
		}
		
		String star_1 = "";
		String star_2 = "";
		String star_3 = "";
		String star_4 = "";
		String star_5 = "";
		
		String twentyFivePraise = "";//25岁以下好评率
		String twentyFivePeople = "";//25岁以下好评率 
		String thirtyPraise = "";//26-30岁好评率
		String thirtyPeople = "";//26-30岁好评率
		String thirtyFivePraise = "";//31-35岁好评率
		String thirtyFivePeople = "";//31-35岁好评率
		String thirtySixUpPraise = "";//36岁以上好评率
		String thirtySixUpPeople = "";//36岁以上好评率

		String neutralPraise = "";//中性皮肤
		String neutralPeople = "";//中性皮肤
		String mixPraise = "" ;//混合性
		String mixPeople = "" ;//混合性
		String oilinessPraise = "";//油性
		String oilinessPeople = "";//油性
		String drynessPraise = "";//干性
		String drynessPeople = "";//干性
		String irritabilityPraise = "";//过敏性
		String irritabilityPeople = "";//过敏性
		String sensibilityPraise = "";//先天过敏性
		String sensibilityPeople = "";//先天过敏性
		
		String[] stars = compositeScore.split("\\],\\[");
		String[] oldPraises = yearScore.split("\\},\\{");
		String[] skinScores = skinScore.split("\\},\\{");
		
		YokaCosmeticBean cosmeticBean;
		try {
			if(stars.length == 5){
				star_1 = stars[0].split(",")[1];
				star_2 = stars[1].split(",")[1];
				star_3 = stars[2].split(",")[1];
				star_4 = stars[3].split(",")[1];
				star_5 = stars[4].split(",")[1];
			}
			
			if(oldPraises.length == 4){
				twentyFivePraise =  oldPraises[0].split(",")[0].split(":")[1];
				twentyFivePeople= oldPraises[0].split(",")[1].split(":")[1];
				thirtyPraise=  oldPraises[1].split(",")[0].split(":")[1];
				thirtyPeople  =  oldPraises[1].split(",")[1].split(":")[1];
				thirtyFivePraise =  oldPraises[2].split(",")[0].split(":")[1];
				thirtyFivePeople =  oldPraises[2].split(",")[1].split(":")[1];
				thirtySixUpPraise =  oldPraises[3].split(",")[0].split(":")[1];
				thirtySixUpPeople=  oldPraises[3].split(",")[1].split(":")[1];
			}
			
			if(skinScores.length == 6){
				neutralPraise= skinScores[0].split(",")[0].split(":")[1];//中性皮肤
				neutralPeople= skinScores[0].split(",")[1].split(":")[1];//中性皮肤
				mixPraise = skinScores[1].split(",")[0].split(":")[1];//混合性
				mixPeople= skinScores[1].split(",")[1].split(":")[1];//混合性
				oilinessPraise = skinScores[2].split(",")[0].split(":")[1];//油性
				oilinessPeople = skinScores[2].split(",")[1].split(":")[1];//油性
				drynessPraise = skinScores[3].split(",")[0].split(":")[1];//干性
				drynessPeople= skinScores[3].split(",")[1].split(":")[1];//干性
				irritabilityPraise = skinScores[4].split(",")[0].split(":")[1];//过敏性
				irritabilityPeople = skinScores[4].split(",")[1].split(":")[1];//过敏性
				sensibilityPraise = skinScores[5].split(",")[0].split(":")[1];//先天过敏性
				sensibilityPeople = skinScores[5].split(",")[1].split(":")[1];//先天过敏性
			}
			
			String allocationScheme = doc.select("div#mark-box div.mark.mark1.on dl dd ul").text();
			String avgscore = doc.select("div#mark-box div.txt span").text();
			String numPeStr = doc.select("div#mark-box div.txt p").text();
			String numberPeople = StringUtils.substringBefore(numPeStr, "人评分");
			String publicPraise = doc.select("div#cp-iwom-select dl#user-tags dd").text();
			
			cosmeticBean = new YokaCosmeticBean();
			cosmeticBean.setId(id);;
			cosmeticBean.setProjectName(projectName);;
			cosmeticBean.setKeyword(keyword);
			cosmeticBean.setUrl(url);
			cosmeticBean.setStar_1(star_1);
			cosmeticBean.setStar_2(star_2);
			cosmeticBean.setStar_3(star_3);
			cosmeticBean.setStar_4(star_4);
			cosmeticBean.setStar_5(star_5);
			cosmeticBean.setAllocationScheme(allocationScheme);
			cosmeticBean.setAvgscore(avgscore);
			cosmeticBean.setNumberPeople(numberPeople);
			cosmeticBean.setTwentyFivePeople(twentyFivePeople);
			cosmeticBean.setTwentyFivePraise(twentyFivePraise);
			cosmeticBean.setThirtyPeople(thirtyPeople);
			cosmeticBean.setThirtyPraise(thirtyPraise);
			cosmeticBean.setThirtyFivePeople(thirtyFivePeople);
			cosmeticBean.setThirtyFivePraise(thirtyFivePraise);
			cosmeticBean.setThirtySixUpPeople(thirtySixUpPeople);
			cosmeticBean.setThirtySixUpPraise(thirtySixUpPraise);
			cosmeticBean.setNeutralPeople(neutralPeople);
			cosmeticBean.setNeutralPraise(neutralPraise);
			cosmeticBean.setMixPeople(mixPeople);
			cosmeticBean.setMixPraise(mixPraise);
			cosmeticBean.setOilinessPeople(oilinessPeople);
			cosmeticBean.setOilinessPraise(oilinessPraise);
			cosmeticBean.setDrynessPeople(drynessPeople);
			cosmeticBean.setDrynessPraise(drynessPraise);
			cosmeticBean.setIrritabilityPeople(irritabilityPeople);
			cosmeticBean.setIrritabilityPraise(irritabilityPraise);
			cosmeticBean.setSensibilityPeople(sensibilityPeople);
			cosmeticBean.setSensibilityPraise(sensibilityPraise);
			cosmeticBean.setPublicPraise(publicPraise);
			
			cosmeticBean.saveOnNotExist();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String numsText = doc.select("dl#cp-nav dt a.on").text();
		String nums = StringUtils.substringBetween(numsText, "（","）");
		
		System.out.println( ">>>>>>>>>>> numsText " + numsText);
		System.out.println( ">>>>>>>>>>> nums   " + nums);
		
		if(nums != null && !nums.equals("") && !nums.equals("0") ){
			int pages = Integer.valueOf(nums) / 10 + 1;
			String buildUrl = url.replace("_1.htm", "_{0}.htm");
			if(pages > 0){
				for(int pageNum= 1 ; pageNum <= pages ; pageNum++){
					String sendUrl = this.buildUrl(buildUrl, pageNum);
					Task newTask = buildTask(sendUrl, "yoka_cosmetic_list", task);
					Queue.push(newTask);
				}
			}
		}
		
	}
	
	public String buildUrl(String url , int pageNum){
		return MessageFormat.format(url, String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url, String queueName, Task perTask) {
		Task task =  super.buildTask(url, queueName, perTask);
		task.setStartDate(perTask.getStartDate());
		task.setEndDate(perTask.getEndDate());
		return task;
	}
	
	public static void main(String[] args){
		new YokaCosmeticResolveFetch().run();
	}
	

}
