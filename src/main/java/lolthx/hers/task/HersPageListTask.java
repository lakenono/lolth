package lolthx.hers.task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
/**
 * 创建HersSearchTask 要工程名和搜索关键字
 * @author yanghp
 *
 */
public class HersPageListTask extends DistributedParser {
	public HersSearchTask hers = new HersSearchTask("cehi", "面膜");
	
	@Override
	public String getQueueName() {
		return HersSearchTask.QUEUE_PAGE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		Document doc = Jsoup.parse(result);
		hers.sendHersBbs(doc);
	}
}
