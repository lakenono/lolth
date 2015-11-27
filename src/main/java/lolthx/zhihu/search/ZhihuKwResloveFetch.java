package lolthx.zhihu.search;

import java.text.MessageFormat;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZhihuKwResloveFetch  extends DistributedParser{
	
	private static String COMMENT_URL = "http://www.zhihu.com/node/QuestionAnswerListV2&questionId={0}&offset={1}";
	
	@Override
	public String getQueueName() {
		return "zhihu_kw_reslove";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		
		String extra = task.getExtra();
		
		Elements els = doc.select("div.box-result div.result-about-list");
		for(Element el : els){
			
			String detailUrl = el.select("h4.about-list-title a").attr("href");
			String count = el.select("p.about-answer span.count").text();
			String extras = extra + ":" + count;
			task.setExtra(extras);
			String detailqueue= "zhihu_kw_question";
			Task detailTask = buildTask(detailUrl, detailqueue , task);
			Queue.push(detailTask);
			
			String userUrl = el.select("p.about-answer a").attr("href");
			String userquene = "zhihu_kw_user";
			Task userTask = buildTask(userUrl, userquene , task);
			Queue.push(userTask);
			
			String questionId = StringUtils.substringAfter(detailUrl, "question/");
			String nums = el.select("div.about-lable span").text();
			String num = StringUtils.substringBefore(nums, "个回答");
			String extrasi = extra + ":" + questionId;
			task.setExtra(extrasi);
			int pagei = Integer.valueOf(num) / 50;
			for(int i = 1 ; i <=pagei ; i++){
				String url = this.buildUrl(questionId, i);
				String answersquene = "zhihu_kw_answers";
				Task answersTask = buildTask(url, answersquene , task);
				Queue.push(answersTask);
			}
		}
	}
	
	public String buildUrl(String questionId ,int i){
		return MessageFormat.format(COMMENT_URL, questionId, String.valueOf(i * 50));
	}
	
	@Override
	protected String getCookieDomain() {
		return "zhihu.com";
	}
	
	public static void main(String[] args){
		new ZhihuKwResloveFetch().run();
	}
	
}
