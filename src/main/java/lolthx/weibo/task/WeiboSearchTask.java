package lolthx.weibo.task;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 微博搜索任务
 * 
 * @author yanghp
 *
 */
@Slf4j
public class WeiboSearchTask {
	// 队列名称
	public static final String WEIBO_SEARCH_QUEUE = "weibo_search_queue";
	// 微博搜索模板URL
	private final String CN_WEIBO_SEARCH_URL_TEMPLATE = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword={0}&advancedfilter=1&starttime={1}&endtime={2}&sort=time&page={3}";
	private String keyword;// 搜索关键字
	private String startTime;// 开始时间
	private String endTime;// 结束时间
//	private int sleep = 15000;// 休眠15秒

	public WeiboSearchTask(String keyword) throws ParseException {
		this(keyword, "", "");
	}

	public WeiboSearchTask(String keyword, String startTime, String endTime)
			throws ParseException {
		this.keyword = keyword;
		handleTime(startTime, endTime);
	}

	private void handleTime(String startTime, String endTime)
			throws ParseException {
		Date startDate, endDate;

		if (StringUtils.isNotBlank(endTime)) {
			endDate = DateUtils.parseDate(endTime, new String[] { "yyyyMMdd" });
		} else {
			endDate = new Date();
		}

		if (StringUtils.isNotBlank(startTime)) {
			startDate = DateUtils.parseDate(startTime,
					new String[] { "yyyyMMdd" });
		} else {
			startDate = DateUtils.addDays(endDate, -1);

		}

		this.startTime = DateFormatUtils.format(startDate, "yyyyMMdd");
		this.endTime = DateFormatUtils.format(endDate, "yyyyMMdd");

		log.debug("startTime : {} | endTime : {} ", this.startTime,
				this.endTime);
	}

	private String buildUrl(int pageNum) {
		return MessageFormat.format(CN_WEIBO_SEARCH_URL_TEMPLATE, keyword,
				startTime, endTime, String.valueOf(pageNum));
	}

	private void run() throws InterruptedException, TException {

		log.info("{} sina_weibo search task startTime {} - {}", this.keyword,this.startTime,this.endTime);
		int pagenum = getMaxPage();
		log.info("{} sina_weibo search Get max page : {}", this.keyword,
				pagenum);
//		Thread.sleep(sleep);
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
		String cookies = "_T_WM=381052f5df15a47db4b6c216d9fa6b8e; SUB=_2A254qy2qDeSRGeNL7FQS9inIyj-IHXVYV7PirDV6PUJbrdANLVPhkW1Mx5Pwf3qtPcXl9Bixn6Md_eO72Q..; gsid_CTandWM=4uDre42b1a7eMv2kMnqKPnoFp6F";
		String html = GlobalComponents.jsoupFetcher.fetch(url, cookies);
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
		task.setProjectName(this.keyword);
		task.setQueueName(WEIBO_SEARCH_QUEUE);
		task.setUrl(url);
		return task;
	}
	public static void main(String[] args) throws ParseException, InterruptedException, TException {
		WeiboSearchTask weibo = new WeiboSearchTask("瑞纳");
		weibo.run();
	}
}
