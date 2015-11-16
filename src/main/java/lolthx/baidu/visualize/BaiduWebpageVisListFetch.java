package lolthx.baidu.visualize;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.baidu.visualize.bean.BaiduWebpageVisBean;
import lolthx.baidu.webpage.BaiduWebSiteFetch;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class BaiduWebpageVisListFetch extends DistributedParser{
	
	@Override
	public String getQueueName() {
		return "baidu_webpage_vis_list";
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
				BaiduWebpageVisBean bean = new BaiduWebpageVisBean();

				// title
				String title = element.select("h3.t a").first().text();
				bean.setTitle(title);

				// url
				String url = element.select("h3.t a").attr("href");

				bean.setId(url);

				// baidu cache url
				String text = element.select("div.c-abstract").first().text();
				bean.setText(text);

				// keyword
				String[] args = task.getExtra().split(":");
				bean.setCity(args[0]);
				bean.setKeyword(args[1]);
				bean.setProjectName(task.getProjectName());
				
				bean.saveOnNotExist();
				
			} catch (Exception e) {
				log.error("handle baidu webpage vis list error : {}",e.getMessage(),e,task.getExtra());
				continue;
			}
		}
	}

	public static void main(String[] args){
		for(int i = 1 ; i <= 304 ; i++){
			new BaiduWebpageVisListFetch().run();
		}
	}
	
}
