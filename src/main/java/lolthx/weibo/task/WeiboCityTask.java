package lolthx.weibo.task;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lakenono.core.GlobalComponents;
import lolthx.weibo.bean.WeiboCity;
import lolthx.weibo.fetch.WeiboSearchFetch;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboCityTask {
	// projectName name id
	public WeiboSearchFetch wei = new WeiboSearchFetch();
	public final String baseURL = "https://weibo.cn/{0}/profile?hasori=0&haspic=0&starttime={1}&endtime={2}&advancedfilter=1&page={3}";
	public static String fans = "0";

	public int getWeiboList(String id, String startTime, String endTime, int page_num) throws Exception {
		if (!StringUtils.isNumeric(id)) {
			id = wei.getUid(id);
		}
		String url = buildUrl(id, startTime, endTime, page_num);
		Document doc = fetchUrl(url);
		if (StringUtils.equals(fans, "0")) {
			String text = doc.select("div.tip2").text();
			if (StringUtils.isNotBlank(text)) {
				fans = StringUtils.substringBetween(text, "丝[", "]");
			}
		}
		if (doc.select("div#pagelist").size() == 0) {
			Elements elements = doc.select("div.c[id]");
			if (null == elements && elements.isEmpty()) {
				return 0;
			}
			return elements.size();
		} else {
			String page = doc.select("div#pagelist").first().text();
			String nums = StringUtils.substringBetween(page, "/", "页");
			int maxPage = Integer.parseInt(nums);
			Document document = fetchUrl(buildUrl(id, startTime, endTime, maxPage));
			Elements eles = document.select("div.c[id]");
			if (null == eles && eles.isEmpty()) {
				return 10 * (maxPage - 1);
			}
			return 10 * (maxPage - 1) + eles.size();
		}
	}

	private Document fetchUrl(String url) throws TException, InterruptedException {
		log.debug("fetch url is:{}", url);
		String cookies = GlobalComponents.authService.getCookies("weibo.cn");
		String html = GlobalComponents.jsoupFetcher.fetch(url, cookies, "");
		Thread.sleep(15000);
		return Jsoup.parse(html);
	}

	protected String buildUrl(String id, String startTime, String endTime, int pageNum) throws Exception {
		return MessageFormat.format(baseURL, id, startTime, endTime, pageNum);
	}

	public static void main(String[] args) throws Exception {
		String[] tasks = {
				"济南	微博济南	3221854141",
				"济南	济南市旅游局微博	2115973751",
				"济南	济南公安	1702549133",
				"吉林	山水吉林雾凇江城	2724371683",
				"吉林	吉林市公安局	2959366295",
				"吉林	吉林市发布	3319940460",
				"吉安	吉安市旅游	1905799770",
				"吉安	吉安公安	2472572320",
				"吉安	吉安发布	2801324472",
				"惠州	最美惠州	2679652763",
				"惠州	平安惠州	1735902494",
				"惠州	惠州发布	2823995382",
				"淮安	平安淮安	2560987592",
				"淮安	淮安旅游	3629325807",
				"淮安	淮安发布	3582010354",
				"湖州	悠游湖州	2389015477",
				"湖州	平安湖州	1935265547",
				"湖州	湖州发布	3514408660",
				"呼和浩特	青城旅游发布	5367684019",
				"呼和浩特	呼和浩特微博	2128065487",
				"呼和浩特	呼和浩特交警	1604466704",
				"衡阳	衡阳市公安局	2142660111",
				"衡阳	衡阳-旅游	5242383134",
				"衡阳	衡阳发布	1417678504",
				"菏泽	菏泽市旅游局官方微博	2116057491",
				"菏泽	菏泽公安	2183864363",
				"菏泽	菏泽发布	3632295651",
				"合肥	合肥警方 	2513443800",
				"合肥	合肥发布	2403752844",
				"杭州	平安杭州	2662494703",
				"杭州	杭州市旅游委员会	1789342195",
				"杭州	杭州发布	5211979483",
				"邯郸	聚焦邯郸	2619153927",
				"邯郸	邯郸市旅游局	2060952187",
				"邯郸	邯郸公安网络发言人	1808545011",
				"海口	海口旅游咨询服务中心	1917587477",
				"海口	海口公安	HKGA",
				"海口	海口发布	5623185245",
				"哈尔滨	平安哈尔滨	1887683497",
				"哈尔滨	哈尔滨市旅游局	2275846603",
				"哈尔滨	哈尔滨发布	2753006425",
				"桂林	平安桂林	2641501554",
				"桂林	桂林市旅游发展委员会	1989772524",
				"贵阳	微博贵阳	2645846213",
				"贵阳	贵阳旅游官方微博	2473528064",
				"贵阳	贵阳公安	2101553952",
				"广州	中国广州发布	2605594314",
				"广州	广州旅游	2532579261",
				"广州	广州公安	1722022490",
				"赣州	江西赣州旅游	2174819272",
				"赣州	赣州公安	3919694626",
				"赣州	赣州发布	3817381137",
				"福州	福州市旅游局	2171536211",
				"福州	福州市公安局	1925694254",
				"福州	福州发布	2639029341",
				"佛山	公安主持人	1699377733",
				"佛山	佛山-旅游	2804925804",
				"佛山	佛山发布	2949913010",
				"鄂尔多斯	鄂尔多斯旅游局	2142059811",
				"鄂尔多斯	鄂尔多斯公安	1924024735",
				"鄂尔多斯	鄂尔多斯发布	2052350073",
				"东营	东营市旅游局官方微博	2115998401",
				"东营	东营公安	1997551475",
				"东营	东营发布	1608195440",
				"东莞	平安东莞	1719711161",
				"东莞	莞香花开	2030371897",
				"东莞	东莞旅游局	2072735371",
				"德州	德州市旅游局官方微博	2116048875",
				"德州	德州公安	2074235423",
				"德州	德州发布	2485817127",
				"大庆	中国大庆发布	5000611793",
				"大庆	平安大庆	3292938153",
				"大庆	大庆市旅游局	2306025041",
				"大连	文明大连	1713436155",
				"大连	大连市旅游局	2814046595",
				"大连	大连公安	1760668437",
				"成都	平安成都	2206820037",
				"成都	成都旅游微博	2164623652",
				"成都	成都发布	1523766213",
				"常州	微常州	2616693703",
				"常州	平安常州	1796308821",
				"常州	常州旅游	2634517493",
				"常德	常德市人民政府	2497233690",
				"常德	常德市旅游外侨局	2177262002",
				"常德	常德市交通警察支队	2057911392",
				"沧州	微博沧州	2711828411",
				"沧州	沧州市旅游局	2636873692",
				"沧州	沧州公安网络发言人	1812423571",
				"北京 	平安北京	1288915263",
				"北京 	北京市旅游发展委员会  	1936009361",
				"北京 	北京发布 	2418724427",
				"保定	保定市旅游局	2592946742",
				"保定	保定公安网络发言人	1812456955",
				"保定	保定发布	2779486914",
				"包头	平安包头	2337581421",
				"包头	包头旅游官方资讯	2168046575",
				"包头	包头发布	2425752947",
				"鞍山	平安鞍山	2076130575",
				"鞍山	鞍山发布	2549578244",
				"安庆	安庆旅游局官方微博	1960078897",
				"安庆	安庆公安在线	2638330734",
				"安庆	安庆发布	287497836"
		};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			start.setTime(format.parse("20140831"));
			end.setTime(format.parse("20150831"));
		} catch (ParseException e) {
			log.error("parse time is error:{}", e.getMessage());
		}
		WeiboCityTask weibo = new WeiboCityTask();
		for (String task : tasks) {
			String[] split = StringUtils.splitByWholeSeparator(task, null);
			if (split.length != 3) {
				log.error("task is error {}", task);
				continue;
			}
			int sum = 0;
			fans = "0";
			while (start.before(end)) {
				String startTime = format.format(start.getTime());
				Date step = DateUtils.addDays(start.getTime(), 14);
				if (step.after(end.getTime())) {
					step = end.getTime();
				}
				String endTime = format.format(step);
				try {
					int num = weibo.getWeiboList(split[2], startTime, endTime, 1);
					sum += num;
				} catch (Exception e) {
					log.error("fetch is error {}", e.getMessage());
				}
				start.add(Calendar.DAY_OF_MONTH, 15);
			}
			// 对象存储
			WeiboCity bean = new WeiboCity();
			bean.setCity(split[0]);
			bean.setWeiboUser(split[1]);
			bean.setNumbers(sum);
			bean.setFans(fans);
			bean.saveOnNotExist();
			// 时间重新设置
			start.setTime(format.parse("20140831"));
		}
	}
}
