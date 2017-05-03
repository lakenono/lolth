package lolthx.weibo.fetch;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.weibo.utils.WeiboContentSpliter;
import lolthx.weibo.utils.WeiboIdUtils;
import lolthx.weibo.utils.WeiboTimeUtils;
import lolthx.weibo.bean.WeiboBean;
import lolthx.weibo.task.WeiboSearchTask;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

/**
 * 微博新框架爬取，爬取过程中发送微博用户task 构造函数参数为false的时候不会爬取用户资料
 * 
 * @author yanghp
 *
 */
@Slf4j
public class WeiboSearchFetch extends DistributedParser {

	private int sleep = 15000;
	private final String WEIBO_USER_URL_TEMPLATE = "https://weibo.cn/{0}";
	private final String WEIBO_USER_INFO_URL_TEMPLAGE = "https://weibo.cn/{0}/info";
	public static final String USER_QUEUE_NAME = "weibo_user_name_queue";
	private boolean isMq = true;
	//临时cookie
	private String[] cookies = {"_T_WM=e97f5f10b52eab06635bf70d61af164d; SUB=_2A250DfRaDeRhGedG71QV9SjJzjmIHXVX8ZwSrDV6PUJbkdBeLVDskW2ajVmj4-ixQwEKhLeBQ1jrfsQFZg..; SUHB=0YhQSRU58vKOR4; SCF=AhsVEVO1miKCOnpmU-RU-g74lvjdKlrbkDiLEIjpdwETqdf_pi89FOn4umsCrjrADB0cjwh7BmpkiQZUfmxNY-E.; SSOLoginState=1493795850",
			"_T_WM=3b52b1385ccb630816d423f41ead103b; SUB=_2A250DetsDeRhGeBO6lQV8SfIzT2IHXVX8fUkrDV6PUJbkdBeLRH7kW2ghmyZBEZJhGe7PQzQvraEvqCIDw..; SUHB=0tKqpLLiYzFzq7; SCF=Al7ETbavcdYrZYVCKBrMpvdRQGFr-R2YQqjcmqErHKL0t1KPX20-vAZFQxIeuGEOXGV_lgJjzAstOLaTaMOUXWI.; SSOLoginState=1493801788",
			"_T_WM=e998ee2c7be14c2ade0aed9c4c81aefb; SUB=_2A250DexFDeRhGeVP4lYZ8C_NzzyIHXVX8fQNrDV6PUJbkdBeLRnckW1XHUZnp4waJ7O_B9ViLzvRkwekag..; SUHB=0OMUJ2ITCABwaN; SCF=AjbOmIckzpSUu48M541cqryuZOMS2Pp6k5IB2MDBV7_h_o7gXmLGcOttxl3NVo5WvcnIoGHVuF0REaH6mgCqIoM.; SSOLoginState=1493802005; M_WEIBOCN_PARAMS=luicode%3D20000174",
			"_T_WM=d9d1d37082bf48df150c19293a7488a4; SUB=_2A250DeyhDeThGeRJ7FsS9CfFyj-IHXVX8fTprDV6PUJbkdBeLU34kW01kksdsxsQ47nkYoVH5g-hTZ3tXQ..; SUHB=0btsD6Pf2F6irn; SCF=AuhFa6wa0I5oeJM1zSbqt03JOlaYxBt8findKC4sqZMo0OTdwcrQoPw4r6hsKHSqcTRoisnv3OmT0-ttRMLxQ0o.; SSOLoginState=1493802225"};
	Random random = new Random();

	public WeiboSearchFetch() {

	}

	public WeiboSearchFetch(boolean isMq) {
		this.isMq = isMq;
	}

	@Override
	public String getQueueName() {
		return WeiboSearchTask.WEIBO_SEARCH_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			log.info("weibo search result is null !");
			return;
		}
		Document doc = Jsoup.parse(result);
		List<WeiboBean> beans = parse(doc, task);
		for (WeiboBean b : beans) {
			try {
				b.saveOnNotExist();
				if (isMq) {
					// 发送微博id爬取任务,抓取用户资料
					bulidWeiboUserTask(b.getUserid(), b.getUserurl(), task.getProjectName());
				}
			} catch (Exception e) {
				log.error("{} persist error ", b, e);
			}
		}
		beans.clear();
	}

	public void bulidWeiboUserTask(String id, String userUrl, String projectName) {
		try {
			String uid = id;
			if (!StringUtils.isNumeric(uid)) {
				Thread.sleep(sleep);
				uid = getUid(id);
			}

			if (Strings.isNullOrEmpty(uid)) {
				throw new RuntimeException("uid can not get id : " + id);
			}

			Task t = new Task();
			t.setProjectName(projectName);
			t.setQueueName(USER_QUEUE_NAME);
			t.setUrl(buildUserInfoUrl(uid));
			t.setExtra(id + "," + uid + "," + userUrl);
			Queue.push(t);

		} catch (Exception e) {
			log.error("{} get uid error :", id, e);
		}
	}

	private String buildUserInfoUrl(String uid) {
		return MessageFormat.format(WEIBO_USER_INFO_URL_TEMPLAGE, uid);
	}

	public String getUid(String id) throws IOException, InterruptedException, TException {
		String uid = null;
		String userUrl = buildUserUrl(id);
		// String cookies = GlobalComponents.authService
		// .getCookies(getCookieDomain());
//		String cookies = "_T_WM=1f3a474715a997aa787e8579d8fc9460; SUB=_2A2518bFoDeRhGedG71QV9SjJzjmIHXVXHd8grDV6PUJbkdAKLRLakW1ZhOmrO-TLm27k5iRiUAGb-4oSog..; gsid_CTandWM=4uCx6fb71h2jEl2ubFGpC7Kly8X; PHPSESSID=b2851f09046eb0558b1f80f3254df099";
		String ck = cookies[random.nextInt(4)];
		String html = GlobalComponents.jsoupFetcher.fetch(userUrl, ck, "");
		Document doc = Jsoup.parse(html);
		Elements imgElements = doc.select("img.por");
		if (imgElements.size() > 0) {
			uid = imgElements.first().parent().attr("href");
			uid = StringUtils.substringBetween(uid, "/", "/");
		}
		return uid;
	}

	private String buildUserUrl(String id) {
		return MessageFormat.format(WEIBO_USER_URL_TEMPLATE, id);
	}

	@Override
	public String getCookieDomain() {
		return "weibo.cn";
	}

	public List<WeiboBean> parse(Document document, Task task) throws IOException, ParseException {
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
			bean.setWeibourl("https://weibo.cn/comment/" + mid);

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
				// 原文用户
				String forwardUser = element.select("span.cmt a").text();
				bean.setForwardUser(forwardUser);
				// 原文内容
				String forwardText = element.select("span.ctt").text();
				bean.setForwardText(forwardText);
			}

			WeiboContentSpliter.spliteContent(bean);

			bean.setFetchTime(fetchTime);
			bean.setKeyword(task.getExtra());
			bean.setProjectName(task.getProjectName());
			weiboBeans.add(bean);

			log.debug(bean.toString());
		}
		return weiboBeans;
	}

	public static void main(String[] args) throws InterruptedException {
		while (true) {
			new WeiboSearchFetch().run();
			Thread.sleep(15000);
		}
	}
}
