package lolthx.xcar.bbs;

import java.text.MessageFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class XCarBBSListProducer extends Producer{
	
	private static final String XCAR_BBS_LIST_URL = "http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&orderby=dateline&page={1}";
			//"http://www.xcar.com.cn/bbs/forumdisplay.php?fid={0}&page={1}"

	private String id;
	
	private String keyword;
	
	private int page;
	
	public XCarBBSListProducer(String projectName,String id,String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "xcar_bbs_list";
	}

	@Override
	protected int parse() throws Exception {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			Elements pages = doc.select("div.fn_0209 a");
			if (pages.size() == 1) {
				return 1;
			}
			if (pages.size() > 2) {
				int maxPageIdx = pages.size() - 2;
				String pageStr = pages.get(maxPageIdx).text();
				return Integer.parseInt(pageStr);
			}
			return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(XCAR_BBS_LIST_URL, id, String.valueOf(pageNum));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(id + ":" + keyword);
		return buildTask;
	}
	
	public static void main(String args[]) throws Exception{
		String projectName = "XCar_20150807";

		String[] ids = {"959","847"};
		String[] keywords = {"逸动EV","北汽EV160"};
		for(int i = 0 ; i < ids.length ; i++){
			new XCarBBSListProducer(projectName, ids[i], keywords[i]).run();
		}
		
		
	}

}
