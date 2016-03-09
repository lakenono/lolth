package lolthx.bitauto.k;

import java.text.MessageFormat;
import java.text.ParseException;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BitautoWordOfMouthProducer extends Producer  {

	private static final String BITATUO_KB_URL = "http://baa.bitauto.com/{0}/index-all-1-{1}-0.html";
	
	private String id;
	private String keyword;
	
	public BitautoWordOfMouthProducer(String projectName,String id,String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "bitauto_kb_reslove";
	}

	@Override //获取最大页
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(BITATUO_KB_URL, id, String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		try {
			buildTask.setStartDate(DateUtils.parseDate("2015-03-01", "yyyy-MM-dd"));
			buildTask.setEndDate(DateUtils.parseDate("2015-06-01", "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "bitautokb20150728";
		String[] ids = {"sagitar"};
		String[] keywords = {"速腾"};
		for (int i = 0; i < ids.length; i++) {
			new BitautoWordOfMouthProducer(projectName, ids[i], keywords[i]).run();
		}	
	}
	
}
