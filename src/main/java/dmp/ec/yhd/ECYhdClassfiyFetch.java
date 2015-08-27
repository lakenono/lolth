package dmp.ec.yhd;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import dmp.ec.ECBean;

/**
 * 一号店dmp电商根据分类抓取
 * 
 * @author yanghp
 *
 */
@Slf4j
public class ECYhdClassfiyFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return ECYhdClassfiyProduce.EC_CLASSFIY_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {

		List<ECBean> beans = new ArrayList<ECBean>();
		JSONObject parseObject = JSON.parseObject(result);
		Object object = parseObject.get("value");
		Document doc = Jsoup.parse((String) object);
		Elements eles = doc.select("div.mod_search_pro");
		for (Element element : eles) {
			ECBean bean = new ECBean("yhd");
			Element select = element.select("p.proName.clearfix > a").first();
			String url = select.attr("href");
			String id = select.attr("pmid");
			String name = select.attr("title");
			// 抓取分类
			Document document = GlobalComponents.fetcher.document(url);
			String classify = document.select("div.crumb").text();
			classify = StringUtils.replaceChars(classify, "", ">");
			bean.setId(id);
			bean.setUrl(url);
			bean.setTitle(name);
			bean.setCategory(classify);
			bean.setKeyword(task.getExtra());
			beans.add(bean);
		}
		log.info("fetch list:" + beans.size());
		if (!beans.isEmpty()) {
			for (ECBean bean : beans) {
				bean.saveOnNotExist();
			}
		}
		beans.clear();
	}
}
