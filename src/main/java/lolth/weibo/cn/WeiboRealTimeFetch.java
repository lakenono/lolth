package lolth.weibo.cn;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lolth.weibo.bean.WeiboBean;
import lolth.weibo.fetcher.WeiboFetcher;
import lolth.weibo.utils.WeiboContentSpliter;
import lolth.weibo.utils.WeiboIdUtils;
import lolth.weibo.utils.WeiboTimeUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * oppo 实时抓取
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class WeiboRealTimeFetch {
	private static final String CN_WEIBO_SEARCH_URL_TEMPLATE = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword={0}&advancedfilter=1&starttime={1}&endtime={2}&sort=time&page={3}";

	private String keyword;
	private String startTime;
	private String endTime;

	public static void main(String[] args) throws Exception {
		long sleep = 10 * 60 * 1000;
		String keyword = "oppo";
		while (true) {
			try {
				new WeiboRealTimeFetch(keyword).run();
				log.info("Wait for next batch fetch ... ");
				Thread.sleep(sleep);
			} catch (Exception e) {
				log.error("Weibo fetch fail ! ", e);
			}
		}
	}

	public void run() throws Exception {
		int pages = getMaxPage();
		Thread.sleep(15000);
		if (pages == 0) {
			throw new RuntimeException("Get max page fail : " + keyword + " | " + startTime + " | " + endTime);
		}

		for (int i = 1; i <= pages; i++) {
			String url = buildUrl(i);
			try {
				Document doc = WeiboFetcher.cnFetcher.fetch(url);
				List<WeiboBean> beans = parse(doc);

				for (WeiboBean b : beans) {
					try {
						b.persistOnNotExist();
					} catch (Exception e) {
						log.error("{} persist error ", b, e);
					}
				}
				Thread.sleep(15000);
			} catch (Exception e) {
				log.error("{} download or parse error : ", url, e);
			}
		}
	}

	public WeiboRealTimeFetch(String keyword) throws ParseException {
		this(keyword, "", "");
	}

	public WeiboRealTimeFetch(String keyword, String startTime, String endTime) throws ParseException {
		this.keyword = keyword;
		handleTime(startTime, endTime);
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

		log.debug("startTime : {} | endTime : {} ", this.startTime, this.endTime);
	}

	protected int getMaxPage() throws IOException, InterruptedException {
		String url = buildUrl(1);
		Document doc = WeiboFetcher.cnFetcher.fetch(url);

		if (doc.select("div#pagelist").size() == 0) {
			return 0;
		} else {
			String html = doc.select("div#pagelist").first().text();
			String page = StringUtils.substringBetween(html, "/", "页");
			return Integer.parseInt(page);
		}
	}

	protected String buildUrl(int pageNum) {
		return MessageFormat.format(CN_WEIBO_SEARCH_URL_TEMPLATE, keyword, startTime, endTime, String.valueOf(pageNum));
	}

	public List<WeiboBean> parse(Document document) throws IOException, ParseException {
		Elements elements = document.select("div.c[id]");

		LocalDateTime now = LocalDateTime.now();
		String fetchTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

		List<WeiboBean> weiboBeans = new LinkedList<WeiboBean>();

		for (Element element : elements) {
			String html = element.html();

			WeiboBean bean = new WeiboBean();

			// mid
			String mid = StringUtils.substringAfter(element.attr("id"), "M_");
			bean.setMid(mid);

			// id
			bean.setId(WeiboIdUtils.toId(mid));

			// 发布时间
			String postTimeText = element.select("span.ct").text();
			postTimeText = StringUtils.substringBefore(postTimeText, "来自");
			postTimeText = WeiboTimeUtils.getNormalTime(postTimeText, now);
			bean.setPostTime(postTimeText);

			// username
			String username = element.select("a.nk[href]").first().text();
			bean.setUsername(username);

			// userurl
			String userurl = element.select("a.nk[href]").first().attr("href");
			bean.setUserurl(userurl);

			// userid
			String userid = StringUtils.substringAfterLast(userurl, "/");
			bean.setUserid(userid);

			// weibourl
			bean.setWeibourl("http://weibo.cn/comment/" + mid);

			// source
			String source = element.select("span.ct").text();
			source = StringUtils.substringAfter(source, "来自");
			bean.setSource(source);

			// 赞
			Element likesElement = element.getElementsMatchingOwnText("赞\\[").last();
			String likes = StringUtils.substringBetween(likesElement.text(), "赞[", "]");
			bean.setLikes(likes);

			// 转发
			String forwards = StringUtils.substringBetween(html, ">转发[", "]");
			bean.setReposts(forwards);

			// 评论
			String comments = StringUtils.substringBetween(html, ">评论[", "]");
			bean.setComments(comments);

			// 原创
			if (!StringUtils.contains(html, "原文转发")) {
				// text
				String text = element.select("span.ctt").text();
				bean.setText(StringUtils.substringAfter(text, ":"));
			} else {
				String pweibourl = element.select("a.cc").first().attr("href");
				bean.setPweibourl(pweibourl);

				String pmid = StringUtils.substringBetween(pweibourl, "comment/", "?");
				bean.setPmid(pmid);

				String pid = WeiboIdUtils.toId(pmid);
				bean.setPid(pid);

				String text = StringUtils.substringBetween(element.select("div").last().text(), "转发理由:", "赞[");
				bean.setText(text);
			}

			WeiboContentSpliter.spliteContent(bean);

			bean.setFetchTime(fetchTime);
			bean.setKeyword(keyword);
			weiboBeans.add(bean);

			log.debug(bean.toString());
		}
		return weiboBeans;
	}

}
