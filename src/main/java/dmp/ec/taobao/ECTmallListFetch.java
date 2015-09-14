package dmp.ec.taobao;

import lakenono.base.DistributedParser;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dmp.ec.ECBean;

public class ECTmallListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "data_dmp_tmall_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		// 类别
		String category = "";
		Elements categorys = doc.select("ul.crumbSlide-con ");
		if (!categorys.isEmpty()) {
			category = categorys.text();
		}

		Elements divElements = doc.select("div#J_ItemList div.product");

		for (Element div : divElements) {
			ECBean taobaoBean = new ECBean("tmall");
			// ID
			String id = div.attr("data-id");
			if (StringUtils.isBlank(id)) {
				continue;
			}
			taobaoBean.setId(id);

			// 类别
			if (StringUtils.isNoneBlank(category)) {
				taobaoBean.setCategory(category);
			}

			Element title = div.select("div.productMain div.productInfo div.productTitle h4.proInfo-title a").first();
			// 标题
			taobaoBean.setTitle(title.attr("title"));
			// url
			taobaoBean.setUrl("https:"+title.attr("href"));
			taobaoBean.setKeyword(task.getProjectName());

			try {
				taobaoBean.saveOnNotExist();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
