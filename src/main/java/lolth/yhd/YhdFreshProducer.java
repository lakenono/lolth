package lolth.yhd;

import java.text.MessageFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;

public  class YhdFreshProducer extends Producer{
	
	private static final String YHD_FRESH_URL = "http://list.yhd.com/c20947-0/#page={0}&sort=1";

	public YhdFreshProducer(String projectName) {
		super(projectName);
	}

	@Override
	public String getQueueName() {
		return "yhd_fresh_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Elements elements = doc.select("a#lastPage");
		if(!elements.isEmpty()){
			String page = elements.first().text();
			return Integer.parseInt(page);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(YHD_FRESH_URL,pageNum);
	}
	
	public static void main(String[] args) throws Exception {
		
		new YhdFreshProducer("yhd_fresh").run();
	}
	
}
