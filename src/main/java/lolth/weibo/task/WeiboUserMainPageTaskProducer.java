package lolth.weibo.task;

import java.text.MessageFormat;

import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lolth.weibo.fetcher.WeiboFetcher;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class WeiboUserMainPageTaskProducer extends PagingFetchTaskProducer {
	private static final String USER_MAIN_PAGE_URL_TEMPLATE = "http://weibo.cn/{0}?page={1}";
	
	public static final String USER_MAIN_PAGE = "user_main_page";
	private String user = null;
	private String keyword = null;

	public static void main(String[] args) {
		String taskname = "cmcc";
		
//		String[] users = {
//				"iaudi"
//				,"bmwchina"
//				,"mymb"
//				,"yifuyun"
//				,"fashiontyy"
//				,"2783989451"
//				,"sixinying"
//				,"weed9999647"
//				,"1500268464"
//				,"bosspat"
//				,"jsta"
//				,"liuzhanshu"
//				,"eric7777"
//				,"zuima"
//				,"sundance945"
//				,"muyuan1989"
//				,"206784449"
//				,"1955949053"
//				,"zylove118"
//				,"831020777"
//				,"h1101y0906"
//				,"207171792"
//				,"sdjsfee"
//				,"527303123"
//				,"2924219253"
//				,"3272922127"
//				,"534275677 "
//				,"bruceliuzhen"
//				,"3224211994"
//				,"1568607772"
//				,"3154810532"
//				,"wltgxx"
//				,"312914666"
				
//				};
		
		String [] users = {
				"2987373252",
				"2957464141",
				"2705214462",
				"2347596455",
				"2883470262",
				"2855818504",
				"2836195485",
				"2778212947",
				"2758362211",
				"2758355303",
				"2751968800",
				"2720374283",
				"2713089357",
				"2702970064",
				"2697113481",
				"2694970782",
				"2689894715",
				"2667549431",
				"2661991840",
				"2650105005",
				"2646387042",
				"2614392772",
				"2614266510",
				"2554503071",
				"2552253071",
				"2491093640",
				"2472676172",
				"2471240532",
				"2459157132",
				"2445075137",
				"2435119294",
				"2397355202",
				"2395684797",
				"2390645835",
				"2385844211",
				"2371492133",
				"2371426000",
				"2369899732",
				"2363787115",
				"2358828863",
				"2358587081",
				"2339081103",
				"2338467724",
				"2335904627",
				"2306265457",
				"2306265063",
				"2293139780",
				"2284846213",
				"2269513845",
				"2266877964",
				"2257792820",
				"2256447021",
				"2254713032",
				"2247705312",
				"2217209710",
				"2216197323",
				"2208754902",
				"2204224751",
				"2180726397",
				"2172685565",
				"2172518995",
				"2169010462",
				"2157380864",
				"2150301317",
				"2140955991",
				"2131499007",
				"2129646290",
				"2128279082",
				"2127969981",
				"2125153353",
				"2124104417",
				"2123154785",
				"2113606545",
				"2113079485",
				"2112755822",
				"2110912204",
				"2110366254",
				"2109945100",
				"2107406027",
				"2101556585",
				"2086635043",
				"2082424571",
				"1922449002",
				"1916470860",
				"1908194931",
				"1903549647",
				"1902506667",
				"1902332737",
				"1899344791",
				"1893823223",
				"1886839397",
				"1882344367",
				"1881649403",
				"1869808381",
				"1863228143",
				"1862823392",
				"1860132364",
				"1851092182",
				"1849575277",
				"1847025130",
				"1845179970",
				"1840578207",
				"2081572833",
				"2053623367",
				"2039971037",
				"2023541123",
				"2001898201",
				"1970842695",
				"1955627181",
				"1839239265",
				"1837475775",
				"1831725441",
				"1808180670",
				"1807291950",
				"1806891710",
				"1801730781",
				"1800222364",
				"1800030585",
				"1799008904",
				"1798582300",
				"1796377587",
				"1794578854",
				"1786930070",
				"1786668813",
				"1784980885",
				"1783161910",
				"1781910865",
				"1778966083",
				"1778460654",
				"1778117597",
				"1775251972",
				"1774206525",
				"1773776804",
				"1773493067",
				"1773225963",
				"1773029401",
				"1772071873",
				"1771474470",
				"1766807641",
				"1758617801",
				"1752189485",
				"1748430325",
				"1748418685",
				"1954842174",
				"1949919561",
				"1944379775",
				"1934681565",
				"1929134427",
				"1928539393",
				"1738321493",
				"1728185457",
				"1727715095",
				"1726982277",
				"1722157047",
				"1714432311",
				"1705218521",
				"1695899734",
				"1693024862",
				"1686656894",
				"1672656395",
				"1655043030",
				"1651985461",
				"1634851793",
				"1632481887",
				"1630909744",
				"1597722930",
				"1577185665",
				"1571397691",
				"1556992520",
				"1398124603",
				"1358375455",
				"3255518301",
				"3198822383",
				"3183684584",
				"2697243875",
				"2670242353",
				"2484772827",
				"2398277225",
				"2346329382",
				"2345545280",
				"2313361552",
				"2293254352",
				"2278552544",
				"2255160224",
				"2186010147",
				"2183581070",
				"2175415252",
				"2069032031",
				"2016188641",
				"1997330882",
				"1983730055",
				"1978443231",
				"1954519387",
				"1949475960",
				"1905574731",
				"1896349147",
				"1891396510",
				"1852459131",
				"1836460392",
				"1833270261",
				"1801553620",
				"1782706083",
				"1780433990",
				"1780329824",
				"1769029890",
				"1765922551",
				"1764830844",
				"1743969331",
				"1735597852",
				"1705777340",
				"1687367413",
				"1685941784",
				"1675993304",
				"1580950231"
		};
		
		
		WeiboUserMainPageTaskProducer.cleanAllTask(USER_MAIN_PAGE);
		for(String u : users){
			try {
				WeiboUserMainPageTaskProducer producer = new WeiboUserMainPageTaskProducer(USER_MAIN_PAGE,u,taskname);
				producer.setSleep(15000);
				producer.run();
			} catch (Exception e) {
				log.error("{} process error : ",u,e);
			}
		}
		
	}

	public WeiboUserMainPageTaskProducer(String taskQueueName, String user, String keyword) {
		super(taskQueueName);
		this.user = user;
		this.keyword = keyword;
	}

	@Override
	protected int getMaxPage() {
		String url = buildUrl(1);
		try {
			Document doc = WeiboFetcher.cnFetcher.fetch(url);

			if (doc.select("div#pagelist").size() == 0) {
				Elements elements = doc.select("div.c[id]");
				if(elements.isEmpty()){
					return 0;
				}
				return 1;
			} else {
				String html = doc.select("div#pagelist").first().text();
				String page = StringUtils.substringBetween(html, "/", "页");
				return Integer.parseInt(page);
			}
		} catch (Exception e) {
			log.error("{} get maxPage error : ", url, e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(USER_MAIN_PAGE_URL_TEMPLATE, user, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(USER_MAIN_PAGE);
		task.setUrl(url);
		task.setExtra(user);
		
		return task;
	}

}
