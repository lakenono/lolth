package lolthx.bitauto.bbs;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolthx.bitauto.bean.BitautoBBSBean;
import lolthx.bitauto.bean.BitautoBBSUserBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitautoBBSListFetch extends DistributedParser {

	private static Date start = null;// 开始时间
	private static Date end = null;// 结束时间

	static {
		try {
			start = DateUtils.parseDate("2015-02-28", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			end = DateUtils.parseDate("2015-07-28", "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getQueueName() {
		return "bitauto_bbs_list";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);
		BitautoBBSBean bean = null;
		Elements elements = doc.select("div.postscontent>div.postslist_xh");

		System.out.println("elements Size = " + elements.size());

		// 循环列表元素
		for (Element element : elements) {
			try {
				// 发帖时间
				String postTime = element.select("li.zhhf").first().text();

				if (!isTime(postTime)) {
					continue;
				}

				bean = new BitautoBBSBean();
				bean.setPostTime(postTime);

				// url
				String url = element.select("li.bt a").attr("href");
				bean.setUrl(url);

				// title
				String title = element.select("li.bt a span").text().trim();
				bean.setTitle(title);

				// 类型，是否为精品帖子 等
				String type = element.select("li.tu a").attr("class");
				bean.setType(type);

				// 是否必须带thread？主ID StringUtils.substringBefore(task.getExtra(),
				// ":")
				String id = StringUtils.substringBetween(url, "-", ".html");
				bean.setId(id);

				// 作者
				String author = element.select("li.zz a").text().trim();
				bean.setAuthor(author);

				// 作者url
				String authorUrl = element.select("li.zz a").attr("href");
				String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");

				bean.setAuthorId(authorId);
				bean.setProjectName(task.getProjectName());
				bean.setForumId(StringUtils.substringBefore(task.getExtra(), ":"));
				bean.setKeyword(StringUtils.substringAfter(task.getExtra(), ":"));

				String html = GlobalComponents.fetcher.fetch(url);
				Document docDetail = Jsoup.parse(html);

				// views 点击 and 回复
				String views_replys = docDetail.select("div.title_box span").text();

				// 点击率
				String views = StringUtils.substringAfter(views_replys, "/");
				bean.setViews(views);

				// 回复率
				String replys = StringUtils.substringBefore(views_replys, "/");
				bean.setReplys(replys);

				// System.out.println();

				String text = docDetail.select("div.post_text_sl div.post_width").first().text();

				{
					Elements detailEls = docDetail.select("div.post_text_sl div.post_width img");
					for (Element detailEl : detailEls) {
						String attr = detailEl.attr("src");
						text = text + " " + attr;
					}
				}

				// 车主信息
				bean.setText(text);

				bean.saveOnNotExist();
				String sendurl = StringUtils.replace(bean.getUrl(), ".html", "-{0}.html");
				int maxpage = this.getMaxPage(docDetail);// 执行页面用户评论推送
				for (int pagenum = 1; pagenum <= maxpage; pagenum++) {
					String seUrl = buildUrl(sendurl, pagenum);
					Task newTask = buildTask(seUrl, "bitauto_bbs_comment", task);
					Queue.push(newTask);
				}

				parseUser(docDetail);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

	}

	private boolean isTime(String time) {
		try {
			Date srcDate = DateUtils.parseDate(time.trim(), "yyyy-MM-dd");
			return between(start, end, srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	// 存储用户信息
	private void parseUser(Document doc) {
		// 获取左上角用户信息
		Element topicElement = doc.select("div#postleft1").first();
		BitautoBBSUserBean bean = new BitautoBBSUserBean();

		// 获取用户名称
		String name = topicElement.select("div.user_name a").text();
		bean.setName(name);

		// 用户url
		String authorUrl = topicElement.select("div.user_name a").attr("href");
		bean.setUrl(authorUrl);

		// 用户id
		String authorId = StringUtils.substringBetween(authorUrl, "http://i.yiche.com/", "/");
		bean.setId(authorId);

		Elements lis = doc.select("div#postleft1 div.user_info li");

		for (Element li : lis) {
			String data = li.text();
			String dataTitle = StringUtils.substringBefore(data, "：");

			if (dataTitle.equals("等 级")) {
				String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
				bean.setLevel(dataResult);
			}
			if (dataTitle.equals("帖 子")) {
				String dataResult1 = StringUtils.substringAfter(data, "：");
				String dataResult2 = StringUtils.replace(dataResult1, "精华)", "");
				bean.setPosts(StringUtils.trim(StringUtils.substringBefore(dataResult2, "(")));
				bean.setElites(StringUtils.trim(StringUtils.substringAfter(dataResult2, "(")));
			}
			if (dataTitle.equals("注 册")) {
				String dataResult = StringUtils.trim(StringUtils.substringAfter(data, "："));
				bean.setRegTime(dataResult);
			}

			if (StringUtils.startsWith(data, "地 区")) {
				String dataResult = StringUtils.substringAfter(data, "：");
				String city = StringUtils.trim(StringUtils.substringAfter(dataResult, " "));
				String province = StringUtils.trim(StringUtils.substringBefore(dataResult, " "));

				bean.setCity(city);
				if (StringUtils.isNotBlank(province)) {
					bean.setProvince(province);
				} else {
					bean.setProvince(city);
				}

			}
			if (StringUtils.startsWith(data, "车 型")) {
				bean.setCar(StringUtils.trim(StringUtils.substringAfter(data, "：")));
			}
		}
		try {
			bean.saveOnNotExist();
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}
	}

	private int getMaxPage(Document doc) throws Exception {
		Elements pageEs = doc.select("div.the_pages a");
		// 没有分页标签
		if (pageEs.isEmpty() || pageEs.size() <= 2) {
			return 1;
		}

		// 有分页标签
		if (pageEs.size() >= 3) {
			String pages = pageEs.get(pageEs.size() - 2).text();
			if (StringUtils.isNumeric(pages)) {
				return Integer.parseInt(pages);
			}
		}
		return 0;
	}

	public String buildUrl(String url, int pageNum) {
		return MessageFormat.format(url, String.valueOf(pageNum));
	}

	public static void main(String[] args) throws Exception {
		new BitautoBBSListFetch().run();
	}

}
