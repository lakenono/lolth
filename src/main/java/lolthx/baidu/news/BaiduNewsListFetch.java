package lolthx.baidu.news;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;
import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.baidu.news.bean.BaiduNewsBean;

public class BaiduNewsListFetch extends DistributedParser {

	private ArticleTextExtractor extractor = new ArticleTextExtractor();

	@Override
	public String getQueueName() {
		return "baidu_news_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		Elements elements = doc.select("div.result[id]");
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>elementsSize" + elements.size());
		
		for (Element element : elements) {
			try {
				BaiduNewsBean bean = new BaiduNewsBean();

				// title
				String title = element.select("h3.c-title a").first().text();
				bean.setTitle(title);

				// url
				String url = element.select("h3.c-title a").attr("href");
				bean.setUrl(url);
				System.out.println(url);
				bean.setId(url);
				
				// author
				String author = element.select("p.c-author").first().text();
				author = StringUtils.substringBefore(author, "  ");
				bean.setAuthor(author);

				// post time
				String postTime = element.select("p.c-author").first().text();
				postTime = StringUtils.substringAfter(postTime, "  ");
				bean.setPostTime(postTime);

				if (element.select("a.c-more_link").size() != 0) {
					// more
					String more = element.select("a.c-more_link").first().text();
					more = StringUtils.substringBefore(more, "条相同新闻");
					bean.setMore(more);

					// more link
					String moreLink = element.select("a.c-more_link").first().attr("href");
					bean.setMoreLink("http://news.baidu.com" + moreLink);
				}

				// baidu cache url
				String baiduCacheUrl = element.select("a.c-cache").first().attr("href");
				bean.setBaiduCacheUrl(baiduCacheUrl);
				
				String cacheHtml = GlobalComponents.fetcher.fetch(baiduCacheUrl);
				JResult res = this.extractor.extractContent(cacheHtml);
				bean.setText(res.getText());

				// keyword
				bean.setKeyword(task.getExtra());
				bean.setProjectName(task.getProjectName());
				
				// status
				bean.setStatus("success");

				bean.saveOnNotExist();
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	public static void main(String args[]){
		for(int i = 1 ;i <=5;i++){
			new BaiduNewsListFetch().run();
		}
	}
	
	
}
