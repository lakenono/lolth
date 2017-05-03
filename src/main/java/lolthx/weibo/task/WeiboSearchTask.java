package lolthx.weibo.task;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 微博搜索任务 注意：启动任务的时候需要根据projectName创建表，projectName为英文
 * 
 * @author yanghp
 *
 */
@Slf4j
@Data
public class WeiboSearchTask {
	// 队列名称
	public static final String WEIBO_SEARCH_QUEUE = "weibo_search_queue";
	// 微博搜索模板URL
	private final String CN_WEIBO_SEARCH_URL_TEMPLATE = "https://weibo.cn/search/mblog?hideSearchFrame=&keyword={0}&advancedfilter=1&starttime={1}&endtime={2}&sort=time&page={3}";
	private String projectName;
	private String keyword;// 搜索关键字
	private String startTime;// 开始时间
	private String endTime;// 结束时间
	
	//固定的cookie
	private String[] cookies = {"_T_WM=e97f5f10b52eab06635bf70d61af164d; SUB=_2A250DfRaDeRhGedG71QV9SjJzjmIHXVX8ZwSrDV6PUJbkdBeLVDskW2ajVmj4-ixQwEKhLeBQ1jrfsQFZg..; SUHB=0YhQSRU58vKOR4; SCF=AhsVEVO1miKCOnpmU-RU-g74lvjdKlrbkDiLEIjpdwETqdf_pi89FOn4umsCrjrADB0cjwh7BmpkiQZUfmxNY-E.; SSOLoginState=1493795850",
			"_T_WM=3b52b1385ccb630816d423f41ead103b; SUB=_2A250DetsDeRhGeBO6lQV8SfIzT2IHXVX8fUkrDV6PUJbkdBeLRH7kW2ghmyZBEZJhGe7PQzQvraEvqCIDw..; SUHB=0tKqpLLiYzFzq7; SCF=Al7ETbavcdYrZYVCKBrMpvdRQGFr-R2YQqjcmqErHKL0t1KPX20-vAZFQxIeuGEOXGV_lgJjzAstOLaTaMOUXWI.; SSOLoginState=1493801788",
			"_T_WM=e998ee2c7be14c2ade0aed9c4c81aefb; SUB=_2A250DexFDeRhGeVP4lYZ8C_NzzyIHXVX8fQNrDV6PUJbkdBeLRnckW1XHUZnp4waJ7O_B9ViLzvRkwekag..; SUHB=0OMUJ2ITCABwaN; SCF=AjbOmIckzpSUu48M541cqryuZOMS2Pp6k5IB2MDBV7_h_o7gXmLGcOttxl3NVo5WvcnIoGHVuF0REaH6mgCqIoM.; SSOLoginState=1493802005; M_WEIBOCN_PARAMS=luicode%3D20000174",
			"_T_WM=d9d1d37082bf48df150c19293a7488a4; SUB=_2A250DeyhDeThGeRJ7FsS9CfFyj-IHXVX8fTprDV6PUJbkdBeLU34kW01kksdsxsQ47nkYoVH5g-hTZ3tXQ..; SUHB=0btsD6Pf2F6irn; SCF=AuhFa6wa0I5oeJM1zSbqt03JOlaYxBt8findKC4sqZMo0OTdwcrQoPw4r6hsKHSqcTRoisnv3OmT0-ttRMLxQ0o.; SSOLoginState=1493802225"};
	Random random = new Random();

	// private int sleep = 15000;// 休眠15秒
	
	public WeiboSearchTask(){
		
	}

	public WeiboSearchTask(String projectName, String keyword) throws ParseException {
		this(projectName, keyword, "", "");
	}

	public WeiboSearchTask(String projectName, String keyword, String startTime, String endTime) throws ParseException {
		this.keyword = keyword;
		this.projectName = projectName;
		handleTime(startTime, endTime);

		log.debug("keyword {} , startTime : {} | endTime : {} ", keyword, this.startTime, this.endTime);
	}

