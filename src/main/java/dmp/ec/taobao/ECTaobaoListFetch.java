package dmp.ec.taobao;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dmp.ec.ECBean;
import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECTaobaoListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "data_dmp_taobao_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		Elements divs = doc.select("div.items > div.item  ");
		if (divs.isEmpty()) {
			log.error(task.getUrl() + " is empty!");
			return;
		}

		String category = "";
		Elements categorys = doc.select("div.crumb.g-clearfix a.J_Ajax ");
		// 类别
		if (!categorys.isEmpty()) {
			category = categorys.text();
		}
		//

		for (Element div : divs) {
			ECBean taobaoBean = new ECBean("taobao");
			// 关键字
			taobaoBean.setKeyword(task.getProjectName());
			// 类别
			if (StringUtils.isNoneBlank(category)) {
				taobaoBean.setCategory(category);
			}
			Elements a = div.select("div.row-2 a");
			// 标题
			String title = a.text();
			taobaoBean.setTitle(title);
			// ID
			String itemId = a.attr("data-nid");
			taobaoBean.setId(itemId);
			// url
			String itemUrl = a.attr("href");
			taobaoBean.setUrl("http:" + itemUrl);
			try {
				taobaoBean.saveOnNotExist();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

	}

}
