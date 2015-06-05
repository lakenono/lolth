package lolth.pcbaby.bbs;

import org.apache.commons.lang3.StringUtils;

import lakenono.base.DistributedParser;
import lakenono.base.Task;

public class PcBabyBBSTopicReplyFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "pcbaby_bbs_topic_reply";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		
		String url = task.getUrl();
		
	}

}
