package lolthx.yoka.cosmetics;

import java.text.MessageFormat;
import java.text.ParseException;

import lakenono.base.Producer;
import lakenono.base.Task;

import org.apache.commons.lang3.time.DateUtils;

public class YokaCosmeticProducer extends Producer{
	
	private static final String YOKA_COSMETIC_LIST_URL = "http://brand.yoka.com/cosmetics/skii/productcomment{0}_0_0_-1_10_{1}.htm";
	
	private String id;
	private String keyword;

	public YokaCosmeticProducer(String projectName,String id, String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "yoka_cosmetic_resolve";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(YOKA_COSMETIC_LIST_URL, id, String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("20140831", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20150831", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args){
		String projectName = "yoka cosmetic test";
		String[] ids = { "1743" , "359" , "8563" , "37846" , "40158" , "189" , 
				"22069"} ;
		String[] keywords = { "SK-II护肤精华露" , "露得清深层柔珠洗面乳" , "李医生吸黑头面膜" , "曼秀雷敦男士冰爽活炭洁面乳" , "旁氏清透净白系列清透净白洁面乳" , "可伶可俐毛细孔清透洁面乳" , 
				"资生堂矿物泥去黑头粉刺面膜"};
		
		for(int i = 0 ; i < ids.length ; i++){
			new YokaCosmeticProducer(projectName,ids[i],keywords[i]).run();
		}
		
	}
	
}
