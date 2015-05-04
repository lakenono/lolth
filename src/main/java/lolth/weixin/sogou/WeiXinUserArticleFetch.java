package lolth.weixin.sogou;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.fetch.handlers.PageFetchHandler;
import lakenono.log.BaseLog;
import lolth.weixin.sogou.bean.WeiXinBean;
import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AllArgsConstructor
public class WeiXinUserArticleFetch extends BaseLog implements PageFetchHandler
{
	public static void main(String[] args) throws Exception
	{
		while (true)
		{
			try
			{
				//				new WeiXinUserArticleFetch("奧迪", "oIWsFtw87yg5Yjhh6vFO7lr9zf_Y").run();
				//				new WeiXinUserArticleFetch("宝马中国", "oIWsFt67LTuaA8LD_QB9jj8VP26Y").run();
				//				new WeiXinUserArticleFetch("梅赛德斯-奔驰", "oIWsFt3qXWm1Nn69_1akk0zjXWe4").run();
				//				new WeiXinUserArticleFetch("706青年空间", "oIWsFtwKBBPDGq9g0zjpv3YVxaqw").run();
				//				new WeiXinUserArticleFetch("MOOC学院", "oIWsFt_cFN_RK4tz4VrEL4FEHw80").run();
				//				new WeiXinUserArticleFetch("ONE", "oIWsFtwVm9IdlPUp7LB_gVJdWZiQ").run();
				//				new WeiXinUserArticleFetch("单向街书店", "oIWsFt_1BfLG4mYh8Jdp3k3yRlAQ").run();
				//				new WeiXinUserArticleFetch("大象公会", "oIWsFt4bBmJKomRWR5Uo39-rX9mE").run();
				//				new WeiXinUserArticleFetch("硬派健身", "oIWsFt4_pQdhWaQPrBlU3E0Sq2Xo").run();
				//				new WeiXinUserArticleFetch("清华南都", "oIWsFt4niLvpL50sIWKXKCFd7I7s").run();
				//				new WeiXinUserArticleFetch("知乎日报", "oIWsFt5HJEgGlbxXAB2hBcmwjQho").run();
				//				new WeiXinUserArticleFetch("36氪", "oIWsFt-NQJJTI1l_HJBI-iEy3qbg").run();
				//				new WeiXinUserArticleFetch("爱范儿", "oIWsFt3BMAX2LWmUkqQsJtCzWi1Y").run();
				//				new WeiXinUserArticleFetch("果壳", "oIWsFt86MuAacbPGA3TM1glwaTp4").run();
				//				new WeiXinUserArticleFetch("科学松鼠会", "oIWsFt7uZh_mqfA4PSIKvp3RBp9w").run();
				//				new WeiXinUserArticleFetch("财新网", "oIWsFt5aa3mnBq5L_LEfu0066G0c").run();
				//				new WeiXinUserArticleFetch("三联生活周刊", "oIWsFt_B7kk2G1yV3QkYOAboXhFM").run();
				//				new WeiXinUserArticleFetch("澎湃新闻", "oIWsFt0I3Dwtk5Ml0KnJcf3fz_Ao").run();
				//				new WeiXinUserArticleFetch("视觉志", "oIWsFt6Tr_virVck4lVlAYIIWk3k").run();
				//				new WeiXinUserArticleFetch("普象工业设计小站", "oIWsFt5FrAiwnUG11lN5boXS6TZU").run();
				//				new WeiXinUserArticleFetch("4A广告提案网", "oIWsFt0jOCFkUAPjOy98nT9-pjjg").run();
				//				new WeiXinUserArticleFetch("创业家杂志", "oIWsFt1EsrwA3ZPumFdeXDDsN5Og").run();
				//				new WeiXinUserArticleFetch("中国国家地理", "oIWsFt1A6Sh5-NrFDwq0l-q1Hvzs").run();
				//				new WeiXinUserArticleFetch("蚂蜂窝", "oIWsFt0jb9dKd_rf0-3BueHTFhEc").run();
				//				new WeiXinUserArticleFetch("音悦台", "oIWsFtyKkfjyi_FnipxaTihkiPz0").run();
				//				new WeiXinUserArticleFetch("改变自己", "oIWsFt2UezFNv7A6nD0z1PyDTO2g").run();
				//				new WeiXinUserArticleFetch("英国那些事儿", "oIWsFtywwCsYrqK8-7vQQ_tfLphc").run();
				//				new WeiXinUserArticleFetch("日本流行每日速报", "oIWsFt2xTJ7hmFcgEKp4Xq3GenFY").run();
//				new WeiXinUserArticleFetch("虎嗅", "oIWsFt4JYI9WZ31CSvzYlMbSJiOM").run();new WeiXinUserArticleFetch("oIWsFt5qMK6KIy4h8uI3UWScmfkY","Autotong");
				new WeiXinUserArticleFetch("Autotong","oIWsFt5qMK6KIy4h8uI3UWScmfkY").run();
				new WeiXinUserArticleFetch("qiche3721","oIWsFtzzPJDIzwR-CSg2or5ssc5c").run();
				new WeiXinUserArticleFetch("qcply-2","oIWsFt-BWDNNNKg6X_KKc-YkOmqM").run();
				new WeiXinUserArticleFetch("Geekcar","oIWsFtwVPGYeiPcJd4yq57-5Ih7g").run();
				new WeiXinUserArticleFetch("wenshucheyun","oIWsFt8LFQXZTcEIGXDfnwLdLBEQ").run();
				new WeiXinUserArticleFetch("yypcar","oIWsFtyps_h7iFAuq7-L1reLisrE").run();
				new WeiXinUserArticleFetch("zhongguoqichebao","oIWsFt1e7cSS997saZkIKTv5iohY").run();
				new WeiXinUserArticleFetch("meiqingqinxue1981","oIWsFt0M8q8rkIK15RFZEf3fnan0").run();
				new WeiXinUserArticleFetch("automao","oIWsFt2uHuq06TQRjBI10PfWZ3AU").run();
				new WeiXinUserArticleFetch("auto-paiqiguan","oIWsFt3D6nzLN3_WJvIKGwXeb3UQ").run();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	private String username;
	private String openid;

	@Override
	public void run() throws IOException, InterruptedException, SQLException, Exception
	{
		try {
			int maxPage = this.getMaxPage();

			for (int i =1; i <= maxPage; i++)
			{
				String taskname = MessageFormat.format("weixin_user-{0}-{1}", this.username, i);

				if (GlobalComponents.taskService.isCompleted(taskname))
				{
					//这里应该不需要++
//				i++;
					this.log.info("task {} is completed", taskname);
					continue;
				}

				this.log.info("keyword[{}] {}/{}...", this.username, i, maxPage);

				try {
					this.process(i);
				} catch (Exception e) {
					e.printStackTrace();
				}

				GlobalComponents.taskService.success(taskname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(int i) throws Exception
	{
		String html = GlobalComponents.dynamicFetch.fetch(this.buildUrl(i));

		if (StringUtils.contains(html, "您的访问出现了一个错误"))
		{
			throw new RuntimeException("爬取失败页面.. sleep");
		}

		html = Jsoup.parse(html).text();

		html = StringUtils.substringBetween(html, "items\":[\"", "\"],\"totalItems");
		html = StringUtils.remove(html, "\\");
		html = StringUtils.remove(html, "");

		Document document = Jsoup.parse(html);

		Elements elements = document.select("document");

		for (Element element : elements)
		{
			WeiXinBean bean = new WeiXinBean();

			String title = element.select("title1").first().text();
			bean.setTitle(title);

			String url = element.select("url").first().text();
			bean.setUrl(url);

			String postTime = element.select("date").first().text();
			bean.setPostTime(postTime);

			String authorname = element.select("sourcename").first().text();
			bean.setAuthorname(authorname);

			String openid = element.select("openid").first().text();
			bean.setAuthorurl("http://weixin.sogou.com/gzh?openid=" + openid);

			bean.setAuthorid(openid);

			bean.setKeyword(this.username);

			bean.persist();
			this.log.info(bean.toString());
		}
		Thread.sleep(15000);
	}

	@Override
	public int getMaxPage() throws Exception
	{
		String url = this.buildUrl(1);
		String html = GlobalComponents.dynamicFetch.fetch(url);
		html = StringUtils.substringBetween(html, "totalPages\":", "}");
		Thread.sleep(15000);
		return Integer.parseInt(html);
	}

	@Override
	public String buildUrl(int pageNum) throws UnsupportedEncodingException
	{
		String url = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid={0}&page={1}";
		return MessageFormat.format(url, this.openid, pageNum);
	}

}
