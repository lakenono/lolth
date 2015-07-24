package lolth.yhd.fresh.fetch;

import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.yhd.fresh.bean.YhdFreshBean;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
@Slf4j
public class YhdFreshFetch extends DistributedParser{
	

	@Override
	public String getQueueName() {
		return "yhd_fresh_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		List<YhdFreshBean> beans = new ArrayList<YhdFreshBean>();
		JSONObject parseObject = JSON.parseObject(result);
		Object object = parseObject.get("value");
		Document doc = Jsoup.parse((String) object);
		Elements eles = doc.select("li.search_item");
		for (Element element : eles) {
			Elements select = element.select("div p.title a");
			YhdFreshBean bean = new YhdFreshBean();
			String url = select.first().attr("href");
			String id = select.first().attr("pmid");
			String name = select.first().attr("title");
			bean.setId(id);
			bean.setUrl(url);
			bean.setName(name);
			beans.add(bean);
		}
		log.info("fetch list:"+beans.size());
		if(!beans.isEmpty()){
			for (YhdFreshBean bean : beans) {
				if(!bean.exist()){
					bean.persist();
				}
			}
		}
		beans.clear();
	}
}
