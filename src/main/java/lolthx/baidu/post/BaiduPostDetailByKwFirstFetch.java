package lolthx.baidu.post;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.baidu.visualize.BaiduNewsVisListFetch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduPostDetailByKwFirstFetch extends DistributedParser{

	private static final String BAIDU_POST_DETAIL_URL_PREFIX = "http://tieba.baidu.com";
	
	@Override
	public String getQueueName() {
		return "baidu_tieba_list_bykw_first";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		Document doc = Jsoup.parse(result);

		Set<String> urlSet = new LinkedHashSet<>();

		Elements elements = doc.select("div.s_post");
		
		for (Element element : elements) {
			try {
				String urlFirst = element.select("span.p_title a.bluelink").attr("href");
				
				String url = BAIDU_POST_DETAIL_URL_PREFIX + StringUtils.substringBefore(urlFirst, "?") + "?pn=1";
				
				Task newTask = buildTask(url, "baidu_post_bykw_second", task);
				Queue.push(newTask);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("handle baidu post kw first error : {}",e.getMessage(),e,task.getExtra());
				continue;
			}		
			
		}
		
	}
	
	public static void main(String args[]){
		for(int i = 1 ; i <= 1000 ; i++){
			new BaiduPostDetailByKwFirstFetch().run();
		}
	}
	
	
}
