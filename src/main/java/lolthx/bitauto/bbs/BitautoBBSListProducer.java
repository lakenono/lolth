package lolthx.bitauto.bbs;

import java.text.MessageFormat;
import java.text.ParseException;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BitautoBBSListProducer extends Producer  {

	private static final String BITAUTO_BBS_URL = "http://baa.bitauto.com/{0}/index-all-all-{1}-1.html";
																                 //http://baa.bitauto.com/weilangverano/index-all-all-1-1.html
	
	private String id;

	private String keyword;
	
	private String taskId;
	
	private String startDate;
	
	private String endDate;
	
	public String getQueueName() {
		return  "bitauto_bbs_reslove";
	}
	//初始化对象
	public BitautoBBSListProducer(String projectName,String taskId,String id,String keyword,String startDate,String endDate) {
		super(projectName);
		this.taskId = taskId;
		this.id = id;
		this.keyword = keyword;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	
	@Override
	protected int parse() throws Exception {
		//没有分页标签
		return 1;
	}
	
	//拼接url
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(BITAUTO_BBS_URL,id,String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword + ":" + taskId);
		try {
			buildTask.setStartDate(DateUtils.parseDate(startDate, "yyyy-MM-dd"));
			buildTask.setEndDate(DateUtils.parseDate(endDate, "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args) throws Exception {
		String projectName = "东南汽车比稿-易车网-20151127";
		String taskId = "";
		String startDate = "";
		String endDate = "";
		String[] ids = { "changancs35"	};
		String[] keywords = {  "长安CS35"	 };
		for (int i = 0; i < ids.length; i++) {
			new BitautoBBSListProducer(projectName,taskId,ids[i],keywords[i],startDate,endDate).run();
		}
	}
}
