package lolthx.baidu.webpage;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaiduSpotsListProducer  extends Producer {
	
	private String BAIDU_SPOTS_LIST = "https://www.baidu.com/s?ie=utf-8&f=8&wd={0}&pn={1}&usm=6";
	private String city;
	
	public BaiduSpotsListProducer(String projectName,String city) {
		super(projectName);
		this.city = city;
	}

	@Override
	public String getQueueName() {
		return "baidu_spots_list";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String str0 = URLEncoder.encode(city, "utf-8");
		return MessageFormat.format(BAIDU_SPOTS_LIST, 	str0, 	pageNum*10 );
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(city);
		return buildTask;
	}
	
	public static void main(String args[]) throws Exception{
		String projectName = "联想符号";
		String[] citys = {
				"北京","天津","上海","重庆","石家庄"
				,"郑州","武汉","长沙","南京","南昌",
				"沈阳","长春","哈尔滨","西安","太原",
				"成都","西宁","合肥","海口","广州",
				"贵阳","杭州","福州","台北","兰州",
				"昆明","拉萨","银川","南宁","乌鲁木齐",
				"呼和浩特","香港","澳门",
				"安庆","保定","沧州","常德","常州",
				"大连","德州","东莞","佛山","赣州",
				"邯郸","菏泽","衡阳","湖州","淮安",
				"惠州","吉安","吉林","济南","济宁",
				"嘉兴","江门","焦作","金华","九江",
				"聊城","临沂","洛阳","茂名","南通",
				"南阳","宁波","青岛","泉州","厦门",
				"上饶","绍兴","深圳","苏州","台州",
				"泰安","唐山","威海","潍坊","渭南",
				"温州","无锡","咸阳","襄阳","新乡",
				"宿迁","徐州","烟台","盐城","扬州",
				"宜昌","岳阳","运城","湛江","漳州",
				"镇江","周口","珠海","淄博","遵义",
				"桂林","鞍山","大庆","泰州","包头"
		};
		
		for(int i = 0 ; i < citys.length ; i++){
			new BaiduSpotsListProducer(projectName, citys[i]).run();
		}
	}
}
