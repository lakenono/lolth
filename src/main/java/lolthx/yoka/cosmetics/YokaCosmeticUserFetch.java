package lolthx.yoka.cosmetics;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.yoka.cosmetics.bean.YokaCosmeticUserlBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class YokaCosmeticUserFetch  extends DistributedParser {
	
	
	
	@Override
	public String getQueueName() {
		return "yoka_cosmetic_user";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		Document doc = Jsoup.parse(result); 
		
		Elements trs = doc.select("div.box1rg div.box1rg1 table tbody ");
		String authorId = StringUtils.substringAfter(task.getUrl(), "cosmetics/");
		String url = task.getUrl();
		String text = trs.text();
		String authorName =  StringUtils.substringBetween(text, "用户昵称：", " 用户年龄");
		String age = StringUtils.substringBetween(text, "用户年龄：", "岁");
		String skin = StringUtils.substringBetween(text, "TA的皮肤： ", " TA的发质");
		String hair = StringUtils.substringBetween(text, "TA的发质：", " 使用心得");
		String experience = StringUtils.substringBetween(text, "使用心得： 共", "篇");
		String essence = StringUtils.substringBetween(text, "篇，", "条精华");
		String bagClassify = StringUtils.substringBetween(text, "化妆包： 共", "个分类");
		String product = StringUtils.substringBetween(text, "分类，共", "款产品");
		String brand = StringUtils.substringBetween(text, "加入品牌： ", "个");
		
		YokaCosmeticUserlBean bean = new YokaCosmeticUserlBean();
		bean.setAuthorId(authorId);
		bean.setAuthorName(authorName);
		bean.setUrl(url);
		bean.setAge(age);
		bean.setSkin(skin);
		bean.setHair(hair);
		bean.setExperience(experience);
		bean.setEssence(essence);
		bean.setBagClassify(bagClassify);
		bean.setProduct(product);
		bean.setBrand(brand);
		
		bean.saveOnNotExist();
	
	}

	
	public static void main(String[] args){
		for( int i = 1 ; i <= 10 ; i++){
			new YokaCosmeticUserFetch().run();
		}

	}
	
	
}