	private void handleTime(String startTime, String endTime) throws ParseException {
		Date startDate, endDate;

		if (StringUtils.isNotBlank(endTime)) {
			endDate = DateUtils.parseDate(endTime, new String[] { "yyyyMMdd" });
		} else {
			endDate = new Date();
		}

		if (StringUtils.isNotBlank(startTime)) {
			startDate = DateUtils.parseDate(startTime, new String[] { "yyyyMMdd" });
		} else {
			startDate = DateUtils.addDays(endDate, -1);

		}

		this.startTime = DateFormatUtils.format(startDate, "yyyyMMdd");
		this.endTime = DateFormatUtils.format(endDate, "yyyyMMdd");
	}

	private String buildUrl(int pageNum) {
		return MessageFormat.format(CN_WEIBO_SEARCH_URL_TEMPLATE, keyword, startTime, endTime, String.valueOf(pageNum));
	}

	public void run() throws TException, InterruptedException {

		log.info("{} sina_weibo search task startTime {} - {}", this.keyword, this.startTime, this.endTime);
		int pagenum = getMaxPage();
		log.info("{} sina_weibo search Get max page : {}", this.keyword, pagenum);
		// Thread.sleep(sleep);
		// 发送第两个任务
		for (int i = 1; i <= pagenum; i++) {
			// 创建url
			String url = buildUrl(i);
			// 创建抓取任务
			Task task1 = buildTask(url);
			// 推送任务
			Queue.push(task1);
		}

	}

	private int getMaxPage() throws TException, InterruptedException {
		String url = buildUrl(1);
//		String cookies = GlobalComponents.authService.getCookies("weibo.cn");
		String ck = cookies[random.nextInt(4)];
//		String ck = "_T_WM=e97f5f10b52eab06635bf70d61af164d; SUB=_2A250DfRaDeRhGedG71QV9SjJzjmIHXVX8ZwSrDV6PUJbkdBeLVDskW2ajVmj4-ixQwEKhLeBQ1jrfsQFZg..; SUHB=0YhQSRU58vKOR4; SCF=AhsVEVO1miKCOnpmU-RU-g74lvjdKlrbkDiLEIjpdwETqdf_pi89FOn4umsCrjrADB0cjwh7BmpkiQZUfmxNY-E.; SSOLoginState=1493795850";
		String html = GlobalComponents.jsoupFetcher.fetch(url, ck, "UTF-8");
		Document doc = Jsoup.parse(html);
		
		if (doc.select("div#pagelist").size() == 0) {
			Elements elements = doc.select("div.c[id]");
			if (elements.isEmpty()) {
				return 0;
			}
			return 1;
		} else {
			String page = doc.select("div#pagelist").first().text();
			String nums = StringUtils.substringBetween(page, "/", "页");
			return Integer.parseInt(nums);
		}
	}

	private Task buildTask(String url) {
		Task task = new Task();
		task.setProjectName(this.projectName);
		task.setQueueName(WEIBO_SEARCH_QUEUE);
		task.setExtra(this.keyword);
		task.setUrl(url);
		return task;
	}

	public static void main(String[] args) throws Exception {
		/*
		String dir = Class.class.getResource("/") + "weiboSearch";
		dir = StringUtils.substringAfter(dir, ":");
		String file = WeiboFileUtils.rename2Temp(dir);
		if (file == null) {
			log.info("no task file,Program exits!!!");
			return;
		}
		log.info("task begin is :{}", file);
		String projectName = WeiboFileUtils.getProjectName(file);
		List<String> readFile = WeiboFileUtils.readFile(file);
		for (String line : readFile) {
			String[] split = StringUtils.splitByWholeSeparator(line, "\t");
			if (split.length == 3) {
				new WeiboSearchTask(projectName, split[0], split[1], split[2]).run();
			} else {
				new WeiboSearchTask(projectName, split[0]).run();
			}
			Thread.sleep(15000);
		}
		WeiboFileUtils.rename2Done(file);
		*/
		new WeiboSearchTask("hangkong", "自由光","20170320","20170420").run();
	}
}
