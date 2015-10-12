package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboBean;
import lolthx.weibo.bean.WeiboUserBean;
import lolthx.weibo.bean.WeiboUserConcernRefBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 微博用户主页任务，最多获取前13页，抓取前100条数据
 * 注意：在创建任务的时候，需根据projectName创建表，projectName为英文
 * @author yanghp
 *
 */
@Slf4j
public class WeiboMainPageTask extends Producer {

	public static final String MAIN_PAGE_QUEUE = "weibo_main_page";
	private final String USER_MAIN_PAGE_URL_TEMPLATE = "http://weibo.cn/{0}?page={1}";
	private String user;
	private String projectName;

	public WeiboMainPageTask(String user, String projectName) {
		super(projectName);
		this.user = user;
		this.projectName = projectName;
	}

	@Override
	public String getQueueName() {
		return MAIN_PAGE_QUEUE;
	}

	@Override
	protected int parse() throws Exception {
		String url = buildUrl(1);
		try {
			String cookies = GlobalComponents.authService
					.getCookies("weibo.cn");
			// String cookies =
			// "_T_WM=381052f5df15a47db4b6c216d9fa6b8e; SUB=_2A254qy2qDeSRGeNL7FQS9inIyj-IHXVYV7PirDV6PUJbrdANLVPhkW1Mx5Pwf3qtPcXl9Bixn6Md_eO72Q..; gsid_CTandWM=4uDre42b1a7eMv2kMnqKPnoFp6F";
			String page_html = GlobalComponents.jsoupFetcher
					.fetch(url, cookies,"");
			Document doc = Jsoup.parse(page_html);
			// Thread.sleep(15000);

			if (doc.select("div#pagelist").size() == 0) {
				Elements elements = doc.select("div.c[id]");
				if (elements.isEmpty()) {
					return 0;
				}
				return 1;
			} else {
				String html = doc.select("div#pagelist").first().text();
				String page = StringUtils.substringBetween(html, "/", "页");
				int maxPage = Integer.parseInt(page);
				//最大页数
				if (maxPage > 100) {
					maxPage = 100;
				}
				return maxPage;
			}
		} catch (Exception e) {
			log.error("{} get maxPage error : ", url, e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(USER_MAIN_PAGE_URL_TEMPLATE, user,
				String.valueOf(pageNum));
	}

	@Override
	public Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(MAIN_PAGE_QUEUE);
		task.setUrl(url);
		task.setExtra(user);
		return task;
	}
	
	public static void main(String[] args) throws Exception {
		String[] users = {
				"2101559864",
				"5521606430",
				"5120482264",
				"2115308053",
				"2052120021",
				"3577849617",
				"2627015690",
				"1709350393",
				"3535175740",
				"3230414023",
				"5208527486",
				"1997271745",
				"cqszfxwb",
				"2128914281",
				"1921066184",
				"3679627014",
				"3511698607",
				"1719916573",
				"2608234633",
				"2434139193",
				"2055648745",
				"2052012810",
				"3708395841",
				"1919892452",
				"2031569683",
				"1973743580",
				"3329420380",
				"2798034700",
				"3259204927",
				"3270346463",
				"2693819864",
				"2179810663",
				"2732804400",
				"2951567912",
				"1735790851",
				"3980043846",
				"2748065513",
				"3939073194",
				"2038016581",
				"1925992111",
				"2259650670",
				"2113401774",
				"1934057840",
				"1898782627",
				"1978054071",
				"2140270927",
				"1999404300",
				"2113506964",
				"2043380211",
				"1790414785",
				"2571494540",
				"3163784122",
				"5477333519",
				"1910370220",
				"1971878703",
				"1797983553",
				"3883727101",
				"2118696790",
				"3514316985",
				"1990533882",
				"2282670150",
				"1918676611",
				"1608184211",
				"3213968477",
				"2050025717",
				"5290464539",
				"2508844792",
				"2636293023",
				"2645198757",
				"2875067720",
				"2300108824",
				"2597907162",
				"3513452155",
				"3881282682",
				"1708468357",
				"2061858797",
				"1903747781",
				"3757167087",
				"2140196951",
				"2759348142",
				"2418542712",
				"1980124632",
				"2696049583",
				"1808109392",
				"2698527412",
				"2598170481",
				"2629250581",
				"1836051934",
				"3909815234",
				"1663933725",
				"5117407139",
				"3214268410",
				"2425003975",
				"weifangtravel",
				"2616360197",
				"3315784023",
				"2115352863",
				"1631659573",
				"3798503424",
				"2062579281",
				"2489610225",
				"3163782211",
				"3802120928",
				"1266164102",
				"2730232527",
				"2106635273",
				"3195538854",
				"1913938912",
				"2116029945",
				"1920014771",
				"5603075231",
				"3268152967",
				"1045423751",
				"2304521142",
				"1803818020",
				"5200439644",
				"2571243254",
				"1806686502",
				"2998045524",
				"2516150701",
				"1617499760",
				"2941157707",
				"1819621657",
				"2760402625",
				"3551974050",
				"2892786960",
				"2852776612",
				"1735882701",
				"2171939562",
				"shaoxingpolice",
				"5209171085",
				"1920422372",
				"2663559543",
				"3509300853",
				"2539961154",
				"2014034660",
				"2493592183",
				"1690015640",
				"1778455640",
				"5517516948",
				"2645886160",
				"1944194624",
				"1652889677",
				"2115980191",
				"2722031203",
				"2239082677",
				"nbtravel",
				"2588125414",
				"2568435544",
				"1990533543",
				"2496611222",
				"5299321460",
				"1805087952",
				"3106250394",
				"1971263622",
				"1988749421",
				"3928761901",
				"1972917825",
				"2146717162",
				"2097024354",
				"2171958520",
				"2673596511",
				"2418432711",
				"1713419250",
				"3583504072",
				"3649418972",
				"2115262352",
				"2043228245",
				"1936959151",
				"2024833771",
				"2637132697",
				"3895563329",
				"2006265001",
				"2116051461",
				"3768357411",
				"1668285003",
				"2626472787",
				"5089748379",
				"3960905543",
				"2858745682",
				"1801989063",
				"3816699409",
				"1930138130",
				"2310271061",
				"3270666330",
				"2282395370",
				"1967953043",
				"5219022556",
				"2176281491",
				"1938657241",
				"5344250889",
				"5167878023",
				"5186246734",
				"1736165940",
				"1932694202",
				"2197452570",
				"5279514376",
				"2116015565",
				"2092312995",
				"3317202677",
				"3221854141",
				"2115973751",
				"1702549133",
				"2724371683",
				"2959366295",
				"3319940460",
				"1905799770",
				"2472572320",
				"2801324472",
				"2679652763",
				"1735902494",
				"2823995382",
				"2560987592",
				"3629325807",
				"3582010354",
				"2389015477",
				"1935265547",
				"3514408660",
				"5367684019",
				"2128065487",
				"1604466704",
				"2142660111",
				"5242383134",
				"1417678504",
				"2116057491",
				"2183864363",
				"3632295651",
				"2513443800",
				"2403752844",
				"2662494703",
				"1789342195",
				"5211979483",
				"2619153927",
				"2060952187",
				"1808545011",
				"1917587477",
				"HKGA",
				"5623185245",
				"1887683497",
				"2275846603",
				"2753006425",
				"2641501554",
				"1989772524",
				"2645846213",
				"2473528064",
				"2101553952",
				"2605594314",
				"2532579261",
				"1722022490",
				"2174819272",
				"3919694626",
				"3817381137",
				"2171536211",
				"1925694254",
				"2639029341",
				"1699377733",
				"2804925804",
				"2949913010",
				"2142059811",
				"1924024735",
				"2052350073",
				"2115998401",
				"1997551475",
				"1608195440",
				"1719711161",
				"2030371897",
				"2072735371",
				"2116048875",
				"2074235423",
				"2485817127",
				"5000611793",
				"3292938153",
				"2306025041",
				"1713436155",
				"2814046595",
				"1760668437",
				"2206820037",
				"2164623652",
				"1523766213",
				"2616693703",
				"1796308821",
				"2634517493",
				"2497233690",
				"2177262002",
				"2057911392",
				"2711828411",
				"2636873692",
				"1812423571",
				"1288915263",
				"1936009361",
				"2418724427",
				"2592946742",
				"1812456955",
				"2779486914",
				"2337581421",
				"2168046575",
				"2425752947",
				"2076130575",
				"2549578244",
				"1960078897",
				"2638330734",
				"2874978364"
		};
		String projectName = "country";
		//创建表
//		DBBean.createTable(WeiboBean.class, projectName);
//		DBBean.createTable(WeiboUserBean.class, projectName);
//		DBBean.createTable(WeiboUserConcernRefBean.class, projectName);
		for (String user : users) {
			WeiboMainPageTask wb = new WeiboMainPageTask(user, projectName);
			wb.run();
			Thread.sleep(15000);
		}
	}
}
