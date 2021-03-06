package lolthx.baidu.webpage;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import lakenono.base.Producer;
import lakenono.base.Task;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class BaiduWebpageListProducer extends Producer {


	private static final String BAIDU_WEB_PAGE_URL = 
				"https://www.baidu.com/s"+
				"?wd={0}"+
				"&pn=750"+
				"&ie=utf-8"+
				"&gpc=stf%3D{1}%2C{2}|stftype%3D2"+
				"&tfflag=1";

	private String keyword;
	private String start;
	private String end;
	private String city;
	
	public BaiduWebpageListProducer(String projectName,String city,String keyword,String start,String end) {
		super(projectName);
		this.city = city;
		this.keyword = keyword;
		this.start = start;
		this.end = end;
		
	}

	@Override
	public String getQueueName() {
		return "baidu_web_page_list";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(start + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });
		Calendar cal = Calendar.getInstance();

		String str0 = URLEncoder.encode(keyword, "utf-8");
		
		String str1 = String.valueOf(startDate.getTime() / 1000);
		
		String str2 = String.valueOf(endDate.getTime() / 1000);

		return MessageFormat.format(BAIDU_WEB_PAGE_URL, str0 , str1 , str2  );
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(city + ":" + keyword +  ":" + start);
		return buildTask;
	}
	
	public static void main(String args[]) throws Exception {
		String projectName = "城市口号";
		String[] citys = {
				"兰州","吉林","鞍山","安庆","赣州","桂林",
				"菏泽","九江","上饶","江门","珠海",
				"茂名","湛江","新乡","焦作","周口",
				"咸阳","宿迁","运城","漳州","遵义",
				"泉州","潍坊","盐城","济宁","临沂",
				"镇江","宜昌","襄阳",
				"上海","天津","北京","重庆","哈尔滨"
				,"长春","沈阳","大连","石家庄","邯郸",
				"郑州","洛阳","南阳","太原","济南",
				"青岛","烟台","淄博","东营","威海",
				"聊城","泰安","武汉","长沙","昆明",
				"贵阳","成都","南宁","西安","呼和浩特",
				"银川","拉萨","南京","苏州","扬州",
				"无锡","南通","徐州","泰州","常州",
				"杭州","绍兴","宁波","温州","台州",
				"嘉兴","金华","福州","厦门","广州",
				"深圳","中山","佛山","东莞","海口",
				"南昌","合肥","包头"	
				
				};
		String[] keywords = {
				"黄河之都、金城兰州",
				"吉祥行、四季行、吉林市行","山泉之城、佛玉之都","和谐安庆，魅力怡城","美丽老家，幸福赣州","桂林山水甲天下",
				"牡丹之都、好汉之乡","欲识庐山真面目，请到江西九江来","中国经典山水、世界遗产名城","侨乡山水风情","浪漫之城、中国珠海",
				"新滨海、新茂名","相约在中国大陆最南端","山水太行，风采新乡","焦作山水、人间仙境","羲皇古都、老子故乡",
				"中国金字塔之都","宿迁有三宝，英雄美酒生态好","华夏之根，诚信之邦，大运之城","水仙花的故乡","转折之城、会议之都",
				"光明之城---泉州","放飞梦想，逍遥潍坊","东方湿地、鹤鹿故乡","孔孟之乡、运河之都、文化济宁","山水沂蒙、多彩临沂",
				"镇江，一座美得让你吃醋的城市","金色三峡银色大坝绿色宜昌","千古帝乡，智慧襄阳",
				"上海---精彩每一天","天天乐道，津津有味","东方古都、长城故乡","重庆，非去不可","冷酷冰城---哈尔滨",
				"北国春城，绿色都市","新沈阳，新环境","浪漫之都。中国大连","燕赵古韵，魅力之城---石家庄","游名城邯郸，品古赵文化",
				"黄河之都","国花牡丹城---洛阳","四圣故里---南里","唐风晋韵，锦绣龙城","趵突声韵甲天下 济南潇洒胜江南",
				"帆船之都---青岛","人间仙境，梦幻烟台","陶瓷名城---中国淄博","黄河大海，相约东营","拥抱碧海蓝天，体验渔家风情",
				"江北水城---中国聊城","中华泰山，天下泰安","高山流水，白云黄鹤","多情长沙，快乐之都","天天是春天",
				"森林之城，魅力贵阳","成功之都，多彩之都，美食之都","奇山秀水绿南宁","龙在中国，根在西安","美丽青城---中国呼和浩特",
				"塞上明珠，中国银川","雪域圣地，高原明珠","博爱之都、绿色古都、文化之城","园林之都，天堂苏州","烟花扬州",
				"太湖明珠，中国无锡","近代历史名城，江海休闲港湾","楚汉雄风，豪情徐州","文昌水秀，祥泰之州","中华龙城，江南常州",
				"东方休闲之都、品质生活之城","梦幻水乡，人文绍兴","书藏古今，港通天下","时尚之都，山水温州","神奇台州、生态之旅",
				"水都绿城、休闲嘉兴","风水金华，购物天堂","八闽古都，有福之州","海上花园、温馨厦门","一日读懂两千年",
				"每天给你带来新的希望","伟人故里，锦绣中山","狮舞岭南，传奇佛山","每天绽放新精彩","阳光海口，娱乐之都",
				"中国水都---南昌","日出黄山，绿然合肥","包容大气，勇立潮头"
				};

		String start = "20140831";
		String end = "20150831";
		for (int i = 0; i < keywords.length; i++) {
			Date startDate = DateUtils.parseDate(start, new String[] { "yyyyMMdd" });
			Date endDate = DateUtils.parseDate(end, new String[] { "yyyyMMdd" });
			while (true){
				new BaiduWebpageListProducer(projectName, citys[i],keywords[i], DateFormatUtils.format(startDate, "yyyyMMdd"), DateFormatUtils.format(startDate, "yyyyMMdd")).run();
				if (DateUtils.isSameDay(startDate, endDate)){
					break;
				}
				startDate = DateUtils.addDays(startDate, 1);
			}
		}
	}
}
