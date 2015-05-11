package lolth.muying;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.muying.bean.CommentBean;

public class MuYingCommodityCommentDetail extends PageParseFetchTaskHandler{
	

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
		if(elements.size() <= 0){
			return;
		}
		List<CommentBean> beans = new ArrayList<>();
		Element element=null;
		Elements select =null;
		CommentBean bean = null;
		for(int i = 0,n=elements.size();i<n;i++){
			bean = new CommentBean();
			bean.setCommodityId(task.getExtra());
			bean.setKeyword(task.getName());
			element = elements.get(i);
			//评论ID
			bean.setCommentId(element.attr("data-id"));
			//评论昵称
			select = element.select("dl > dt > span.name > a");
			bean.setCommentNike(select.text());
			//评论时间
			select = element.select("dl > dt > span.date");
			bean.setCommentTime(select.text());
			//评论评分
			select = element.select("dl > dd:nth-child(2) > span.star.s5");
			String mark = select.attr("class");
			bean.setMark(mark);
			//评论质量
			select = element.select("dl > dd:nth-child(2) > span.see > em");
			bean.setCommentMass(select.text());
			//评论内容
			select = element.select("dl > dd:nth-child(3) > span.text");
			bean.setCommentText(select.text());
			System.out.println(bean.toString());
			beans.add(bean);
		}
		
		if(beans.isEmpty()){
			return;
		}
		
		for(CommentBean commentBean: beans){
			if(!commentBean.exist()){
				commentBean.persist();
			}
		}
		/////////////////
		beans.clear();
		beans = null;
		
	}

	@Override
	protected void handleTask(FetchTask task) throws IOException, InterruptedException, Exception {
		Document doc = GlobalComponents.fetcher.document(task.getUrl());
		parsePage(doc, task);
	}
	
	

}
