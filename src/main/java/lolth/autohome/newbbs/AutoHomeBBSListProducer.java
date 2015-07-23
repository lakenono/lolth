package lolth.autohome.newbbs;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class AutoHomeBBSListProducer extends Producer {

	private static final String AUTOHOME_BBS_URL = "http://club.autohome.com.cn/bbs/forum-c-{0}-{1}.html";

	private String id;

	private String keyword;

	public AutoHomeBBSListProducer(String projectName, String id, String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}
	//推送队列的名字
	@Override
	public String getQueueName() {
		return "autohome_bbs_list";
	}

	//解析最大页
	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		String text = document.select("div.pagearea span.fr").text();
		String page = StringUtils.substringBetween(text, "共", "页");
		return Integer.parseInt(page);
	}

	//拼接url
	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_BBS_URL, id, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "迈锐宝A20150721";
		String[] ids = {"78","110","634","117","50","496","164","834","2313"};
		String[] keywords = {"雅阁","凯美瑞","天籁","蒙迪欧","索纳塔","迈腾","君威","君越","迈锐宝"};
		for (int i = 0; i < ids.length; i++) {
			new AutoHomeBBSListProducer(projectName, ids[i], keywords[i]).run();
		}
	}

}
