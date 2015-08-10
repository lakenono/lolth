package lolthx.weibo.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 微博用户主页任务，最多获取前3页
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
					.fetch(url, cookies);
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
		String[] users = {"1609067013",
				"1653720374",
				"1632160814",
				"1295693333",
				"1456971382",
				"1595816377",
				"1656109761",
				"1747780592",
				"1643240267",
				"1735601680",
				"1429745634",
				"1732767407",
				"1367508905",
				"1240574087",
				"1768441743",
				"1529566465",
				"1677150893",
				"1560590611",
				"1069841353",
				"1663197874",
				"1581562090",
				"1238644613",
				"1462574687",
				"1631916641",
				"1603879300",
				"1743171901",
				"1152195102",
				"1797937134",
				"1430872784",
				"1401810322",
				"1686183142",
				"1304449174",
				"1735402532",
				"1764790950",
				"1425286642",
				"1296338447",
				"1604778062",
				"1803911665",
				"1225921344",
				"1254075095",
				"1493159967",
				"1733521075",
				"1036007957",
				"1590489632",
				"1160980725",
				"1807266910",
				"1711587585",
				"1720837815",
				"1817778385",
				"1730254322",
				"1655155113",
				"1097203817",
				"1748560864",
				"1010422292",
				"1415560385",
				"1172591507",
				"1016439434",
				"1789503532",
				"1683858195",
				"1142674150",
				"1809890832",
				"1506029975",
				"1510079902",
				"1658236217",
				"1358709653",
				"1595592200",
				"1780962163",
				"1362705445",
				"1740991145",
				"1376811760",
				"1745993451",
				"1704600764",
				"1650673013",
				"1642342551",
				"1688350613",
				"1738258517",
				"1682192000",
				"1088759295",
				"1300875242",
				"1220607604",
				"1683921493",
				"1464336514",
				"1690679770",
				"1806828630",
				"1276290971",
				"1400345001",
				"1131064177",
				"1715008763",
				"1435012005",
				"1291950992",
				"1530409024",
				"1807802555",
				"1574185410",
				"1815196301",
				"1409796961",
				"1463853234",
				"1676112735",
				"1506917187",
				"1000174993",
				"1588121005",
				"1885158852",
				"1016439434",
				"1831899577",
				"1607521515",
				"1502743384",
				"1875395121",
				"1579247125",
				"1463405387",
				"1783126357",
				"1774073372",
				"1896277792",
				"1865984842",
				"1410424890",
				"1407494801",
				"1682286140",
				"1815019802",
				"1250945494",
				"1822957497",
				"1111662854",
				"1622446194",
				"1704600764",
				"1829204155",
				"1405875434",
				"1190028671",
				"1045334082",
				"1831917962",
				"1912083227",
				"1847962391",
				"1601052772",
				"1509024755",
				"1291950992",
				"1191323735",
				"1676112735",
				"1909165622",
				"1655458705",
				"1572855487",
				"1686756672",
				"1859144510",
				"1296038210",
				"1930971755",
				"1797793882",
				"1677131963",
				"1099990567",
				"1883299704",
				"1548380717",
				"1792037250",
				"1163002084",
				"1670879717",
				"1886287490",
				"1931772043",
				"1721187443",
				"1504140380",
				"1609974421",
				"1925587292",
				"1798682295",
				"1278082341",
				"1752689755",
				"1627894702",
				"1847353832",
				"1730164220",
				"1852653595",
				"1836313943",
				"1689984847",
				"1789809964",
				"1924381211",
				"1263068104",
				"1760404383",
				"1712179461",
				"1916689457",
				"1768447754",
				"1659763935",
				"1216268087",
				"1759024321",
				"1756709235",
				"1506191855",
				"1864770697",
				"1900921921",
				"1247136007",
				"1674307631",
				"1365708022",
				"1738733915",
				"1691403233",
				"1413087083",
				"1764929632",
				"1900007423",
				"1716917992",
				"1705714613",
				"1592052000",
				"1280295133",
				"1105989760",
				"1525713022",
				"1715344852",
				"1037845693",
				"1758294877",
				"1446159732",
				"1819198173",
				"1880078161",
				"1826139522",
				"1722220770",
				"1662604511",
				"1134237321",
				"1423413537",
				"1563379533",
				"1433353981",
				"1345439683",
				"1008318433",
				"1151031644",
				"1442225603",
				"1154720113",
				"1380492603",
				"1426051327",
				"1234776924",
				"1319366301",
				"1305338935",
				"1356421510",
				"1522512970",
				"1020531000",
				"1200582655",
				"1192346917",
				"1280587070",
				"1166807885",
				"1279241844",
				"1239545697",
				"1287313695",
				"1285955545",
				"1457847335",
				"1268310761",
				"1232858571",
				"1436324492",
				"1567027225",
				"1322580450",
				"1233091590",
				"1026377635",
				"1343056561",
				"1201123523",
				"1437326193",
				"1243095913",
				"1415788080",
				"1038595277",
				"1215790894",
				"1444307485",
				"1571091142",
				"1067736492",
				"1458084437",
				"1319944553",
				"1394557212",
				"1336537110",
				"1239464443",
				"1367976147",
				"1002015232",
				"1087005354",
				"1194659935",
				"1358314052",
				"1446165162",
				"1219696993",
				"1181375061",
				"1559620312",
				"1408353103",
				"1232859675",
				"1557268735",
				"1204279211",
				"1022336402",
				"1395079867",
				"1236931141",
				"1568867665",
				"1195930213",
				"1305603314",
				"1403866984",
				"1563944861",
				"1465304684",
				"1045299213",
				"1312080734",
				"1523308637",
				"1172337190",
				"1551467720",
				"1060175284",
				"1276538982",
				"1409799345",
				"1400538370",
				"1496808883",
				"1558761167",
				"1058044084",
				"1359668680",
				"1571257461",
				"1289248067",
				"1454057342",
				"1419099634",
				"1087017252",
				"1421090842",
				"1261277674",
				"1241112582",
				"1264459067",
				"1004052567",
				"1480156145",
				"1198513315",
				"1264961274",
				"1273094381",
				"1434476591",
				"1073993545",
				"1074575861",
				"1295693333",
				"1804465171",
				"1456971382",
				"1058153563",
				"1656495710",
				"1134566005",
				"1904707825",
				"1797812423",
				"1883272185",
				"1985659917",
				"1458671764",
				"1732767407",
				"1301746064",
				"1576240790",
				"1925850877",
				"1845888232",
				"1725364355",
				"1852978717",
				"2010444665",
				"1560590611",
				"1348040821",
				"1581562090",
				"1238644613",
				"1682934162",
				"1560527843",
				"1814684391",
				"1854978615",
				"1881962863",
				"1930097271",
				"1676910563",
				"1421247082",
				"1694588680",
				"1997124363",
				"1644864757",
				"1835365880",
				"1245189302",
				"1695772510",
				"1656317135",
				"1590489632",
				"1112390091",
				"1616881357",
				"1996135045",
				"1323838161",
				"2007204037",
				"1995887443",
				"1891698553",
				"1376811760",
				"1959113550",
				"1769641017",
				"1868370517",
				"1682192000",
				"1947244225",
				"1921208427",
				"1482028153",
				"1430428017",
				"1760409721",
				"1417606311",
				"1847962391",
				"1350458360",
				"1713677115",
				"1891526660",
				"1888548885",
				"1863875590",
				"1661215255",
				"1676112735",
				"1750797900",
				"1973287395",
				"1340355473",
				"1006676015",
				"1565256153",
				"1243422631",
				"1817554661",
				"1916330363",
				"1680027783",
				"1944370825",
				"1037332383",
				"1806088451",
				"1852463712",
				"1732608211",
				"1061107951",
				"1867266404",
				"1504720144",
				"1114426803",
				"1962073381",
				"1685645051",
				"1276298287",
				"1916688100",
				"1235274844",
				"1812356830",
				"1954434411",
				"1812301760",
				"1672436483",
				"1735514530",
				"1927497907",
				"1694284442",
				"1947934381",
				"1660706270",
				"1950886440",
				"1733353713",
				"1429572031",
				"1205273600",
				"1310856992",
				"1170237510",
				"1268854300",
				"1093583600",
				"1409482971",
				"1147740494",
				"1348040821",
				"1073144472",
				"1420313335",
				"1260932574",
				"1163367915",
				"1307579845",
				"1265098901",
				"1238644613",
				"1245289290",
				"1139199394",
				"1260015783",
				"1144156234",
				"1414230431",
				"1405869414",
				"1423730124",
				"1235658142",
				"1063795685",
				"1493159967",
				"1036007957",
				"1160980725",
				"1402094133",
				"1231812874",
				"1025441013",
				"1328253890",
				"1321846451",
				"1345248401",
				"1283923504",
				"1267664224",
				"1411486577",
				"1103808530",
				"1281509060",
				"1074774420",
				"1509213903",
				"1456455130",
				"1010422292",
				"1055454074",
				"1406121523",
				"1172591507",
				"1062491252",
				"1451371497",
				"1030046520",
				"1142674150",
				"1005834370",
				"1358709653",
				"1412134681",
				"1497243971",
				"1023431957",
				"1194823641",
				"1273219590",
				"1362705445",
				"1400849132",
				"1310745032",
				"1154200437",
				"1410424890",
				"1405875434",
				"1275169330",
				"1300875242",
				"1406931810",
				"1400900250",
				"1448631734",
				"1352996073",
				"1137144422",
				"1296617760",
				"1076122797",
				"1107445472",
				"1284700663",
				"1335860567",
				"1474764305",
				"1231339542",
				"1142599737",
				"1512806653",
				"1340710942",
				"1406506321",
				"1435012005",
				"1078268405",
				"1087907413",
				"1412232443",
				"1481638480",
				"1363465390",
				"1402265991",
				"1420245525",
				"1462872972",
				"1080559572",
				"1138776355",
				"1193367691",
				"1154909567",
				"1498068311",
				"1094183984",
				"1409336355",
				"1405682251",
				"1069873275",
				"1189379592",
				"1043758513",
				"2930728602",
				"5180336013",
				"1579902167",
				"1548457630",
				"1970810781",
				"2425208302",
				"2198409741",
				"2859615820",
				"2174688732",
				"1361984537",
				"1789741375",
				"5066316792",
				"2575008657",
				"5304424657",
				"3101788407",
				"1834603103",
				"1275169330",
				"2876405702",
				"2935830101",
				"5126360401",
				"2901759151",
				"3564661301",
				"2804595774",
				"3309601537",
				"2437037495",
				"1404625693",
				"1477328553",
				"1138840560",
				"5260978892",
				"1903504817",
				"2513487005",
				"5339410669",
				"3167633350",
				"2929869551",
				"3034347644",
				"2295253081",
				"2727092087",
				"2474361337",
				"2168435062",
				"3059485280",
				"5041221205",
				"2883765012",
				"3031037704",
				"2147131285",
				"3938968663",
				"1686531137",
				"3794248412",
				"5457490853",
				"3860123122",
				"5455392556",
				"2149027144",
				"1945860821",
				"3790760420",
				"2511606500",
				"2635494314",
				"3584405721",
				"2520136132",
				"5345574060",
				"5495347263",
				"2854908967",
				"2836103731",
				"1348040821",
				"2344949000",
				"5415867235",
				"5409495960",
				"2302697395",
				"2086767557",
				"1648220985",
				"1837890124",
				"2809758391",
				"5346443553",
				"5175473576",
				"2233207971",
				"3569399894",
				"2378875913",
				"1834779047",
				"5413584083",
				"1036007957",
				"1745881080",
				"2871154322",
				"1232418552",
				"2899300923",
				"1835365880",
				"3916471850",
				"1400739154",
				"5394659765",
				"1608579460",
				"5413762748",
				"5409485416",
				"3251957397",
				"3245637442",
				"1965297514",
				"5502556479",
				"3736473760",
				"1971297403",
				"2142609867",
				"3926075172",
				"1825700117",
				"3787220553",
				"5334460004",
				"1854406933",
				"1904707825",
				"1630579292",
				"1655473892",
				"1911873294",
				"1310856992",
				"1314742930",
				"1732767407",
				"1879521550",
				"1119680687",
				"1779413837",
				"1940933520",
				"1580074751",
				"1504769361",
				"1768441743",
				"1836924715",
				"1611859132",
				"1588012053",
				"1773392200",
				"1852978717",
				"1749849411",
				"1890054941",
				"1888218610",
				"1581562090",
				"1245289290",
				"1560527843",
				"1728703620",
				"1231997754",
				"1464587334",
				"1425286642",
				"1659887421",
				"1785017875",
				"1901030181",
				"1160051871",
				"1235658142",
				"1657916317",
				"1881962863",
				"1142568270",
				"1254075095",
				"1572576445",
				"1304828461",
				"1644864757",
				"1745881080",
				"1788258091",
				"1695772510",
				"1902987273",
				"1904504921",
				"1887579734",
				"1919102301",
				"1400221690",
				"1739184222",
				"1771050021",
				"1934362287",
				"1679729243",
				"1055454074",
				"1527183021",
				"1099783130",
				"1885257272",
				"1323838161",
				"1500419043",
				"1275169330",
				"1719184447",
				"1770344801",
				"1660045892",
				"1789651114",
				"1902800965",
				"1895081573",
				"1787852987",
				"1506917187",
				"1213445814",
				"1148477244",
				"1907194437",
				"1189295834",
				"1373051627",
				"1807454013",
				"1227068493",
				"1339231730",
				"1884150484",
				"1759082913",
				"1860695260",
				"1763949001",
				"1770429422",
				"1747833600",
				"1791711501",
				"1340207980",
				"1169117887",
				"1077062260",
				"1408943613",
				"1376438630",
				"1685645051",
				"1738512405",
				"1902394762",
				"1283583687",
				"1583773493",
				"1841220137",
				"1656421353",
				"1298161451",
				"1644193234",
				"1400739154",
				"1340982913",
				"2061166381",
				"1902394081",
				"2485495801",
				"5249296544",
				"1662852524",
				"1960450427",
				"2352540560",
				"5102073475",
				"2558309081",
				"2265603904",
				"3471491350",
				"2609887335",
				"5444645236",
				"3826668517",
				"3265217312",
				"2810880997",
				"1937440331",
				"1970515541",
				"2649597223",
				"3324643834",
				"2316939052",
				"2472769063",
				"5285155762",
				"3492838302",
				"2884920040",
				"1701869402",
				"1799694937",
				"3538426267",
				"2057463607",
				"5361329139",
				"2695732660",
				"3608036203",
				"3192819493",
				"3174619270",
				"1805012324",
				"2997241555",
				"2640048937",
				"5247830039",
				"5109732787",
				"2162937262",
				"3567347131",
				"3012027467",
				"3937546680",
				"3933224288",
				"3862733885",
				"3225511717",
				"3479565811",
				"5283494279",
				"3819035247",
				"3898511218",
				"2119793845",
				"1934075065",
				"3923833421",
				"2900400203",
				"2876878222",
				"2318848645",
				"3224647351",
				"2274301531",
				"2494177945",
				"3280273215",
				"3700064584",
				"1336957407",
				"2954712770",
				"2260440030",
				"5239193120",
				"3447084954",
				"2525032141",
				"2235448584",
				"1916973777",
				"2711503530",
				"1869670523",
				"5230733002",
				"3935306361",
				"3820370349",
				"3896683953",
				"3908955859",
				"3473497712",
				"3885216246",
				"2714735523",
				"5175276430",
				"2759797101",
				"5201601235",
				"3305973015",
				"2968771902",
				"1735348552",
				"5132078934",
				"1340207980",
				"5013338087",
				"2420662783",
				"5237304174",
				"3264237904",
				"5135581773",
				"2142619103",
				"5289191232",
				"2387767864",
				"3425117594",
				"1950993241",
				"1612539954",
				"2111894034",
				"1607604327",
				"1314586804",
				"1058366673",
				"1049646917",
				"1423076651",
				"1165327410",
				"1283583687",
				"1098667020",
				"1275802093",
				"1404839600",
				"1424482942",
				"1113863141",
				"1087728325",
				"1342042105",
				"1423489710",
				"1265962282",
				"1151517774",
				"1404036522",
				"1220859785",
				"1260257150",
				"1446159732",
				"1130381834",
				"1358955873",
				"1172223157",
				"1040684931",
				"1363069765",
				"1342358184",
				"1135743431",
				"1424426222",
				"1002676817",
				"1450688842",
				"1404905547",
				"1270752717",
				"1136093815",
				"1439230087",
				"1302827131",
				"1285438213",
				"1411340033",
				"1293755115",
				"1169962613",
				"1394537335",
				"1310048182",
				"1443438011",
				"1180079070",
				"1413136404",
				"1033383273",
				"1054567967",
				"1455123805",
				"1000051143",
				"1130716473",
				"1214869962",
				"1225423764",
				"1245119810",
				"1443471694",
				"1403007663",
				"1266256267",
				"1061826827",
				"1230977442",
				"1160938093",
				"1077463422",
				"1375989045",
				"1104636415",
				"1397297961",
				"1400807115",
				"1047156210",
				"1324096592",
				"1283949240",
				"1036090483",
				"1430701132",
				"1163546843",
				"1189710973",
				"1285068217",
				"1198215703",
				"1077769925",
				"1426313164",
				"1102595487",
				"1211169410",
				"1102340181",
				"1060259215",
				"1410232667",
				"1155927555",
				"1001675772",
				"1096530647",
				"1451096201",
				"1432148252",
				"1095394777",
				"1450783832",
				"1222721907",
				"1244742903",
				"1400249775",
				"1416780587",
				"1217599257",
				"1276170907",
				"1188141961",
				"1308121021",
				"1141515441",
				"1404245181",
				"1234776924",
				"1317509423",
				"1166807885",
				"1197043925"};
		String projectName = "迈锐宝E2";
		for (String user : users) {
			WeiboMainPageTask wb = new WeiboMainPageTask(user, projectName);
			wb.run();
			Thread.sleep(15000);
		}
				
	}
}
