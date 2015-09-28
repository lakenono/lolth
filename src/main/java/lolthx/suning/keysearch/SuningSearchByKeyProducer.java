package lolthx.suning.keysearch;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class SuningSearchByKeyProducer  extends Producer  {

	private String id;
	private String keyword;
	private static final String SUNING_SEARCH_BU_KEY_URL = "http://list.suning.com/0-{0}-0-0-0-9017.html?cp={1}";
	private String sn_queue = "suning_search_list";
	
	public SuningSearchByKeyProducer(String projectName,String id,String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}
	
	public void setECQueue(){
		this.sn_queue = "ec_suning";
	}
	
	@Override
	public String getQueueName() {
		return sn_queue;
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Element lastPage = doc.getElementById("pageLast");
		Element firstPage = doc.getElementById("pageFirst");
		if (lastPage != null) {
			String maxPage = lastPage.text();
			if (StringUtils.isNumeric(maxPage)) {
				return Integer.parseInt(maxPage);
			} else {
				return 0;
			}
		}else{
			Element py = doc.getElementById("Py");
			if(py != null){
				return 0;
			}else{
				if(firstPage != null){
					return 1;
				}
			}
		}
		Elements items = doc.select("ul.container li.item");
		if (!items.isEmpty()) {
			return 1;
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(SUNING_SEARCH_BU_KEY_URL, id, String.valueOf(pageNum - 1));
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String args[]){
		String projectName = "ceshi";
		String[] ids = {"20368"};
		String[] keywords = {"电风扇"};
		for(int i =0 ;i < keywords.length;i++){
			SuningSearchByKeyProducer sn = new SuningSearchByKeyProducer(projectName,ids[i], keywords[i]);
			sn.setECQueue();
			sn.run();
		}
		
	}
	
	
	
}
