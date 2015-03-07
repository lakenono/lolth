package lolth.xcar.bbs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;
import lolth.xcar.bbs.bean.XCarBBSPostBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
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
			String todosql = "select url from " + BaseBean.getTableName(XCarBBSPostBean.class) + " where postTime >= '2014-12-01' and text is null";
			List<String> urls = GlobalComponents.db.getRunner().query(todosql, new ColumnListHandler<String>());

			for (String url : urls)
			{
				try
				{
					this.process(url);
				}
				catch (Exception e)
				{
					this.log.error("",e);
				}
			}
		}
	}

	private void process(String url) throws IOException, InterruptedException, SQLException
	{
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);
		String text = document.select("div.F_box_2 table.t_row tbody tr td table.t_msg tbody tr").get(1).text();

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
