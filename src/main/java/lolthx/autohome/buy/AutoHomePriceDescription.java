package lolthx.autohome.buy;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import lakenono.core.GlobalComponents;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoHomePriceDescription {

	private static final String AUTOHOME_PRICE_URL = "http://jiage.autohome.com.cn/price/carlist/s-{0}";

	/**
	 * 根据主ID解析所有的子明细ID
	 */
	public Map<String, String[]> resolveUrl(String[] mainIds, String[] mainkws) throws Exception {
		String[] ids;
		String[] keywords;
		Map<String, String[]> map = new HashMap<String, String[]>();
		StringBuffer stb = new StringBuffer();
		StringBuffer sty = new StringBuffer();
		for (int i = 0; i < mainIds.length; i++) {
			String mainId = mainIds[i];
			String keyword = mainkws[i];

			Document doc = GlobalComponents.fetcher.document(buildMainUrl(mainId, 1));

			Elements els = doc.select("div.content div.tab-content div.model-list ul");

			for (Element el : els) {

				String href = el.select("li.model-list-item a.model-name").attr("href");

				String id = StringUtils.substringAfter(href, "carlist/p-");

				if (stb.length() == 0) {
					stb.append(id);
					sty.append(keyword);
				} else {
					stb.append("," + id);
					sty.append("," + keyword);
				}
			}
		}

		ids = stb.toString().split(",");
		keywords = sty.toString().split(",");

		map.put("ids", ids);
		map.put("keywords", keywords);

		return map;
	}

	protected String buildMainUrl(String mainId, int pageNum) throws Exception {
		return MessageFormat.format(AUTOHOME_PRICE_URL, mainId, String.valueOf(pageNum));
	}

}
