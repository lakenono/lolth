package dmp.ec.amazon;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import dmp.ec.ECBean;
import lakenono.base.DistributedParser;
import lakenono.base.Task;

public class AmazonGoodsFetch extends DistributedParser {

	public void parse(String result, Task task) throws Exception {

		Document doc = Jsoup.parse(result);
		Elements items = doc.select("li.s-result-item");
		for (Element item : items) {

			ECBean bean = new ECBean("amazon");
			// 商品id
			String goodsId = item.attr("data-asin");

			if (StringUtils.isBlank(goodsId)) {
				continue;
			}

			bean.setId(goodsId);

			// 商品title
			Elements title = item.select("h2.a-size-base");
			String goodsTitle = title.first().text();
			bean.setTitle(goodsTitle);

			// 商品url
			Elements h5 = item.select("h5");
			Elements url = item.select("a.a-size-small");
			if (!h5.isEmpty() && !url.isEmpty()) {
				String goodsUrl = url.first().absUrl("href");
				bean.setUrl(goodsUrl);
			} else {
				Elements normalurl = item.select("a.a-link-normal");
				String goodsUrl = normalurl.first().absUrl("href");
				bean.setUrl(goodsUrl);
			}
			bean.setKeyword(task.getExtra());
			bean.saveOnNotExist();

			// Elements price = item.select("span.a-size-base");
			// System.out.println(StringUtils.replace(price.first().text(), "￥",
			// ""));
			//
			// Elements nameinfo =
			// item.select("div.a-row").select("span.a-size-small");
			// System.out.println(StringUtils.substring(nameinfo.text(), 0,
			// StringUtils.indexOf(nameinfo.text(), "￥") - 1));

		}

	}

	@Override
	public String getQueueName() {
		// TODO Auto-generated method stub
		return AmazonSearchProducer.QUEUENAME;
	}
	
	public static void main(String[] args) {
		AmazonGoodsFetch a = new AmazonGoodsFetch();
		a.run();
	}


}
