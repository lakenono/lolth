package lolth.weixin.sogou.task;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.task.FetchTask;
import lakenono.task.ListFetchTaskProducer;
import lolth.weixin.sogou.bean.WeiXinBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class WeiXinTextProducer extends ListFetchTaskProducer<String>
{
	public static void main(String[] args) throws Exception
	{
		WeiXinTextProducer producer = new WeiXinTextProducer("weixin-text");
		producer.run();
	}

	public WeiXinTextProducer(String taskQueueName)
	{
		super(taskQueueName);
	}

	@Override
	protected FetchTask buildTask(String url)
	{
		FetchTask task = new FetchTask();
		task.setName("weixin-text");
		task.setBatchName("weixin-text");
		task.setUrl(url);
		return task;
	}

	@Override
	protected List<String> getTaskArgs() throws Exception
	{
		List<String> urls = GlobalComponents.db.getRunner().query("SELECT url FROM " + BaseBean.getTableName(WeiXinBean.class) + " WHERE url IS NOT NULL AND text IS NULL", new ColumnListHandler<String>());
		return urls;
	}

}
