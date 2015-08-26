package dmp.ec.jd;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECJdListFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "ec_dmp_jd_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		if ("1".equals(task.getExtra())) {
			Elements lis = doc.select("li.gl-item");
			Task newTask = null;
			for (Element li : lis) {
				String href = li.select("div.p-name a").attr("href");
				newTask = buildTask(href, "ec_dmp_jd_item", task);
				Queue.push(newTask);
			}
		} else if ("0".equals(task.getExtra())) {
			Elements lis = doc.select("ul.list-h li");
			Task newTask = null;
			for (Element li : lis) {
				String id = li.attr("sku");
				if (StringUtils.isBlank(id)) {
					continue;
				}
				String href = li.select("div.p-name a").attr("href");
				newTask = buildTask(href, "ec_dmp_jd_item", task);
				Queue.push(newTask);
			}

		} else {
			log.error("jd list push error:type is " + task.getExtra());
		}
	}

}
