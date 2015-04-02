package lolth.weixin.sogou.task;

import org.jsoup.nodes.Document;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.weixin.sogou.bean.WeiXinBean;

public class WeiXinTextTaskHandler extends PageParseFetchTaskHandler
{
	public static void main(String[] args)
	{
		WeiXinTextTaskHandler handler = new WeiXinTextTaskHandler("weixin-text");
		handler.run();
	}

	public WeiXinTextTaskHandler(String taskQueueName)
	{
		super(taskQueueName);
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception
	{
		String text = doc.text();

		GlobalComponents.db.getRunner().update("update " + BaseBean.getTableName(WeiXinBean.class) + " set text=? where url=?", text, task.getUrl());
	}

}
