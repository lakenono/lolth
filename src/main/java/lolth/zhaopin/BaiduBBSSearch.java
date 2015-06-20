package lolth.zhaopin;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;

public class BaiduBBSSearch extends Producer {

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	private static final String BAIDU_SEARCH_BBS_URL = "https://www.baidu.com/s?wd={0}&pn={1}&oq={2}&ie=utf-8";

	private static final int count = 10;
	private String keyword;

	public BaiduBBSSearch(String projectName, String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "baidu_search_bbs_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements divs = doc.select("div.nums");
		if (divs.isEmpty()) {
			return 0;
		}
		String nums = divs.text();
		Matcher matcher = pattern.matcher(nums);
		nums = matcher.replaceAll("");
		if (StringUtils.isNumeric(nums)) {
			int page = 0;
			int parseInt = Integer.parseInt(nums);
			if (parseInt == 0) {
				page = 0;
			} else if (parseInt >= 760) {
				page = 76;
			} else if (parseInt <= 10) {
				page = 1;
			} else {
				page = parseInt / count;
				page = page + (parseInt % count != 0 ? 1 : 0);
			}
			return page;
		}
		return 0;
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	@Override
	protected String buildUrl(int pageNum) throws Exception {
		String codeKeyword = UrlUtils.encode("site:" + projectName + " " + keyword, "utf-8");
		return MessageFormat.format(BAIDU_SEARCH_BBS_URL, codeKeyword, String.valueOf((pageNum - 1) * count), codeKeyword);
	}
	
	public static void main(String[] args) {
		String [] projectNames = {"www.newsmth.net","bbs.sysu.edu.cn","bbs.xdnice.com","www.ujsbbs.com","bbs.njtech.edu.cn","hnnu.myubbs.com","bbs.zjut.edu.cn"};
		String [] keywords = {"工作","就业","求职","培训","offer","大街网","应届生求职网","智联招聘","前程无忧","考研","创业","兼职","实习","何处去"};
		for(String projectName:projectNames){
			for(String keyword:keywords){
				try {
					new BaiduBBSSearch(projectName,keyword).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
 	}

}
