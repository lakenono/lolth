package lolth.xcar.bbs.old;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.xcar.bbs.bean.XCarBBSPostBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XCarBBSPostTextFetch
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws SQLException, IOException, InterruptedException
	{
		while (true)
		{
			//String todosql = "select url from " + BaseBean.getTableName(XCarBBSPostBean.class) + " where postTime >= '2014-12-01' and text is null";
			String todosql = "select url from " + BaseBean.getTableName(XCarBBSPostBean.class) + " where text is null";

			List<String> urls = GlobalComponents.db.getRunner().query(todosql, new ColumnListHandler<String>());

			for (String url : urls)
			{
				try
				{
					this.process(url);
				}
				catch (Exception e)
				{
					this.log.error("", e);
				}
			}
		}
	}

	private void process(String url) throws IOException, InterruptedException, SQLException
	{
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		if (StringUtils.contains(html, "抱歉，您无法进行当前操作，可能的原因是"))
		{
			this.log.info("帖子无法查看..");
			return;
		}

		String text = document.select("table.t_msg tbody tr td.line").first().text();

		XCarBBSPostBean bean = new XCarBBSPostBean();
		bean.setText(text);
		bean.setUrl(url);
		bean.update();
	}

	public static void main(String[] args) throws SQLException, IOException, InterruptedException
	{
		new XCarBBSPostTextFetch().run();
	}
}
