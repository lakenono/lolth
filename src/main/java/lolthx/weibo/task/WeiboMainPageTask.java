package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.weibo.bean.WeiboBean;
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
				if (maxPage > 13) {
					maxPage = 13;
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
				"zq1190",
				"zhoulijun",
				"zhengfawei1113",
				"zhangtiemian",
				"yzliupeng",
				"yllhf",
				"yichemall",
				"yankaixi",
				"xieshengzhang",
				"xiaohe510185",
				"wuhufabu",
				"whzjgz",
				"wendymakeup",
				"wcwcwc",
				"wangxu1124",
				"wangcaijun",
				"wanche100",
				"tonglintony",
				"tiantianzhitongche",
				"tangshanqiche",
				"sunshinezhe",
				"suhanginter",
				"stroman",
				"smeqd",
				"slshenbianshi",
				"simpleclone",
				"shixiongbujie",
				"sengekanche",
				"rui1976",
				"renhedao",
				"quyifan618",
				"qorosauto",
				"qinaidexiangni",
				"qianlai521",
				"qcshzk",
				"percysong",
				"pengyongping",
				"ok1255",
				"nmgxvgang",
				"nanyangzhongda",
				"naauto",
				"mrroc",
				"movieview",
				"meixinmeifeiws",
				"meilishawu",
				"maxconan",
				"matias2008",
				"liaoshengyuwu",
				"leiiliu",
				"kuntse",
				"joyce787",
				"jojomagi",
				"joannaplace",
				"jiaoguikun",
				"jackielaukill",
				"itomjojo",
				"Innovisiongroup",
				"huimaiche",
				"huazhong001",
				"guanxuejun",
				"greatpassion",
				"gaody7758",
				"fuliqq",
				"frankjin23",
				"fenxicdian",
				"fenghuangshipin",
				"feilongyes",
				"f121",
				"dy1078",
				"duanxucd",
				"doralai",
				"dengxicheng",
				"dengcm",
				"dczhao",
				"dclife",
				"dangdaigetan",
				"dalianguanzhi",
				"dahecc",
				"csauto",
				"crowneplazachangshu",
				"cqwbzh",
				"comilly",
				"comcctv",
				"christianmandy",
				"chewenkuangbiao",
				"chanwanji",
				"changshu",
				"cdsbauto",
				"cdfkx",
				"cddduck",
				"cautw",
				"carvpchina",
				"capitamalljinniu",
				"bitauto01",
				"benbend",
				"bdcbellydancechina",
				"autonews",
				"amoymushroom",
				"amazonbzchina",
				"airforceworld",
				"92coupe",
				"1991dyx",
				"14520akjie",
				"12gang",
				"5701975466",
				"5687740949",
				"5687391614",
				"5687390129",
				"5687388022",
				"5687385509",
				"5687385388",
				"5687148214",
				"5687130005",
				"5687121329",
				"5687120919",
				"5687119731",
				"5687119232",
				"5687119071",
				"5686764637",
				"5686761786",
				"5686760072",
				"5685810898",
				"5685787607",
				"5684438092",
				"5684382137",
				"5684081649",
				"5684080502",
				"5683415520",
				"5683413504",
				"5682776237",
				"5682663895",
				"5681456558",
				"5681184621",
				"5681034750",
				"5676943446",
				"5676060419",
				"5675436472",
				"5674427092",
				"5674097781",
				"5671415193",
				"5671175033",
				"5670023608",
				"5669927793",
				"5669903089",
				"5669879617",
				"5669859590",
				"5669604876",
				"5669604708",
				"5669541759",
				"5669344894",
				"5669331908",
				"5669321493",
				"5669104986",
				"5669089115",
				"5669003965",
				"5668957287",
				"5668946368",
				"5668903305",
				"5668892979",
				"5668891112",
				"5668889495",
				"5668451542",
				"5668407368",
				"5668369976",
				"5668248762",
				"5668186076",
				"5668176697",
				"5668130571",
				"5668047307",
				"5667881462",
				"5667846329",
				"5667834604",
				"5667830607",
				"5667827733",
				"5667584521",
				"5667568983",
				"5667559225",
				"5667493259",
				"5667480633",
				"5667455774",
				"5667443115",
				"5667283261",
				"5667210943",
				"5667181401",
				"5667138606",
				"5667098648",
				"5667094325",
				"5667080484",
				"5666773223",
				"5666767374",
				"5666753230",
				"5666749147",
				"5666734708",
				"5661969175",
				"5661115245",
				"5659677216",
				"5659621249",
				"5659552278",
				"5659375202",
				"5659297255",
				"5658759012",
				"5658445608",
				"5658286919",
				"5657769944",
				"5657345364",
				"5657185799",
				"5657155014",
				"5656900423",
				"5656171604",
				"5655387577",
				"5653519045",
				"5653344614",
				"5651160783",
				"5648376609",
				"5645207259",
				"5644417152",
				"5644021612",
				"5643495412",
				"5640104238",
				"5638688291",
				"5635522471",
				"5634212958",
				"5631523573",
				"5628978354",
				"5620114489",
				"5591540494",
				"5583919517",
				"5579271075",
				"5561460832",
				"5557718697",
				"5556022702",
				"5553060478",
				"5542164755",
				"5518096675",
				"5484986398",
				"5478018227",
				"5470383911",
				"5470367703",
				"5463403912",
				"5447036267",
				"5430482653",
				"5400313151",
				"5399454171",
				"5387158961",
				"5365803390",
				"5351783334",
				"5350685183",
				"5334460004",
				"5330222893",
				"5283633045",
				"5253086922",
				"5251644136",
				"5248832874",
				"5171765434",
				"5152325086",
				"5147892395",
				"5120233391",
				"5112237156",
				"5105119704",
				"5096491691",
				"5092746089",
				"5060985885",
				"5044898855",
				"5043868610",
				"5043809677",
				"5041496079",
				"5037933183",
				"5037339928",
				"5035621186",
				"5033552228",
				"5032466862",
				"5031954781",
				"5030046887",
				"5027586247",
				"5027122238",
				"5025517146",
				"5025448291",
				"5023524896",
				"5023025500",
				"5022578678",
				"5021396082",
				"5018662611",
				"5018195684",
				"5014685607",
				"3995455520",
				"3994314053",
				"3951685796",
				"3946136153",
				"3942069274",
				"3939552218",
				"3934744863",
				"3910397982",
				"3907870008",
				"3903203718",
				"3895429128",
				"3885346065",
				"3836512103",
				"3835391999",
				"3717165131",
				"3699164642",
				"3669204273",
				"3608524472",
				"3540580602",
				"3496579602",
				"3484299385",
				"3461576634",
				"3440862820",
				"3440727542",
				"3358752442",
				"3307473571",
				"3304473290",
				"3280893561",
				"3270516914",
				"3258733332",
				"3243909505",
				"3228566922",
				"3224881647",
				"3221044100",
				"3215152483",
				"3212875851",
				"3211788287",
				"3211242815",
				"3194500341",
				"3193981343",
				"3172761302",
				"3172570792",
				"3170588262",
				"3169540664",
				"3145802014",
				"3145635232",
				"3136453795",
				"3128267477",
				"3122259824",
				"3118579867",
				"3110165811",
				"3088001561",
				"3080685315",
				"3075746213",
				"3053261603",
				"3046205927",
				"3045569575",
				"3036432807",
				"3027141480",
				"3025868182",
				"3022803717",
				"3015159971",
				"3013004847",
				"2987309635",
				"2981158081",
				"2968918361",
				"2926406397",
				"2914249511",
				"2910568877",
				"2910566185",
				"2905889587",
				"2898701735",
				"2898495527",
				"2897922565",
				"2893827077",
				"2892341167",
				"2890851427",
				"2890148283",
				"2889400355",
				"2889114155",
				"2887292067",
				"2886704967",
				"2885670885",
				"2885524791",
				"2885436531",
				"2882351563",
				"2882075595",
				"2878968003",
				"2877890281",
				"2860410500",
				"2852341475",
				"2842486650",
				"2829314760",
				"2825119310",
				"2822751762",
				"2821746223",
				"2805676274",
				"2796615333",
				"2795431875",
				"2794299723",
				"2793442190",
				"2793139895",
				"2775439735",
				"2766166433",
				"2759849373",
				"2758113951",
				"2757806097",
				"2755957181",
				"2752673801",
				"2751344365",
				"2750499771",
				"2748918821",
				"2743243547",
				"2738599377",
				"2736236595",
				"2735316090",
				"2725978201",
				"2721038034",
				"2720935941",
				"2719419483",
				"2716208330",
				"2713584057",
				"2709798685",
				"2704755173",
				"2703177211",
				"2700833960",
				"2695109517",
				"2693954931",
				"2693107065",
				"2691917505",
				"2691497595",
				"2686233823",
				"2683017431",
				"2672578104",
				"2669652623",
				"2662310943",
				"2657118730",
				"2656807393",
				"2653216164",
				"2652033453",
				"2650407401",
				"2646941593",
				"2645478351",
				"2641718315",
				"2640023153",
				"2629847461",
				"2617168820",
				"2537735764",
				"2526731317",
				"2521984123",
				"2514453593",
				"2511282282",
				"2507680335",
				"2503445642",
				"2492126435",
				"2488988121",
				"2477047497",
				"2472661891",
				"2442077597",
				"2425643890",
				"2390821271",
				"2382977971",
				"2382262682",
				"2368651053",
				"2354821341",
				"2350663883",
				"2336968165",
				"2311915502",
				"2311465910",
				"2295535983",
				"2290770410",
				"2247941911",
				"2230417750",
				"2217229023",
				"2208376302",
				"2203041810",
				"2200254014",
				"2195581205",
				"2191870420",
				"2182600682",
				"2179103573",
				"2174762157",
				"2141176674",
				"2138793162",
				"2134863101",
				"2134591122",
				"2133979911",
				"2120659173",
				"2120554273",
				"2118245174",
				"2117865915",
				"2117250772",
				"2111860750",
				"2108818375",
				"2103179913",
				"2101574267",
				"2101298563",
				"2097172710",
				"2094719281",
				"2092903430",
				"2077954577",
				"2061453993",
				"2056608110",
				"2055927427",
				"2053417877",
				"2052762061",
				"2041036150",
				"2030092141",
				"2022632377",
				"2020198333",
				"2007584295",
				"2001483007",
				"1998955602",
				"1990525883",
				"1974575187",
				"1971663173",
				"1967644631",
				"1950224922",
				"1925979481",
				"1923951613",
				"1912897243",
				"1902950464",
				"1897017884",
				"1885722502",
				"1884245222",
				"1879521550",
				"1876463057",
				"1866839295",
				"1852983467",
				"1850613092",
				"1844532077",
				"1842795511",
				"1842279081",
				"1836371062",
				"1835080772",
				"1820670930",
				"1818495951",
				"1818457851",
				"1815770825",
				"1814771774",
				"1811527455",
				"1809302275",
				"1807647905",
				"1802943961",
				"1798156975",
				"1788233257",
				"1782483305",
				"1780589362",
				"1771828975",
				"1761168453",
				"1749847584",
				"1748653507",
				"1745310841",
				"1735546403",
				"1732669644",
				"1685954337",
				"1685933771",
				"1678573723",
				"1677993564",
				"1675764957",
				"1670635982",
				"1652399124",
				"1646357542",
				"1642464213",
				"1607605252",
				"1572918213",
				"1530563884",
				"1463747987",
				"1459119295",
				"1456704732",
				"1452146421",
				"1439348062",
				"1429404480",
				"1428866642",
				"1427636732",
				"1403250837",
				"1365446774",
				"1357825744",
				"1348163721",
				"1297157577",
				"1266715004",
				"1253158483",
				"1251813820",
				"1241022965",
				"1216206610",
				"1152943507",
				"1149113114",
				"1146011710",
				"1135753130",
				"1133594372",
				"1114426803",
				"1060663540",
				"1017835661",
				"1007592783",
				"1003342007",
				"717176888",
				"706786281",
				"639978991",
				"601266190",
				"535676740",
				"534275677",
				"534109123",
				"521444123",
				"521432234",
				"470480993",
				"345201069",
				"333870922",
				"278204441",
				"237897919",
				"236780654",
				"234036095",
				"228280826",
				"207571555",
				"52966601",
				"37904174",
				"27903816"
		};
		String projectName = "guanzhi_Relaunch";
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
