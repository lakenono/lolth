package lolth.autohome.bbs;

import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.autohome.bbs.bean.AutoHomeBBSBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tiezi
{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws Exception
	{
		while (true)
		{
			List<String> todo = GlobalComponents.db.getRunner().query("select url from sns_autohome_bbs where postTime >= '2014-12-01' and views is null limit 1000", new ColumnListHandler<String>());
			//			List<String> todo = GlobalComponents.db.getRunner().query("select url from sns_autohome_bbs where views is null limit 1000", new ColumnListHandler<String>());

			for (String url : todo)
			{
				try
				{
					String html = GlobalComponents.fetcher.fetch(url);
					AutoHomeBBSBean bean = this.parse(html);
					bean.setUrl(url);
					bean.update();
				}
				catch (Exception e)
				{
					this.log.error("", e);
				}

			}

		}
	}

	public AutoHomeBBSBean parse(String html)
	{
		Document document = Jsoup.parse(html);

		AutoHomeBBSBean bean = new AutoHomeBBSBean();

		// views
		String views = document.select("font#x-views").first().text();
		bean.setViews(views);

		// replys
		String replys = document.select("font#x-replys").first().text();
		bean.setReplys(replys);

		// text
		String text = document.select("div.rconten div.conttxt").first().text();
		{
			Elements elements = document.select("div.rconten div.conttxt img");
			for (Element element : elements)
			{
				String attr = element.attr("src");
				text = text + " " + attr;
			}
		}
		bean.setText(text);

		return bean;
	}

	public static void main(String[] args) throws Exception
	{
		new Tiezi().run();
	}
}
