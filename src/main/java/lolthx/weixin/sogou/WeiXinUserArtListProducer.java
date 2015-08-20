package lolthx.weixin.sogou;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class WeiXinUserArtListProducer extends Producer {

	private static final String WEIXIN_ARTICLE_LIST_URL = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid={0}&eqs={1}&ekv=7&page={2}&t=1439281967480";
	// http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=oIWsFtzDYuTnMO5quf5YBEq2_Tmg&eqs=N5sRonTg1615ojRf0XX5Iu318jZILFkT2SOy9jZqdYbhuayGlkWEGsIj%2FSxOP0Ek7M9NO&ekv=7&page=4&t=1439281967480
	private String id;
	private String eqs;
	private String keyword;

	public WeiXinUserArtListProducer(String projectName, String id, String eqs, String keyword) {
		super(projectName);
		this.id = id;
		this.eqs = eqs;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "weixin_user_art_list";
	}

	@Override
	protected int parse() throws Exception {
		String cookies = "ABTEST=0|1439273437|v1; IPLOC=CN1100; SUID=C50BE83D6A20900A0000000055C991DD; SUIR=1439273437; SUV=00B578AB3DE80BC555C991DD6C0BE345; SUID=C50BE83D6A28920A0000000055C991DE; SNUID=15DB38EDD0CACDD227A202D8D0DBFBD2; sct=5; LSTMV=280%2C262; LCLKINT=2992";
		String html = GlobalComponents.jsoupFetcher.fetch(buildUrl(1), cookies, "utf-8");
		String page = StringUtils.substringBetween(html, "totalPages\":", ",\"page");
		System.out.println(page);
		if (page == null || page.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(page);
		}
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(WEIXIN_ARTICLE_LIST_URL, id, eqs, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String args[]) throws Exception {
		String projectName = "weixin_article_user test";
		String[] ids = { "oIWsFtzDYuTnMO5quf5YBEq2_Tmg" };
		String[] eqs = { "N5sRonTg1615ojRf0XX5Iu318jZILFkT2SOy9jZqdYbhuayGlkWEGsIj%2FSxOP0Ek7M9NO" };
		String[] keywords = { "电动汽车" };
		for (int i = 0; i < ids.length; i++) {
			new WeiXinUserArtListProducer(projectName, ids[i], eqs[i], keywords[i]).run();
		}
	}

}
