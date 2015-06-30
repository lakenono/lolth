package lolth.zhaopin;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.zhaopin.bean.EmploymentadvertiseBean;

import org.apache.commons.lang3.StringUtils;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

public class BaiduBBSSearchTopicFetch extends DistributedParser{

	private ArticleTextExtractor extractor = new ArticleTextExtractor();
	
	@Override
	public String getQueueName() {
		return "baidu_search_bbs_topic";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		//正文抽取
		JResult content = extractor.extractContent(result);
		EmploymentadvertiseBean bean = new EmploymentadvertiseBean();
		bean.setUrl(task.getUrl());
		bean.setProjectName(task.getProjectName());
		bean.setKeyword(task.getExtra());
		//正文抽取
		bean.setText(content.getText());
		bean.setTitle(content.getTitle());
		
		bean.persistOnNotExist();
	}

}
