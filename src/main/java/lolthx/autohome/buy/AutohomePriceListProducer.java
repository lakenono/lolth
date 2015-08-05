package lolthx.autohome.buy;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AutohomePriceListProducer extends Producer {

	private static final String AUTOHOME_PRICE_DETAIL_URL = "http://jiage.autohome.com.cn/price/carlist/p-{0}-0-0-0-0-0-{1}";

	private String id;

	private String keyword;

	public AutohomePriceListProducer(String projectName, String id, String keyword) {
		super(projectName);
		this.id = id;
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "autohome_price_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));

		Elements pageE = doc.select("span.page-item-jump");
		if (pageE.isEmpty()) {
			return 0;
		}

		String pages = pageE.first().text();
		pages = StringUtils.substringBetween(pages, "共", "页");
		if (StringUtils.isNumeric(pages)) {
			return Integer.parseInt(pages);
		}

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_PRICE_DETAIL_URL, id, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String args[]) throws Exception {
		String projectName = "AutoHomePrice Test";

		//String[] mainIds = { "78", "110", "634", "117", "50", "496", "164", "834", "2313" };
		//String[] mainkws = { "雅阁", "凯美瑞", "天籁", "蒙迪欧", "索纳塔", "迈腾", "君威", "君越", "迈锐宝" };
	
		String[] mainIds = {  "634" };
		String[] mainkws = {  "天籁"};
		
		AutoHomePriceDescription autohpd = new AutoHomePriceDescription();
		Map<String, String[]> map = new HashMap<String, String[]>();
		map = autohpd.resolveUrl(mainIds, mainkws);
		String[] ids = map.get("ids");
		String[] keywords = map.get("keywords");

		/**
		 String[] ids = { "19491"};
		 String[] keywords = { "奥迪A4L 2015款" };
		 */
		
		for (int i = 0; i < ids.length; i++) {
			new AutohomePriceListProducer(projectName, ids[i], keywords[i]).run();
		}

	}

}
