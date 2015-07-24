package lolth.yhd.comment.fetch;

import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolth.yhd.comment.bean.YHDCommentBean;
import lolth.yhd.search.fetch.YHDGoodsFetch;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 一号店评论爬取
 * 抓取类型default_fetch
 * @author yanghp
 *
 */
public class YHDCommentFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		
		return YHDGoodsFetch.COMQUE;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}
		List<YHDCommentBean> list = new ArrayList<>();
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("div.item dl");
		if(elements.size()> 0){
			for (Element element : elements) {
				YHDCommentBean bean = new YHDCommentBean();
				String score = element.select("dt span.star").first().attr("class");
				String text = element.select("span.text").text();
				String url = element.select("span a").attr("href");
				String id = StringUtils.substringBetween(url, "review/", ".");
				String date = element.select("span.date").text();
				String repley = element.select("div.up").text();
				bean.setCommentId(id);
				bean.setContent(text);
				bean.setDate(date);
				bean.setGoodsId(task.getProjectName());
				bean.setRepley(repley);
				bean.setScore(score);
				list.add(bean);
			}
			if (list.isEmpty()) {
				return;
			}
			for (YHDCommentBean b : list) {
				if (!b.exist()) {
					b.persist();
				}
			}
			list.clear();
		}
	}
	
}
