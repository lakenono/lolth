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
	
	private int pageInt;
	
	public String getQueueName() {
		return  "bitauto_bbs_list";
	}
	//初始化对象
	public BitautoBBSListProducer(String taskQueueName,String id,String keyword, int pageInt) {
		super(taskQueueName);
		this.id = id;
		this.keyword = keyword;
		this.pageInt = pageInt;
	}

	
	@Override
	protected int parse() throws Exception {
		if (pageInt != 0) {
			return pageInt;
		}
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
		try {
			buildTask.setStartDate(DateUtils.parseDate("2015-06-01", "yyyy-MM-dd"));
			buildTask.setEndDate(DateUtils.parseDate("2015-10-31", "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buildTask;
	}
	
	public static void main(String[] args) throws Exception {
		String projectName = "通用汽车比稿-易车网-20151109";
		String[] ids = { "h3","changancs75","trumpchigs4","baojun560","dx7"		};
		String[] keywords = { "哈弗H6",		"长安CS75",		"传祺GS4",	"宝骏560",	"东南dx7"	 };
		int[] pages = { 20,	30,	30,	25,	25	 };
		for (int i = 0; i < ids.length; i++) {
			new BitautoBBSListProducer(projectName,ids[i],keywords[i], pages[i]).run();
		}
	}
}
