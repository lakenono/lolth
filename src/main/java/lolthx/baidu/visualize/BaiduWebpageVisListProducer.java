package lolthx.baidu.visualize;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BaiduWebpageVisListProducer extends Producer {

	private static final String BAIDU_WEBPAGE_VIS_URL = "https://www.baidu.com/s"+
			"?wd={0}"+
			"&pn=750"+
			"&ie=utf-8"+
			"&gpc=stf%3D{1}%2C{2}|stftype%3D2"+
			"&tfflag=1";
	
	
	private String city;
	private String keyword;
	private String start;
	private String end;
	
	public BaiduWebpageVisListProducer(String projectName,String city,String keyword,String start,String end) {
		super(projectName);
		this.city = city;
		this.keyword = keyword;
		this.start = start;
		this.end = end;
	}


	@Override
	public String getQueueName() {
		return "baidu_webpage_vis_first";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String str0 = URLEncoder.encode(keyword, "utf-8");
		
		int str1 = (pageNum - 1) * 10;
		
		Date startDate = DateUtils.parseDate(start + " 00:00:00", new String[] { "yyyyMMdd HH:mm:ss" });
		Date endDate = DateUtils.parseDate(end + " 23:59:59", new String[] { "yyyyMMdd HH:mm:ss" });
		
		String str2 = String.valueOf(startDate.getTime() / 1000);
		
		String str3 = String.valueOf(endDate.getTime() / 1000);
		
		return MessageFormat.format(BAIDU_WEBPAGE_VIS_URL, str0);
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(city + ":" + keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("20140831", "yyyyMMdd"));
			buildTask.setEndDate(DateUtils.parseDate("20150831", "yyyyMMdd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args){
		String projectName = "城市文明";
		String[] citys={
				"安庆"	,	"安庆"	,
				"保定"	,	"保定"	,
				"北京"	,	"北京"	,
				"沧州"	,	"沧州"	,
				"常德"	,	"常德"	,
				"常州"	,	"常州"	,
				"成都"	,	"成都"	,
				"大连"	,	"大连"	,
				"德州"	,	"德州"	,
				"东莞"	,	"东莞"	,
				"佛山"	,	"佛山"	,
				"福州"	,	"福州"	,
				"赣州"	,	"赣州"	,
				"广州"	,	"广州"	,
				"贵阳"	,	"贵阳"	,
				"哈尔滨"	,	"哈尔滨"	,
				"海口"	,	"海口"	,
				"邯郸"	,	"邯郸"	,
				"杭州"	,	"杭州"	,
				"合肥"	,	"合肥"	,
				"菏泽"	,	"菏泽"	,
				"衡阳"	,	"衡阳"	,
				"呼和浩特"	,	"呼和浩特"	,
				"湖州"	,	"湖州"	,
				"淮安"	,	"淮安"	,
				"惠州"	,	"惠州"	,
				"吉安"	,	"吉安"	,
				"吉林"	,	"吉林"	,
				"济南"	,	"济南"	,
				"济宁"	,	"济宁"	,
				"嘉兴"	,	"嘉兴"	,
				"江门"	,	"江门"	,
				"焦作"	,	"焦作"	,
				"金华"	,	"金华"	,
				"九江"	,	"九江"	,
				"昆明"	,	"昆明"	,
				"拉萨"	,	"拉萨"	,
				"兰州"	,	"兰州"	,
				"聊城"	,	"聊城"	,
				"临沂"	,	"临沂"	,
				"洛阳"	,	"洛阳"	,
				"茂名"	,	"茂名"	,
				"南昌"	,	"南昌"	,
				"南京"	,	"南京"	,
				"南宁"	,	"南宁"	,
				"南通"	,	"南通"	,
				"南阳"	,	"南阳"	,
				"宁波"	,	"宁波"	,
				"青岛"	,	"青岛"	,
				"泉州"	,	"泉州"	,
				"厦门"	,	"厦门"	,
				"上海"	,	"上海"	,
				"上饶"	,	"上饶"	,
				"绍兴"	,	"绍兴"	,
				"深圳"	,	"深圳"	,
				"沈阳"	,	"沈阳"	,
				"石家庄"	,	"石家庄"	,
				"苏州"	,	"苏州"	,
				"台州"	,	"台州"	,
				"太原"	,	"太原"	,
				"泰安"	,	"泰安"	,
				"唐山"	,	"唐山"	,
				"天津"	,	"天津"	,
				"威海"	,	"威海"	,
				"潍坊"	,	"潍坊"	,
				"渭南"	,	"渭南"	,
				"温州"	,	"温州"	,
				"乌鲁木齐"	,	"乌鲁木齐"	,
				"无锡"	,	"无锡"	,
				"武汉"	,	"武汉"	,
				"西安"	,	"西安"	,
				"西宁"	,	"西宁"	,
				"咸阳"	,	"咸阳"	,
				"襄阳"	,	"襄阳"	,
				"新乡"	,	"新乡"	,
				"宿迁"	,	"宿迁"	,
				"徐州"	,	"徐州"	,
				"烟台"	,	"烟台"	,
				"盐城"	,	"盐城"	,
				"扬州"	,	"扬州"	,
				"宜昌"	,	"宜昌"	,
				"银川"	,	"银川"	,
				"岳阳"	,	"岳阳"	,
				"运城"	,	"运城"	,
				"湛江"	,	"湛江"	,
				"漳州"	,	"漳州"	,
				"长春"	,	"长春"	,
				"长沙"	,	"长沙"	,
				"镇江"	,	"镇江"	,
				"郑州"	,	"郑州"	,
				"重庆"	,	"重庆"	,
				"周口"	,	"周口"	,
				"珠海"	,	"珠海"	,
				"淄博"	,	"淄博"	,
				"遵义"	,	"遵义"	,
				"桂林"	,	"桂林"	,
				"鞍山"	,	"鞍山"	,
				"大庆"	,	"大庆"	,
				"泰州"	,	"泰州"	,
				"包头"	,	"包头"	
};
		String[] keywords={
				"安庆卫生"	,	"安庆文明"	,
				"保定卫生"	,	"保定文明"	,
				"北京卫生"	,	"北京文明"	,
				"沧州卫生"	,	"沧州文明"	,
				"常德卫生"	,	"常德文明"	,
				"常州卫生"	,	"常州文明"	,
				"成都卫生"	,	"成都文明"	,
				"大连卫生"	,	"大连文明"	,
				"德州卫生"	,	"德州文明"	,
				"东莞卫生"	,	"东莞文明"	,
				"佛山卫生"	,	"佛山文明"	,
				"福州卫生"	,	"福州文明"	,
				"赣州卫生"	,	"赣州文明"	,
				"广州卫生"	,	"广州文明"	,
				"贵阳卫生"	,	"贵阳文明"	,
				"哈尔滨卫生"	,	"哈尔滨文明"	,
				"海口卫生"	,	"海口文明"	,
				"邯郸卫生"	,	"邯郸文明"	,
				"杭州卫生"	,	"杭州文明"	,
				"合肥卫生"	,	"合肥文明"	,
				"菏泽卫生"	,	"菏泽文明"	,
				"衡阳卫生"	,	"衡阳文明"	,
				"呼和浩特卫生"	,	"呼和浩特文明"	,
				"湖州卫生"	,	"湖州文明"	,
				"淮安卫生"	,	"淮安文明"	,
				"惠州卫生"	,	"惠州文明"	,
				"吉安卫生"	,	"吉安文明"	,
				"吉林卫生"	,	"吉林文明"	,
				"济南卫生"	,	"济南文明"	,
				"济宁卫生"	,	"济宁文明"	,
				"嘉兴卫生"	,	"嘉兴文明"	,
				"江门卫生"	,	"江门文明"	,
				"焦作卫生"	,	"焦作文明"	,
				"金华卫生"	,	"金华文明"	,
				"九江卫生"	,	"九江文明"	,
				"昆明卫生"	,	"昆明文明"	,
				"拉萨卫生"	,	"拉萨文明"	,
				"兰州卫生"	,	"兰州文明"	,
				"聊城卫生"	,	"聊城文明"	,
				"临沂卫生"	,	"临沂文明"	,
				"洛阳卫生"	,	"洛阳文明"	,
				"茂名卫生"	,	"茂名文明"	,
				"南昌卫生"	,	"南昌文明"	,
				"南京卫生"	,	"南京文明"	,
				"南宁卫生"	,	"南宁文明"	,
				"南通卫生"	,	"南通文明"	,
				"南阳卫生"	,	"南阳文明"	,
				"宁波卫生"	,	"宁波文明"	,
				"青岛卫生"	,	"青岛文明"	,
				"泉州卫生"	,	"泉州文明"	,
				"厦门卫生"	,	"厦门文明"	,
				"上海卫生"	,	"上海文明"	,
				"上饶卫生"	,	"上饶文明"	,
				"绍兴卫生"	,	"绍兴文明"	,
				"深圳卫生"	,	"深圳文明"	,
				"沈阳卫生"	,	"沈阳文明"	,
				"石家庄卫生"	,	"石家庄文明"	,
				"苏州卫生"	,	"苏州文明"	,
				"台州卫生"	,	"台州文明"	,
				"太原卫生"	,	"太原文明"	,
				"泰安卫生"	,	"泰安文明"	,
				"唐山卫生"	,	"唐山文明"	,
				"天津卫生"	,	"天津文明"	,
				"威海卫生"	,	"威海文明"	,
				"潍坊卫生"	,	"潍坊文明"	,
				"渭南卫生"	,	"渭南文明"	,
				"温州卫生"	,	"温州文明"	,
				"乌鲁木齐卫生"	,	"乌鲁木齐文明"	,
				"无锡卫生"	,	"无锡文明"	,
				"武汉卫生"	,	"武汉文明"	,
				"西安卫生"	,	"西安文明"	,
				"西宁卫生"	,	"西宁文明"	,
				"咸阳卫生"	,	"咸阳文明"	,
				"襄阳卫生"	,	"襄阳文明"	,
				"新乡卫生"	,	"新乡文明"	,
				"宿迁卫生"	,	"宿迁文明"	,
				"徐州卫生"	,	"徐州文明"	,
				"烟台卫生"	,	"烟台文明"	,
				"盐城卫生"	,	"盐城文明"	,
				"扬州卫生"	,	"扬州文明"	,
				"宜昌卫生"	,	"宜昌文明"	,
				"银川卫生"	,	"银川文明"	,
				"岳阳卫生"	,	"岳阳文明"	,
				"运城卫生"	,	"运城文明"	,
				"湛江卫生"	,	"湛江文明"	,
				"漳州卫生"	,	"漳州文明"	,
				"长春卫生"	,	"长春文明"	,
				"长沙卫生"	,	"长沙文明"	,
				"镇江卫生"	,	"镇江文明"	,
				"郑州卫生"	,	"郑州文明"	,
				"重庆卫生"	,	"重庆文明"	,
				"周口卫生"	,	"周口文明"	,
				"珠海卫生"	,	"珠海文明"	,
				"淄博卫生"	,	"淄博文明"	,
				"遵义卫生"	,	"遵义文明"	,
				"桂林卫生"	,	"桂林文明"	,
				"鞍山卫生"	,	"鞍山文明"	,
				"大庆卫生"	,	"大庆文明"	,
				"泰州卫生"	,	"泰州文明"	,
				"包头卫生"	,	"包头文明"	
		};
		
		String start = "20140831";
		String end = "20150831";
		
		for(int i = 0 ; i< citys.length; i++){
			new BaiduWebpageVisListProducer(projectName,citys[i],keywords[i], start, end).run();
		}
	}
}
