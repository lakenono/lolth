package dmp.ec.yhd;

import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.yhd.task.YhdSearchProduce;
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
 * 一号店dmp电商搜索抓取
 * 
 * @author yanghp
 *
 */
@Slf4j
public class ECYhdSearchFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return YhdSearchProduce.EC_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {

		List<ECBean> beans = new ArrayList<ECBean>();
		JSONObject parseObject = JSON.parseObject(result);
		Object object = parseObject.get("value");
		Document doc = Jsoup.parse((String) object);
		Elements eles = doc.select("div.mod_search_pro");
		String categroy = "";
		for (Element element : eles) {
			ECBean bean = new ECBean("yhd");
			Elements select = element.select("p.proName > a");
			String url = select.first().attr("href");
			String id = select.first().attr("pmid");
			//过滤没有pid数据
			if(id.equals("0")){
				continue;
			}
			String name = select.first().attr("title");
			// 抓取分类
			if(StringUtils.isBlank(categroy)){
				Document document = GlobalComponents.fetcher.document(url);
				Elements select2 = document.select("div.crumb > a");
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < select2.size()-1; i++) {
					sb.append(select2.get(i).text());
				}
				sb.deleteCharAt(sb.length()-1);
				categroy = sb.toString();
				categroy = StringUtils.replaceChars(categroy, "", "/");
			}
			bean.setId(id);
			bean.setUrl(url);
			bean.setTitle(name);
			bean.setCategory(categroy);
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
	public static void main(String[] args) {
		new ECYhdSearchFetch().run();
	}
}
