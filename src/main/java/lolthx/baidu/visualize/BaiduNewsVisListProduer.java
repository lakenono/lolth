package lolthx.baidu.visualize;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BaiduNewsVisListProduer extends Producer{

	private static final String BAIDU_NEWS_NUM_URL = 
			"http://news.baidu.com/ns?from=news&cl=2"
			+"&q1={0}"
			+ "&bt={1}&et={2}&pn={3}"
			+ "&q3=&q4=&mt=0&lm=&s=2&tn=news&ct=0&rn=20";

	
	private String city;
	private String keyword;
	private String start;
	private String end;
	
	public BaiduNewsVisListProduer(String projectName,String city,String keyword,String start,String end) {
		super(projectName);
		this.city = city;
		this.keyword = keyword;
		this.start = start;
		this.end = end;
	}

	@Override
	public String getQueueName() {
		return "baidu_news_vis_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(76));
		Element ele = document.select("p#page span.pc").last();
		if(ele == null){
			return 0;
		}else{
			String text = ele.text().trim();
			return Integer.valueOf(text);
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String str0 = URLEncoder.encode(keyword, "utf-8");
		
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(end + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });
		
		String str1 = String.valueOf(startDate.getTime() / 1000);
		
		String str2 = String.valueOf(endDate.getTime() / 1000);
		
		int str3 = (pageNum - 1) * 10;
				
		return MessageFormat.format(BAIDU_NEWS_NUM_URL, str0,str1,str2,str3);
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(city + ":" + keyword);
		return buildTask;
	}
	
	public static void main(String args[]) throws ParseException{
		String projectName = "媒体评价";
		String[] citys = {
				"安庆","安庆","安庆","安庆","安庆",
				"保定","保定","保定","保定","保定",
				"北京","北京","北京","北京","北京",
				"沧州","沧州","沧州","沧州","沧州",
				"常德","常德","常德","常德","常德",
				"常州","常州","常州","常州","常州",
				"成都","成都","成都","成都","成都",
				"大连","大连","大连","大连","大连",
				"德州","德州","德州","德州","德州",
				"东莞","东莞","东莞","东莞","东莞",
				"佛山","佛山","佛山","佛山","佛山",
				"福州","福州","福州","福州","福州",
				"赣州","赣州","赣州","赣州","赣州",
				"广州","广州","广州","广州","广州",
				"贵阳","贵阳","贵阳","贵阳","贵阳",
				"哈尔滨","哈尔滨","哈尔滨","哈尔滨","哈尔滨",
				"海口","海口","海口","海口","海口",
				"邯郸","邯郸","邯郸","邯郸","邯郸",
				"杭州","杭州","杭州","杭州","杭州",
				"合肥","合肥","合肥","合肥","合肥",
				"菏泽","菏泽","菏泽","菏泽","菏泽",
				"衡阳","衡阳","衡阳","衡阳","衡阳",
				"呼和浩特","呼和浩特","呼和浩特","呼和浩特","呼和浩特",
				"湖州","湖州","湖州","湖州","湖州",
				"淮安","淮安","淮安","淮安","淮安",
				"惠州","惠州","惠州","惠州","惠州",
				"吉安","吉安","吉安","吉安","吉安",
				"吉林","吉林","吉林","吉林","吉林",
				"济南","济南","济南","济南","济南",
				"济宁","济宁","济宁","济宁","济宁",
				"嘉兴","嘉兴","嘉兴","嘉兴","嘉兴",
				"江门","江门","江门","江门","江门",
				"焦作","焦作","焦作","焦作","焦作",
				"金华","金华","金华","金华","金华",
				"九江","九江","九江","九江","九江",
				"昆明","昆明","昆明","昆明","昆明",
				"拉萨","拉萨","拉萨","拉萨","拉萨",
				"兰州","兰州","兰州","兰州","兰州",
				"聊城","聊城","聊城","聊城","聊城",
				"临沂","临沂","临沂","临沂","临沂",
				"洛阳","洛阳","洛阳","洛阳","洛阳",
				"茂名","茂名","茂名","茂名","茂名",
				"南昌","南昌","南昌","南昌","南昌",
				"南京","南京","南京","南京","南京",
				"南宁","南宁","南宁","南宁","南宁",
				"南通","南通","南通","南通","南通",
				"南阳","南阳","南阳","南阳","南阳",
				"宁波","宁波","宁波","宁波","宁波",
				"青岛","青岛","青岛","青岛","青岛",
				"泉州","泉州","泉州","泉州","泉州",
				"厦门","厦门","厦门","厦门","厦门",
				"上海","上海","上海","上海","上海",
				"上饶","上饶","上饶","上饶","上饶",
				"绍兴","绍兴","绍兴","绍兴","绍兴",
				"深圳","深圳","深圳","深圳","深圳",
				"沈阳","沈阳","沈阳","沈阳","沈阳",
				"石家庄","石家庄","石家庄","石家庄","石家庄",
				"苏州","苏州","苏州","苏州","苏州",
				"台州","台州","台州","台州","台州",
				"太原","太原","太原","太原","太原",
				"泰安","泰安","泰安","泰安","泰安",
				"唐山","唐山","唐山","唐山","唐山",
				"天津","天津","天津","天津","天津",
				"威海","威海","威海","威海","威海",
				"潍坊","潍坊","潍坊","潍坊","潍坊",
				"渭南","渭南","渭南","渭南","渭南",
				"温州","温州","温州","温州","温州",
				"乌鲁木齐","乌鲁木齐","乌鲁木齐","乌鲁木齐","乌鲁木齐",
				"无锡","无锡","无锡","无锡","无锡",
				"武汉","武汉","武汉","武汉","武汉",
				"西安","西安","西安","西安","西安",
				"西宁","西宁","西宁","西宁","西宁",
				"咸阳","咸阳","咸阳","咸阳","咸阳",
				"襄阳","襄阳","襄阳","襄阳","襄阳",
				"新乡","新乡","新乡","新乡","新乡",
				"宿迁","宿迁","宿迁","宿迁","宿迁",
				"徐州","徐州","徐州","徐州","徐州",
				"烟台","烟台","烟台","烟台","烟台",
				"盐城","盐城","盐城","盐城","盐城",
				"扬州","扬州","扬州","扬州","扬州",
				"宜昌","宜昌","宜昌","宜昌","宜昌",
				"银川","银川","银川","银川","银川",
				"岳阳","岳阳","岳阳","岳阳","岳阳",
				"运城","运城","运城","运城","运城",
				"湛江","湛江","湛江","湛江","湛江",
				"漳州","漳州","漳州","漳州","漳州",
				"长春","长春","长春","长春","长春",
				"长沙","长沙","长沙","长沙","长沙",
				"镇江","镇江","镇江","镇江","镇江",
				"郑州","郑州","郑州","郑州","郑州",
				"重庆","重庆","重庆","重庆","重庆",
				"周口","周口","周口","周口","周口",
				"珠海","珠海","珠海","珠海","珠海",
				"淄博","淄博","淄博","淄博","淄博",
				"遵义","遵义","遵义","遵义","遵义",
				"桂林","桂林","桂林","桂林","桂林",
				"鞍山","鞍山","鞍山","鞍山","鞍山",
				"大庆","大庆","大庆","大庆","大庆",
				"泰州","泰州","泰州","泰州","泰州",
				"包头","包头","包头","包头","包头"
		};
		String[] keywords = {
				"搜狐 安庆","新浪 安庆","腾讯 安庆","凤凰 安庆","网易 安庆",
				"搜狐 保定","新浪 保定","腾讯 保定","凤凰 保定","网易 保定",
				"搜狐 北京","新浪 北京","腾讯 北京","凤凰 北京","网易 北京",
				"搜狐 沧州","新浪 沧州","腾讯 沧州","凤凰 沧州","网易 沧州",
				"搜狐 常德","新浪 常德","腾讯 常德","凤凰 常德","网易 常德",
				"搜狐 常州","新浪 常州","腾讯 常州","凤凰 常州","网易 常州",
				"搜狐 成都","新浪 成都","腾讯 成都","凤凰 成都","网易 成都",
				"搜狐 大连","新浪 大连","腾讯 大连","凤凰 大连","网易 大连",
				"搜狐 德州","新浪 德州","腾讯 德州","凤凰 德州","网易 德州",
				"搜狐 东莞","新浪 东莞","腾讯 东莞","凤凰 东莞","网易 东莞",
				"搜狐 佛山","新浪 佛山","腾讯 佛山","凤凰 佛山","网易 佛山",
				"搜狐 福州","新浪 福州","腾讯 福州","凤凰 福州","网易 福州",
				"搜狐 赣州","新浪 赣州","腾讯 赣州","凤凰 赣州","网易 赣州",
				"搜狐 广州","新浪 广州","腾讯 广州","凤凰 广州","网易 广州",
				"搜狐 贵阳","新浪 贵阳","腾讯 贵阳","凤凰 贵阳","网易 贵阳",
				"搜狐 哈尔滨","新浪 哈尔滨","腾讯 哈尔滨","凤凰 哈尔滨","网易 哈尔滨",
				"搜狐 海口","新浪 海口","腾讯 海口","凤凰 海口","网易 海口",
				"搜狐 邯郸","新浪 邯郸","腾讯 邯郸","凤凰 邯郸","网易 邯郸",
				"搜狐 杭州","新浪 杭州","腾讯 杭州","凤凰 杭州","网易 杭州",
				"搜狐 合肥","新浪 合肥","腾讯 合肥","凤凰 合肥","网易 合肥",
				"搜狐 菏泽","新浪 菏泽","腾讯 菏泽","凤凰 菏泽","网易 菏泽",
				"搜狐 衡阳","新浪 衡阳","腾讯 衡阳","凤凰 衡阳","网易 衡阳",
				"搜狐 呼和浩特","新浪 呼和浩特","腾讯 呼和浩特","凤凰 呼和浩特","网易 呼和浩特",
				"搜狐 湖州","新浪 湖州","腾讯 湖州","凤凰 湖州","网易 湖州",
				"搜狐 淮安","新浪 淮安","腾讯 淮安","凤凰 淮安","网易 淮安",
				"搜狐 惠州","新浪 惠州","腾讯 惠州","凤凰 惠州","网易 惠州",
				"搜狐 吉安","新浪 吉安","腾讯 吉安","凤凰 吉安","网易 吉安",
				"搜狐 吉林","新浪 吉林","腾讯 吉林","凤凰 吉林","网易 吉林",
				"搜狐 济南","新浪 济南","腾讯 济南","凤凰 济南","网易 济南",
				"搜狐 济宁","新浪 济宁","腾讯 济宁","凤凰 济宁","网易 济宁",
				"搜狐 嘉兴","新浪 嘉兴","腾讯 嘉兴","凤凰 嘉兴","网易 嘉兴",
				"搜狐 江门","新浪 江门","腾讯 江门","凤凰 江门","网易 江门",
				"搜狐 焦作","新浪 焦作","腾讯 焦作","凤凰 焦作","网易 焦作",
				"搜狐 金华","新浪 金华","腾讯 金华","凤凰 金华","网易 金华",
				"搜狐 九江","新浪 九江","腾讯 九江","凤凰 九江","网易 九江",
				"搜狐 昆明","新浪 昆明","腾讯 昆明","凤凰 昆明","网易 昆明",
				"搜狐 拉萨","新浪 拉萨","腾讯 拉萨","凤凰 拉萨","网易 拉萨",
				"搜狐 兰州","新浪 兰州","腾讯 兰州","凤凰 兰州","网易 兰州",
				"搜狐 聊城","新浪 聊城","腾讯 聊城","凤凰 聊城","网易 聊城",
				"搜狐 临沂","新浪 临沂","腾讯 临沂","凤凰 临沂","网易 临沂",
				"搜狐 洛阳","新浪 洛阳","腾讯 洛阳","凤凰 洛阳","网易 洛阳",
				"搜狐 茂名","新浪 茂名","腾讯 茂名","凤凰 茂名","网易 茂名",
				"搜狐 南昌","新浪 南昌","腾讯 南昌","凤凰 南昌","网易 南昌",
				"搜狐 南京","新浪 南京","腾讯 南京","凤凰 南京","网易 南京",
				"搜狐 南宁","新浪 南宁","腾讯 南宁","凤凰 南宁","网易 南宁",
				"搜狐 南通","新浪 南通","腾讯 南通","凤凰 南通","网易 南通",
				"搜狐 南阳","新浪 南阳","腾讯 南阳","凤凰 南阳","网易 南阳",
				"搜狐 宁波","新浪 宁波","腾讯 宁波","凤凰 宁波","网易 宁波",
				"搜狐 青岛","新浪 青岛","腾讯 青岛","凤凰 青岛","网易 青岛",
				"搜狐 泉州","新浪 泉州","腾讯 泉州","凤凰 泉州","网易 泉州",
				"搜狐 厦门","新浪 厦门","腾讯 厦门","凤凰 厦门","网易 厦门",
				"搜狐 上海","新浪 上海","腾讯 上海","凤凰 上海","网易 上海",
				"搜狐 上饶","新浪 上饶","腾讯 上饶","凤凰 上饶","网易 上饶",
				"搜狐 绍兴","新浪 绍兴","腾讯 绍兴","凤凰 绍兴","网易 绍兴",
				"搜狐 深圳","新浪 深圳","腾讯 深圳","凤凰 深圳","网易 深圳",
				"搜狐 沈阳","新浪 沈阳","腾讯 沈阳","凤凰 沈阳","网易 沈阳",
				"搜狐 石家庄","新浪 石家庄","腾讯 石家庄","凤凰 石家庄","网易 石家庄",
				"搜狐 苏州","新浪 苏州","腾讯 苏州","凤凰 苏州","网易 苏州",
				"搜狐 台州","新浪 台州","腾讯 台州","凤凰 台州","网易 台州",
				"搜狐 太原","新浪 太原","腾讯 太原","凤凰 太原","网易 太原",
				"搜狐 泰安","新浪 泰安","腾讯 泰安","凤凰 泰安","网易 泰安",
				"搜狐 唐山","新浪 唐山","腾讯 唐山","凤凰 唐山","网易 唐山",
				"搜狐 天津","新浪 天津","腾讯 天津","凤凰 天津","网易 天津",
				"搜狐 威海","新浪 威海","腾讯 威海","凤凰 威海","网易 威海",
				"搜狐 潍坊","新浪 潍坊","腾讯 潍坊","凤凰 潍坊","网易 潍坊",
				"搜狐 渭南","新浪 渭南","腾讯 渭南","凤凰 渭南","网易 渭南",
				"搜狐 温州","新浪 温州","腾讯 温州","凤凰 温州","网易 温州",
				"搜狐 乌鲁木齐","新浪 乌鲁木齐","腾讯 乌鲁木齐","凤凰 乌鲁木齐","网易 乌鲁木齐",
				"搜狐 无锡","新浪 无锡","腾讯 无锡","凤凰 无锡","网易 无锡",
				"搜狐 武汉","新浪 武汉","腾讯 武汉","凤凰 武汉","网易 武汉",
				"搜狐 西安","新浪 西安","腾讯 西安","凤凰 西安","网易 西安",
				"搜狐 西宁","新浪 西宁","腾讯 西宁","凤凰 西宁","网易 西宁",
				"搜狐 咸阳","新浪 咸阳","腾讯 咸阳","凤凰 咸阳","网易 咸阳",
				"搜狐 襄阳","新浪 襄阳","腾讯 襄阳","凤凰 襄阳","网易 襄阳",
				"搜狐 新乡","新浪 新乡","腾讯 新乡","凤凰 新乡","网易 新乡",
				"搜狐 宿迁","新浪 宿迁","腾讯 宿迁","凤凰 宿迁","网易 宿迁",
				"搜狐 徐州","新浪 徐州","腾讯 徐州","凤凰 徐州","网易 徐州",
				"搜狐 烟台","新浪 烟台","腾讯 烟台","凤凰 烟台","网易 烟台",
				"搜狐 盐城","新浪 盐城","腾讯 盐城","凤凰 盐城","网易 盐城",
				"搜狐 扬州","新浪 扬州","腾讯 扬州","凤凰 扬州","网易 扬州",
				"搜狐 宜昌","新浪 宜昌","腾讯 宜昌","凤凰 宜昌","网易 宜昌",
				"搜狐 银川","新浪 银川","腾讯 银川","凤凰 银川","网易 银川",
				"搜狐 岳阳","新浪 岳阳","腾讯 岳阳","凤凰 岳阳","网易 岳阳",
				"搜狐 运城","新浪 运城","腾讯 运城","凤凰 运城","网易 运城",
				"搜狐 湛江","新浪 湛江","腾讯 湛江","凤凰 湛江","网易 湛江",
				"搜狐 漳州","新浪 漳州","腾讯 漳州","凤凰 漳州","网易 漳州",
				"搜狐 长春","新浪 长春","腾讯 长春","凤凰 长春","网易 长春",
				"搜狐 长沙","新浪 长沙","腾讯 长沙","凤凰 长沙","网易 长沙",
				"搜狐 镇江","新浪 镇江","腾讯 镇江","凤凰 镇江","网易 镇江",
				"搜狐 郑州","新浪 郑州","腾讯 郑州","凤凰 郑州","网易 郑州",
				"搜狐 重庆","新浪 重庆","腾讯 重庆","凤凰 重庆","网易 重庆",
				"搜狐 周口","新浪 周口","腾讯 周口","凤凰 周口","网易 周口",
				"搜狐 珠海","新浪 珠海","腾讯 珠海","凤凰 珠海","网易 珠海",
				"搜狐 淄博","新浪 淄博","腾讯 淄博","凤凰 淄博","网易 淄博",
				"搜狐 遵义","新浪 遵义","腾讯 遵义","凤凰 遵义","网易 遵义",
				"搜狐 桂林","新浪 桂林","腾讯 桂林","凤凰 桂林","网易 桂林",
				"搜狐 鞍山","新浪 鞍山","腾讯 鞍山","凤凰 鞍山","网易 鞍山",
				"搜狐 大庆","新浪 大庆","腾讯 大庆","凤凰 大庆","网易 大庆",
				"搜狐 泰州","新浪 泰州","腾讯 泰州","凤凰 泰州","网易 泰州",
				"搜狐 包头","新浪 包头","腾讯 包头","凤凰 包头","网易 包头"
		};
		
		String start = "20140831";
		String end = "20150831";
		
		for(int i = 0 ; i< citys.length; i++){
			new BaiduNewsVisListProduer(projectName,citys[i],keywords[i], start, end).run();
		}
		
	}
	
	
}
