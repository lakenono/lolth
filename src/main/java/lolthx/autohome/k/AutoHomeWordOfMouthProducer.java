package lolthx.autohome.k;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolth.autohome.newbbs.AutoHomeBBSListProducer;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class AutoHomeWordOfMouthProducer extends Producer {
	
	private static final String AUTOHOME_KB_URL = "http://k.autohome.com.cn/{0}/index_{1}.html";

	private String id;

	private String keyword;
	
	/**
	 * @param projectName
	 * @param id
	 * @param keyword
	 */
	public AutoHomeWordOfMouthProducer(String projectName,String id,String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	
	@Override
	public String getQueueName() {
		return "autohome_kb_list";
	}

	@Override //获取最大页数
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		String text = document.select("div.page-cont a.page-item-last").attr("href");
		String page = StringUtils.substringBetween(text, "index_", ".html");
		return Integer.parseInt(page);
	}

	@Override //获取链接
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_KB_URL, id, String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" +keyword);
		return buildTask;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "auotHomeKb";
		//"78","110","634","117","50","496","164","834","2313" || "雅阁","凯美瑞","天籁","蒙迪欧","索纳塔","迈腾","君威","君越","迈锐宝"
		String[] ids = {"78"};
		String[] keywords = {"雅阁"};
		for (int i = 0; i < ids.length; i++) {
			new AutoHomeWordOfMouthProducer(projectName, ids[i], keywords[i]).run();
		}	
	}
	
}
