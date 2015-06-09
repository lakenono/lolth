package lolth.jd.search;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.jd.search.bean.CommodityBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class JDCommodityFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "jd_commodity";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		String url = task.getUrl();
		String id = StringUtils.substringBetween(url, "com/", ".html");
		Document doc = Jsoup.parse(result);
		
		CommodityBean commodityBean = new CommodityBean();
		commodityBean.setId(id);
		commodityBean.setUrl(url);
		commodityBean.setProjectName(task.getProjectName());
		commodityBean.setKeyword(task.getExtra());
		Elements elements = doc.select("#name");
		if (!elements.isEmpty()) {
			String title = elements.text();
			commodityBean.setTitle(title);
		}

		elements = doc.select("#comment-count > a");
		if (!elements.isEmpty()) {
			String reply = elements.text();
			commodityBean.setReply(reply);
		}

		elements = doc.select("#product-detail-2 > table tr");
		if (!elements.isEmpty()) {
			String value;
			for (Element element : elements) {
				value = element.text();
				if (value.indexOf("类别") > -1) {
					commodityBean.setCategory(StringUtils.substring(value, 3));
				} else if (value.indexOf("特性") > -1) {
					commodityBean.setFeatures(StringUtils.substring(value, 3));
				}
			}
		}else{
			//#parameter2
			elements = doc.select("#parameter2 > li");
			if (!elements.isEmpty()) {
				String value;
				for (Element element : elements) {
					value = element.text();
					if (value.indexOf("类别") > -1) {
						commodityBean.setCategory(StringUtils.substring(value, 3));
					} else if (value.indexOf("特性") > -1) {
						commodityBean.setFeatures(StringUtils.substring(value, 3));
					}
				}
			}
		}
		log.debug(commodityBean.toString());
		commodityBean.persistOnNotExist();
	}

}
