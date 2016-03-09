package lolthx.xcar.k;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class XCarWordOfMouthListProducer extends Producer {

	private static final String XCAR_KB_LIST_URL = "http://newcar.xcar.com.cn/{0}/review/0/0_{1}.htm";

	private String id;
	private String keyword;

	public XCarWordOfMouthListProducer(String projectName, String id, String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;

	}

	@Override
	public String getQueueName() {
		return "xcar_kb_reslove";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(XCAR_KB_LIST_URL, id, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		return buildTask;
	}

	public static void main(String args[]) throws Exception {
		String projectName = "Xcar kb";

		String[] ids = { "2474", "2511", "1733", "1405", "2431", "2581" };
		// http://newcar.xcar.com.cn/2474/review/0/0_1.htm
		String[] keywords = { "创酷", "ix25", "昂科拉", "翼搏", "标致2008", "缤智" };

		for (int i = 0; i < ids.length; i++) {
			new XCarWordOfMouthListProducer(projectName, ids[i], keywords[i]).run();
		}

	}

}
