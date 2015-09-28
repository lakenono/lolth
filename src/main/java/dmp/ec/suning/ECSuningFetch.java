package dmp.ec.suning;

import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dmp.ec.ECBean;

@Slf4j
public class ECSuningFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "ec_suning";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("li.item");
		List<ECBean> beans = new ArrayList<ECBean>();
		String category = "";
		for (Element element : elements) {
			Elements select = element.select("div.i-name > a.sellPoint");
			String name = select.text();
			String url = select.first().absUrl("href");
			String id = StringUtils.substringAfterLast(url, "/");
			id = StringUtils.substringBefore(id, ".");
			if(StringUtils.isBlank(category)){
				String html = GlobalComponents.jsoupFetcher.fetch(url);
				Document parse = Jsoup.parse(html);
				Elements eles = parse.select("a.ft");
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < eles.size(); i++) {
					sb.append(eles.get(i).text()).append("/");
				}
				sb.deleteCharAt(sb.length()-1);
				category = sb.toString();
			}
			ECBean sn = new ECBean("suning");
			sn.setId(id);
			sn.setCategory(category);
			sn.setKeyword(task.getExtra());
			sn.setTitle(name);
			sn.setUrl(url);
			beans.add(sn);
		}
		log.info("fetch suning goods numbers : {}",beans.size());
		for (ECBean ecBean : beans) {
			ecBean.saveOnNotExist();
		}
		beans.clear();
	}
	
	public static void main(String[] args) {
		new ECSuningFetch().run();
	}
}
