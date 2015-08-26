package dmp.ec.jd;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import dmp.ec.ECBean;
import lakenono.base.DistributedParser;
import lakenono.base.Task;

public class ECJdItemFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "ec_dmp_jd_item";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		ECBean taobaoBean = new ECBean("jd");
		//
		Elements categorys = doc.select("div.breadcrumb");
		taobaoBean.setCategory(categorys.text());
		//
		Elements titles = doc.select("div#name h1");
		taobaoBean.setTitle(titles.text());
		//
		String url = task.getUrl();
		taobaoBean.setUrl(url);
		//
		String id = StringUtils.substringBetween(url, "com/", ".html");
		taobaoBean.setId(id);
		//
		taobaoBean.setKeyword(task.getProjectName());

		taobaoBean.saveOnNotExist();
	}

}
