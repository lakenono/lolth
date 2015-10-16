package lolthx.baidu.visualize;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.baidu.visualize.bean.BaiduNewsVisBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.jetwick.snacktory.ArticleTextExtractor;

@Slf4j
public class BaiduNewsVisListFetch extends DistributedParser{

	@Override
	public String getQueueName() {
		return "baidu_news_vis_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		Elements elements = doc.select("div.result[id]");

		for (Element element : elements) {
			try {
				BaiduNewsVisBean bean = new BaiduNewsVisBean();

				// title
				String title = element.select("h3.c-title a").first().text();
				bean.setTitle(title);

				// url
				String url = element.select("h3.c-title a").attr("href");

				bean.setId(url);
				
				// author
				String author = element.select("p.c-author").first().text();
				author = StringUtils.substringBefore(author, "  ");
				bean.setAuthor(author);

				// post time
				String postTime = element.select("p.c-author").first().text();
				postTime = StringUtils.substringAfter(postTime, "  ");
				bean.setPostTime(postTime);

				
				String text = element.select("div.c-summary.c-row").first().text();
				bean.setText(text);

				// keyword
				String[] args = task.getExtra().split(":");
				bean.setCity(args[0]);
				bean.setKeyword(args[1]);
				bean.setProjectName(task.getProjectName());
				
				bean.saveOnNotExist();
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	public static void main(String args[]){
		for(int i = 1 ;i <=17000 ;i++ ){
			new BaiduNewsVisListFetch().run();
		}
	}
}
