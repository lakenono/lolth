package lolthx.bitauto.bbs;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BitautoBBSListProducer extends Producer  {

	private static final String BITAUTO_BBS_URL = "http://baa.bitauto.com/{0}/index-all-all-{1}-1.html";
																                 //http://baa.bitauto.com/weilangverano/index-all-all-1-1.html
	
	private String id;

	private String keyword;
	
	public String getQueueName() {
		return  "bitauto_bbs_list";
	}
	//初始化对象
	public BitautoBBSListProducer(String taskQueueName,String id,String keyword) {
		super(taskQueueName);
		this.id = id;
		this.keyword = keyword;
	}

	
	@Override
	protected int parse() throws Exception {
		//没有分页标签
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pageEs = document.select("div.the_pages a");
		
		// 没有分页标签
		if (pageEs.isEmpty()) {
			if (!document.select("div.postslist_xh").isEmpty()) {
				return 1;
			}
		}
		// 有分页标签
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				return Integer.parseInt(pages);
			}
		}
		return 0;
	}
	
	//拼接url
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(BITAUTO_BBS_URL,id,String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		return buildTask;
	}
	
	public static void main(String[] args) throws Exception {
		String projectName = "别克威朗-20150728";
		//"trax", "ix25", "angkela", "yibo", "2008", "vezel" || "创酷","北京现代","昂科拉","翼搏","标致","本田缤智"
		String[] ids = {"weilangverano"};
		String[] keywords = {"威朗VERANO"};
		for (int i = 0; i < ids.length; i++) {
			new BitautoBBSListProducer(projectName,ids[i],keywords[i]).run();
		}
	}
}
