package lolthx.weibo.task;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lakenono.core.GlobalComponents;
import lolthx.weibo.bean.WeiboCity;
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
	public final String baseURL = "http://weibo.cn/{0}/profile?hasori=0&haspic=0&starttime={1}&endtime={2}&advancedfilter=1&page={3}";

	public int getWeiboList(String id, String startTime, String endTime, int page_num) throws Exception {
		String url = buildUrl(id, startTime, endTime, page_num);
		Document doc = fetchUrl(url);

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
				return 10 * (maxPage-1);
			}
			return 10 * (maxPage-1) + eles.size();
		}
	}

	private Document fetchUrl(String url) throws TException, InterruptedException {
		log.debug("fetch url is:{}",url);
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
				"遵义	遵义公安	2101559864",
				"遵义	遵义发布	5521606430",
				"遵义	贵州遵义旅游	5120482264",
				"淄博	淄博市旅游局官方微博	2115308053",
				"淄博	淄博警方	2052120021",
				"淄博	淄博发布	3577849617",
				"珠海	珠海市文化体育旅游局	2627015690",
				"珠海	珠海公安	1709350393",
				"珠海	珠海发布	3535175740",
				"周口	周口市旅游管理部门	3230414023",
				"周口	周口发布	5208527486",
				"周口	平安周口	1997271745",
				"重庆 	重庆微发布	cqszfxwb",
				"重庆 	重庆市旅游局	2128914281",
				"重庆 	重庆交巡警	1921066184",
				"中山	中山旅游局	3679627014",
				"中山	中山发布	3511698607",
				"中山	平安中山	1719916573",
				"郑州	郑州旅游局	2608234633",
				"郑州	郑州发布	2434139193",
				"郑州	平安郑州	2055648745",
				"镇江	镇江旅游	2052012810",
				"镇江	镇江发布	3708395841",
				"镇江	平安镇江	1919892452",
				"长沙	长沙市旅游局	2031569683",
				"长沙	长沙警事	1973743580",
				"长沙	长沙发布	3329420380",
				"长春	长春市旅游局	2798034700",
				"长春	长春公安	3259204927",
				"长春	长春发布	3270346463",
				"漳州	漳州外宣办	2693819864",
				"漳州	漳州市旅游局	2179810663",
				"漳州	漳州公安	2732804400",
				"湛江	湛江旅游	2951567912",
				"湛江	湛江公安	1735790851",
				"湛江	湛江发布	3980043846",
				"运城	运城发布	2748065513",
				"运城	山西运城旅游	3939073194",
				"运城	平安运城	2038016581",
				"岳阳	岳阳市政府门户网站	1925992111",
				"岳阳	岳阳市旅游局官方	2259650670",
				"岳阳	岳阳公安警事	2113401774",
				"银川	银川体育旅游	1934057840",
				"银川	微博银川	1898782627",
				"银川	平安银川	1978054071",
				"宜昌	宜昌市旅游局官方微博	2140270927",
				"宜昌	宜昌发布	1999404300",
				"宜昌	平安宜昌	2113506964",
				"扬州	扬州旅游局	2043380211",
				"扬州	扬州公安	1790414785",
				"扬州	扬州发布	2571494540",
				"盐城	盐城旅游	3163784122",
				"盐城	盐城发布	5477333519",
				"盐城	平安盐城	1910370220",
				"烟台	烟台市旅游局官方微博	1971878703",
				"烟台	烟台市公安局	1797983553",
				"烟台	烟台发布	3883727101",
				"徐州	徐州市旅游局	2118696790",
				"徐州	徐州发布	3514316985",
				"徐州	平安徐州	1990533882",
				"宿迁	宿迁之声	2282670150",
				"宿迁	宿迁警方	1918676611",
				"宿迁	畅游宿迁	1608184211",
				"新乡	新乡旅游微博	3213968477",
				"新乡	新乡警方在线	2050025717",
				"新乡	新乡发布	5290464539",
				"襄阳	中国襄阳政府网	2508844792",
				"襄阳	襄阳发布	2636293023",
				"襄阳	文明襄阳	2645198757",
				"咸阳	中国咸阳门户网站	2875067720",
				"咸阳	咸阳文物旅游局微博	2300108824",
				"咸阳	咸阳公安	2597907162",
				"西宁	夏都西宁旅游	3513452155",
				"西宁	夏都西宁	3881282682",
				"西宁	西宁网警	1708468357",
				"西安	西安市旅游局	2061858797",
				"西安	西安公安	1903747781",
				"西安	西安发布	3757167087",
				"武汉	武汉市旅游局	2140196951",
				"武汉	武汉发布	2759348142",
				"武汉	平安武汉	2418542712",
				"无锡	无锡市旅游局	1980124632",
				"无锡	无锡发布	2696049583",
				"无锡	平安无锡	1808109392",
				"乌鲁木齐	乌鲁木齐市旅游局	2698527412",
				"乌鲁木齐	乌鲁木齐市公安局	2598170481",
				"乌鲁木齐	乌鲁木齐发布	2629250581",
				"温州	温州旅游官方微博	1836051934",
				"温州	温州发布	3909815234",
				"温州	平安温州	1663933725",
				"渭南	渭南市旅游局	5117407139",
				"渭南	渭南发布	3214268410",
				"渭南	陕西渭南公安	2425003975",
				"潍坊	潍坊市旅游局	weifangtravel",
				"潍坊	潍坊公安	2616360197",
				"潍坊	潍坊发布	3315784023",
				"威海	威海市旅游局官方微博	2115352863",
				"威海	威海警方在线	1631659573",
				"威海	威海发布	3798503424",
				"天津 	天津市旅游信息咨询中心	2062579281"
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
			while (start.before(end)) {
				String startTime = format.format(start.getTime());
				Date step = DateUtils.addDays(start.getTime(), 14);
				if (step.after(end.getTime())) {
					step = end.getTime();
				}
				String endTime = format.format(step);
				int num = weibo.getWeiboList(split[2], startTime, endTime, 1);
				sum += num;
				start.add(Calendar.DAY_OF_MONTH, 15);
			}
			// 对象存储
			WeiboCity bean = new WeiboCity();
			bean.setCity(split[0]);
			bean.setWeiboUser(split[1]);
			bean.setNumbers(sum);
			bean.saveOnNotExist();
			// 时间重新设置
			start.setTime(format.parse("20140831"));
		}
	}
}
