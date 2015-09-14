package lolthx.lefeng.comment;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lolthx.lefeng.search.LeFengSearchProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeFengCommentProducer extends Producer {

	private String tefeng_comment_url = "http://review.lefeng.com/review/{0}-{1}.html";

	private String catStr;

	public LeFengCommentProducer(String projectName, String catStr) {
		super(projectName);
		this.catStr = catStr;
	}

	@Override
	public String getQueueName() {
		return "lefeng_item_comment";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements divs = document.select("div.manu > a");
		int page = 0;
		if (divs.isEmpty()) {
			Elements di = document.select("div.photo.ClearFix");
			if (di.isEmpty()) {
				log.error("lefeng commont page empty : " + divs);
			} else {
				page = 1;
			}
		} else {
			String pageStr = divs.get(divs.size() - 2).text();
			if (StringUtils.isNumeric(pageStr)) {
				page = Integer.parseInt(pageStr);
			} else {
				log.error("lefeng commont page error : " + divs);
			}
		}
		log.info("lefeng commont max page : " + page);
		return page;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(tefeng_comment_url, catStr, String.valueOf(pageNum));
	}
	

	public static void main(String[] args) {
		// 179967-0-0-a 全部评论
		// 179967-1-0-g 好评
		// 179967-0-0-m 中评
		// 179967-0-0-l 差评
		// 179967-0-1-p 口碑
	}

}
