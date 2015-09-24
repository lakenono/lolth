package lolthx.lefeng.consultation;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.core.GlobalComponents;
import lolthx.lefeng.comment.LeFengCommentProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeFengConsultationProducer extends Producer{
	
	private String lefeng_consultation_url = "http://product.lefeng.com/goods/{0}-{1}.html";

	private String catStr;
	
	public LeFengConsultationProducer(String projectName,String catStr) {
		super(projectName);
		this.catStr = catStr;
	}

	@Override
	public String getQueueName() {
		return "lefeng_item_consultation";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements divs = document.select("div.manu > a");
		int page = 0;
		if (divs.isEmpty()) {
			Elements di = document.select("div.spzx > div.wen");
			if (di.isEmpty()) {
				log.error("lefeng consult page empty : " + divs);
			} else {
				page = 1;
			}
		} else {
			String pageStr = divs.get(divs.size() - 2).text();
			if (StringUtils.isNumeric(pageStr)) {
				page = Integer.parseInt(pageStr);
			} else {
				log.error("lefeng consult page error : " + divs);
			}
		}
		log.info("lefeng consult max page : " + page);
		return page;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(lefeng_consultation_url, catStr, String.valueOf(pageNum));
	}
	
	public static void main(String[] args) {
		//187217-0-0-all  全部咨询
		//187217-0-0-zx   商品咨询
		//187217-0-0-ps   配送支付
		//187217-0-0-sh   售后服务
		
		
	}

}
