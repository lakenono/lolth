package lolthx.xcar.bbs;

import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class XCarBBSListProducer extends Producer {

	private static final String XCAR_BBS_LIST_URL = "http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&orderby=dateline&page={1}";
	// "http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&page={1}"

	private String id;

	private String keyword;

	private int pageInt;

	public XCarBBSListProducer(String projectName, String id, String keyword, int pageInt) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
		this.pageInt = pageInt;
	}

	@Override
	public String getQueueName() {
		return "xcar_bbs_list";
	}

	@Override
	protected int parse() throws Exception {
		if (pageInt != 0) {
			return pageInt;
		}
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pages = doc.select("div.fn_0209 a");
		if (pages.size() == 1) {
			return 1;
		}
		if (pages.size() > 2) {
			int maxPageIdx = pages.size() - 2;
			String pageStr = pages.get(maxPageIdx).text();
			return Integer.parseInt(pageStr);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(XCAR_BBS_LIST_URL, id, String.valueOf(pageNum));
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

	public static void main(String args[]) throws Exception {
		String projectName = "埃尼比稿-爱卡汽车-20151216";
		String[] ids = { "118" };
		String[] keywords = { "机油论坛" };
		int[] pages = { 120 };
		for (int i = 0; i < ids.length; i++) {
			new XCarBBSListProducer(projectName, ids[i], keywords[i], pages[i]).run();
		}

	}

}
