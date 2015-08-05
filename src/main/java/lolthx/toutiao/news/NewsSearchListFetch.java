package lolthx.toutiao.news;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.toutiao.news.bean.TouTiaoNewsBean;

import org.apache.commons.lang3.StringUtils;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

public class NewsSearchListFetch extends DistributedParser {

	private ArticleTextExtractor extractor = new ArticleTextExtractor();

	@Override
	public String getQueueName() {
		return "toutiao_news_search_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		String url = task.getUrl();
		TouTiaoNewsBean newsBean = new TouTiaoNewsBean();
		//正文抽取
		JResult content = extractor.extractContent(result);
		newsBean.setSeoUrl(url);
		newsBean.setText(content.getText()!=null?content.getText().trim():content.getText());
		//更新数据库数据
		newsBean.update();
	}
}
