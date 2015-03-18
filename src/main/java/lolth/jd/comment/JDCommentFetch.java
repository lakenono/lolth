package lolth.jd.comment;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.jd.comment.bean.JDGoodsCommentBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDCommentFetch {
	// 用户名
	// 用户url
	// 用户级别
	// 用户地域
	// 评价类型
	// 用户评星
	// 用户评论时间
	// 用户评论url
	// 标签
	// 心得
	// 型号
	// 购买日期

	private int sleep = 180;
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private String id;
	private String commentType;

	public JDCommentFetch(String id, String commentType) {
		super();
		this.id = id;
		this.commentType = commentType;
	}

	public void run() throws Exception {
		this.log.info("jd comments version 0.2 start...");

		int maxPage = this.getMaxPage();

		this.log.info("start 0/{}...", maxPage);

		for (int i = 0; i < maxPage; i++) {
			try {
				this.log.info("start {}/{}...", i + 1, maxPage);

				String buildUrl = this.buildUrl(id, commentType, i);
				this.log.info("fetch {}", buildUrl);
				String html = GlobalComponents.fetcher.fetch(buildUrl);
				List<JDGoodsCommentBean> comments = this.parse(html);

				for (JDGoodsCommentBean goodsComment : comments) {
					goodsComment.setType(commentType);

					try {
						goodsComment.persist();
					} catch (Exception e) {
						this.log.error("存储失败...", e);
					}
				}
			} catch (Exception e) {
				log.error("{}|{}|{} handler error!", id, commentType, i + 1, e);
			}

			this.log.info("sleep {}秒", this.sleep);
			Thread.sleep(this.sleep * 1000);
		}

		log.info("jd comment fetch finish!");
	}

	private int getMaxPage() throws Exception {
		String url = this.buildUrl(id, commentType, 0);
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		String text = document.select("li.curr[scoe=" + commentType + "]").first().text();
		String pageString = StringUtils.substringBetween(text, "(", ")");

		int parseInt = Integer.parseInt(pageString);
		
		//少处理最后一页逻辑
		int maxPage = parseInt / 30;
		if(parseInt%30!=0){
			maxPage++;
		}
		return maxPage;
	}

	public List<JDGoodsCommentBean> parse(String html) {
		List<JDGoodsCommentBean> comments = new LinkedList<JDGoodsCommentBean>();

		Document document = Jsoup.parse(html);
		Elements elements = document.select("div#comments-list.m div.mc");

		for (Element element : elements) {
			try {
				// 隐藏ajax评论
				if (element.className().equals("mc hide")) {
					continue;
				}

				JDGoodsCommentBean comment = new JDGoodsCommentBean();

				// 用户匿名评价..
				if (element.select("div.u-name a").size() != 0) {
					String username = element.select("div.u-name a").first().ownText();
					comment.setUsername(username);

					String userUrl = element.select("div.u-name a").first().attr("href");
					comment.setUserUrl(userUrl);

					String userLevel = element.select("span.u-level span").first().ownText();
					comment.setUserLevel(userLevel);

					String area = element.select("span.u-address").first().ownText();
					comment.setArea(area);
				} else {
					String username = element.select("div.u-name").first().ownText();
					comment.setUsername(username);
					comment.setUserUrl("none");
					comment.setUserLevel("none");
					comment.setArea("none");
				}

				String star = element.select("span.star").first().className();
				comment.setStar(star);

				String time = element.select("span.date-comment a").first().ownText().trim();
				comment.setTime(time);

				String url = element.select("span.date-comment a").first().attr("href");
				comment.setUrl(url);

				Elements tagElements = element.getElementsMatchingOwnText("标　　签：");
				if (!tagElements.isEmpty()) {
					String tag = tagElements.first().siblingElements().first().text();
					comment.setTag(tag);
				} else {
					comment.setTag("none");
				}

				String text = element.getElementsMatchingOwnText("心　　得：").first().siblingElements().first().ownText();
				comment.setText(text);

				String itemType = element.getElementsMatchingOwnText("颜　　色：").first().siblingElements().first().ownText();
				comment.setItemType(itemType);

				String purchaseDate = element.getElementsMatchingOwnText("购买日期：").first().siblingElements().first().ownText();
				comment.setPurchaseDate(purchaseDate);

				this.log.debug(comment.toString());
				comments.add(comment);
			} catch (Exception e) {
				this.log.error("解析错误", e, element.html());
			}
		}

		return comments;
	}

	/**
	 * 
	 * @param goodsId
	 * @param type
	 *            好评3 中评2 差评1 图片
	 * @param i
	 * @return
	 */
	public String buildUrl(String goodsId, String type, int i) {
		String baseUrl = "http://club.jd.com/review/{0}-3-{2}-{1}.html";
		return MessageFormat.format(baseUrl, goodsId, type, i + 1 + "");
	}

	public static void main(String[] args) throws Exception {
		// http://item.jd.com/1152512254.html
		new JDCommentFetch("1152512254", "1").run();
//		 new JDCommentFetch("1152512254", "2").run();
//		 new JDCommentFetch("1152512254", "3").run();
		// new Comment("695467", "1").run();
	}
}
