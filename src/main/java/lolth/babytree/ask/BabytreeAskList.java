package lolth.babytree.ask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.babytree.ask.bean.AskBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 
public class BabytreeAskList {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private String keyword;
	private String cid;

	public BabytreeAskList(String keyword, String cid) {
		super();
		this.keyword = keyword;
		this.cid = cid;
	}

	public void run() throws Exception {
		int maxPage = 0;

		try {
			maxPage = this.getMaxPage();
		} catch (Exception e) {
			this.log.error("", e);
		}

		this.log.info("{} job size 0/{}", this.keyword, maxPage);

		for (int i = 0; i < maxPage; i++) {
			this.log.info("{} job runing... {}/{}", this.keyword, i + 1, maxPage);

			String url = this.buildUrl(keyword, cid, i);
			String html = GlobalComponents.fetcher.fetch(url);
			List<AskBean> asks = this.parse(html);

			for (AskBean bean : asks) {
				try {
					bean.persist();
				} catch (Exception e) {
					this.log.error("存储失败", e);
				}
			}
		}
	}

	public List<AskBean> parse(String html) {
		List<AskBean> result = new LinkedList<AskBean>();

		Document document = Jsoup.parse(html);
		Elements asks = document.select("div.search_item_area div.search_item");

		for (Element element : asks) {
			try {
				AskBean bean = new AskBean();

				// url
				String url = element.select("div.search_item_tit a").first().attr("href");
				bean.setUrl(url);

				// title
				String title = element.select("span.search_cor_blue").first().text();
				bean.setTitle(title);

				// cid
				bean.setCid(this.cid);

				// date
				String date = element.select("span.search_date").first().text();
				bean.setDate(date);

				bean.setKeyword(this.keyword);

				this.log.debug(bean.toString());
				result.add(bean);
			} catch (Exception e) {
				this.log.error("{}", element.html(), e);
			}
		}
		return result;
	}

	public int getMaxPage() throws Exception {
		String url = this.buildUrl(keyword, cid, 1);
		String html = GlobalComponents.fetcher.fetch(url);

		Document document = Jsoup.parse(html);
		String ownText = document.select("span.page-number").first().ownText();

		String page = StringUtils.substringBetween(ownText, "共", "页");

		return Integer.parseInt(page);
	}

	// 准备怀孕
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=1&range=&pg=1
	// 怀孕期
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=2&range=&pg=1
	// 婴儿期0-1岁
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=3&range=&pg=2
	// 幼儿期1-3岁
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=4&range=&pg=2
	// 学龄前3-6岁
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=5&range=&pg=2
	// 情感家庭
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=477&range=&pg=2
	// 生活消费
	// http://www.babytree.com/s.php?q=%E8%8A%B1%E7%8E%8B&c=ask&cid=488&range=&pg=2
	public String buildUrl(String keyword, String cid, int page) throws UnsupportedEncodingException {
		String baseUrl = "http://www.babytree.com/s.php?q={0}&c=ask&cid={1}&range=&pg={2}";
		String url = MessageFormat.format(baseUrl, URLEncoder.encode(keyword, "UTF-8"), cid, page + 1 + "");
		return url;
	}

	public static void main(String[] args) throws Exception {
		String[] cids = new String[] { "1", "2", "3", "4", "5", "477", "488" };
//		String[] brands = new String[] { "美素佳儿", "惠氏", "多美滋", "雅培", "诺优能", "美赞臣", "可瑞康" };
		// String[] brands = new String[] { "诺优能", "美赞臣", "可瑞康" };
		String[] brands = new String[] {"惠氏启赋","wyeth启赋","雅培菁致","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒"};
		for (String brand : brands) {
			for (String cid : cids) {
				new BabytreeAskList(brand, cid).run();
			}
		}

		// new AskList("帮宝适", "1").run();
		// new AskList("帮宝适", "1").run();
		// new AskList("帮宝适", "2").run();
		// new AskList("帮宝适", "3").run();
		// new AskList("帮宝适", "4").run();
		// new AskList("帮宝适", "5").run();
		// new AskList("帮宝适", "477").run();
		// new AskList("帮宝适", "488").run();
		//
		// new AskList("花王", "1").run();
		// new AskList("花王", "2").run();
		// new AskList("花王", "3").run();
		// new AskList("花王", "4").run();
		// new AskList("花王", "5").run();
		// new AskList("花王", "477").run();
		// new AskList("花王", "488").run();
		//
		// new AskList("好奇", "1").run();
		// new AskList("好奇", "2").run();
		// new AskList("好奇", "3").run();
		// new AskList("好奇", "4").run();
		// new AskList("好奇", "5").run();
		// new AskList("好奇", "477").run();
		// new AskList("好奇", "488").run();
		//
		// new AskList("妈咪宝贝", "1").run();
		// new AskList("妈咪宝贝", "2").run();
		// new AskList("妈咪宝贝", "3").run();
		// new AskList("妈咪宝贝", "4").run();
		// new AskList("妈咪宝贝", "5").run();
		// new AskList("妈咪宝贝", "477").run();
		// new AskList("妈咪宝贝", "488").run();

	}
}
