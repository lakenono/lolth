package lolthx.pacuto.k;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class PacutoWordOfMouthListProducer extends Producer  {

	private static final String PACUTO_K_LIST_URL = "http://price.pcauto.com.cn/comment/{0}/p{1}.html";
	
	private String id;
	private String keyword;
	
	
	public PacutoWordOfMouthListProducer(String projectName,String id,String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "pacuto_kb_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pageEs = doc.select("div#pcauto_page a");
		if (pageEs.isEmpty()) {
			if (!doc.select("div.main_table.clearfix").isEmpty()) {
				return 1;
			}
		}
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				return Integer.parseInt(pages);
			}
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(PACUTO_K_LIST_URL,id,String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		return buildTask;
	}
	
	public static void main(String args[]) throws Exception{
		String projectName = "pacuto kb test";
		String[] ids = {"sg10400", "sg9917"};
		//http://price.pcauto.com.cn/comment/{0}/p{1}.html
		//"sg10400", "sg9917", "sg7891", "sg8029", "sg10059", "sg10791" || "创酷TRAX","北京现代ix25","昂科拉","翼搏","标致2008","缤智"
		String[] keywords = {"创酷TRAX","北京现代ix25"};
		
		for(int i = 0;i<ids.length ;i++){
			new PacutoWordOfMouthListProducer(projectName, ids[i], keywords[i]).run();
		}
		
	}
	
}
