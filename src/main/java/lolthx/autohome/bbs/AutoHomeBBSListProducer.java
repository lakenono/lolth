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
																				      //http://club.autohome.com.cn/bbs/forum-c-200200-1.html

	private String id;
	private String keyword;
	private String taskId;
	private String startDate;
	private String endDate;
	
	public AutoHomeBBSListProducer(String projectName, String taskId,String id, String keyword,String startDate,String endDate) {
		super(projectName);
		this.taskId = taskId;
		this.id = id;
		this.keyword = keyword;
		this.startDate = startDate;
		this.endDate=endDate;
	}

	// 推送队列的名字
	@Override
	public String getQueueName() {
		return "autohome_bbs_reslove";
	}

	// 解析最大页
	@Override
	protected int parse() throws Exception {
		return 1;
	}

	// 拼接url
	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_BBS_URL, id, String.valueOf(pageNum));
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
		String taskId = "123321";
		String projectName = "福特比稿-汽车之家-20151224";
		String startDate = "2015-01-01";
		String endDate = "2016-01-01";
		String[] ids = { "614"	};
		String[] keywords = { "朗逸"	};
		for (int i = 0; i < ids.length; i++) {
			new AutoHomeBBSListProducer(projectName, taskId,ids[i], keywords[i],startDate,endDate).run();
		}
	}

}
