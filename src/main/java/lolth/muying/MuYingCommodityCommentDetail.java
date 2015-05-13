package lolth.muying;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.muying.bean.CommentBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class MuYingCommodityCommentDetail extends PageParseFetchTaskHandler {

	private static final String MARK_SUB = "star s";
	private static final String MASS_SUB = ": ";

	public MuYingCommodityCommentDetail(String taskQueueName) {
		super(taskQueueName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		String url = task.getUrl();
		if (StringUtils.isBlank(url)) {
			return;
		}

		Elements elements = doc.select("body > div");

		if (elements.isEmpty()) {
			return;
		}
		String commodityId = task.getExtra();
		List<CommentBean> beans = new ArrayList<>();
		Element element = null;
		Elements select = null;
		CommentBean bean = null;

		for (int i = 0, n = elements.size(); i < n; i++) {
			bean = new CommentBean();
			bean.setCommodityId(commodityId);
			element = elements.get(i);
			// 评论ID
			bean.setCommentId(element.attr("data-id"));
			// 评论昵称
			select = element.select("span.name > a");
			bean.setCommentNike(select.text());
			// 评论时间
			select = element.select("span.date");
			bean.setCommentTime(select.text());
			// 评论评分
			select = element.select("span.star.s5");
			String mark = select.attr("class");
			if (mark.indexOf(MARK_SUB) > -1) {
				mark = mark.substring(MARK_SUB.length(), mark.length());
			} else {
				mark = "";
			}
			bean.setMark(mark);
			// 评论质量
			select = element.select("span.see > em");
			String mass = select.text();
			if (mass.indexOf(MASS_SUB) > -1) {
				mass = mass.substring(MASS_SUB.length() + 1, mass.length());
			} else {
				mass = "";
			}
			bean.setCommentMass(mass.trim());
			// 评论内容
			select = element.select("span.text");
			bean.setCommentText(select.text());
			beans.add(bean);
			log.debug(bean.toString());
		}

//		if (beans.isEmpty()) {
//			return;
//		}
//
//		for (CommentBean commentBean : beans) {
//			if (!commentBean.exist()) {
//				commentBean.persist();
//			}
//		}
		// ///////////////
		beans.clear();
		beans = null;

	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.fetcher.document(task.getUrl());
		parsePage(doc, task);
	}

	public static void main(String[] args) {
		String taskQueueName = MuYingCommodityDetailFetch.MUYING_SHOP_DETAIL_COMMENT;
		MuYingCommodityCommentDetail commodityCommentDetail = new MuYingCommodityCommentDetail(taskQueueName);
		commodityCommentDetail.setSleep(5000);
		commodityCommentDetail.run();
	}

}
