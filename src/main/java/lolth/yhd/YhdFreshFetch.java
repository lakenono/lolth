package lolth.yhd;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolth.yhd.bean.YhdFreshBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class YhdFreshFetch extends DistributedParser{
	
	private final String JSONURL = "http://list.yhd.com/searchPage/c20947-0-81806/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k/?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";

	@Override
	public String getQueueName() {
		return "yhd_fresh_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		List<YhdFreshBean> beans = new ArrayList<YhdFreshBean>();
		Document doc = Jsoup.parse(result);
//		Elements elements = doc.select("div.mod_search_list li");
		Elements elements = doc.select("ul#itemSearchList li");
		if(!elements.isEmpty()){
			for (Element element : elements) {
				//获取id
				Elements select = element.select("p.title a");
				if(!select.isEmpty()){
					YhdFreshBean bean = new YhdFreshBean();
					//id
					String id = select.first().attr("pmid");
					//url
					String url = select.first().attr("href");
					//name
					String name = select.first().attr("title");
					bean.setId(id);
					bean.setUrl(url);
					bean.setName(name);
					beans.add(bean);
				}
			}
		}
//		获取URL的页数num
		String num = StringUtils.substringBetween(task.getUrl(), "=", "&");
		String jsonURL = MessageFormat.format(JSONURL,num);
		//抓取第二页面，解析json数据
		String fetchJson = GlobalComponents.fetcher.fetch(jsonURL);
		String text = StringUtils.substringBetween(fetchJson,"<body>", "</script>");
		JSONObject parseObject = JSON.parseObject(text);
		Object object = parseObject.get("value");
		Document jdoc = Jsoup.parse((String) object);
		Elements eles = jdoc.select("li.search_item");
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
