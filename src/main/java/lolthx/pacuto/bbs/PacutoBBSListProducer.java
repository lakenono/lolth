package lolthx.pacuto.bbs;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PacutoBBSListProducer extends Producer {

	private static final String PACUTO_BBS_URL = "http://bbs.pcauto.com.cn/forum-{0}-{1}.html";

	private String id;

	private String keyword;

	public PacutoBBSListProducer(String projectName, String id, String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "pacuto_bbs_reslove";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(PACUTO_BBS_URL, id, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		return buildTask;
	}

	public static void main(String args[]) throws Exception {
		String projectName = "太平洋 test";
		// "20095", "20200", "18795", "18665", "19865", "20106" |
		// "创酷","ix25","昂科拉","翼搏","标致2008","缤智"
		String ids[] = { "20095", "20200" };
		String keywords[] = { "创酷", "ix25" };
		// http://bbs.pcauto.com.cn/forum-20095-1.html
		for (int i = 0; i < ids.length; i++) {
			new PacutoBBSListProducer(projectName, ids[i], keywords[i]).run();
		}

	}
}
