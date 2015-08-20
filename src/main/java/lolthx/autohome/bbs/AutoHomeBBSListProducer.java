package lolthx.autohome.bbs;

import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class AutoHomeBBSListProducer extends Producer {

	private static final String AUTOHOME_BBS_URL = "http://club.autohome.com.cn/bbs/forum-c-{0}-{1}.html?orderby=dateline&qaType=-1";

	private String id;

	private String keyword;

	private int pageInt;

	public AutoHomeBBSListProducer(String projectName, String id, String keyword, int pageInt) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
		this.pageInt = pageInt;
	}

	// 推送队列的名字
	@Override
	public String getQueueName() {
		return "autohome_bbs_list";
	}

	// 解析最大页
	@Override
	protected int parse() throws Exception {
		if (pageInt != 0) {
			return pageInt;
		}
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		String text = document.select("div.pagearea span.fr").text();
		String page = StringUtils.substringBetween(text, "共", "页");
		return Integer.parseInt(page);
	}

	// 拼接url
	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_BBS_URL, id, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("2014-12-30", "yyyy-MM-dd"));
			buildTask.setEndDate(DateUtils.parseDate("2015-06-01", "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}

	public static void main(String[] args) throws Exception {

		String projectName = "英大财险A20150806";
		String[] ids = { "596", "3575", "3497", "3827", "2429", "2779", "537", "3648", "3395", "2141", "3119", "2761", "3430", "3533", "2357" };
		String[] keywords = { "力帆620", "云100", "知豆", "知豆D2", "逸动", "荣威E50", "荣威550", "奇瑞eQ", "江淮iEV", "传祺GA5", "东风风神E30", "秦", "唐", "EV系列", "MODEL S" };
		int[] pages = { 3, 3, 5, 3, 440, 1, 61, 20, 18, 12, 1, 157, 190, 40, 25 };
		for (int i = 0; i < ids.length; i++) {
			new AutoHomeBBSListProducer(projectName, ids[i], keywords[i], pages[i]).run();
		}
	}

}
