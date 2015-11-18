package lolthx.hers.fetch;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.DistributedParser;
import lakenono.base.Task;

public class HersCommenBbsFetch extends DistributedParser {
	HersFetch hers = new HersFetch();
	@Override
	public String getQueueName() {
		return HersFetch.COMMEN_QUEUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		Document doc = Jsoup.parse(result);
		hers.extactComment(doc, task,task.getExtra());
	}
}
