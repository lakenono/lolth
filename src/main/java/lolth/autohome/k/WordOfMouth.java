package lolth.autohome.k;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.autohome.k.bean.WordOfMouthBean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 用户名
// 用户url
// 点评帖子url
// 点评帖子title
// 用户认证车型
// 购买车辆
// 购买地点
// 购车经销商
// 购买时间
// 裸车购买价
// 油耗
// 目前行驶 
// 评分-空间
// 评分-动力
// 评分-操控
// 评分-油耗
// 评分-舒适性
// 评分-外观
// 评分-内饰
// 评分-性价比
// 购车目的
// 发帖时间
// 帖子内容
// 浏览次数
// 点赞
public class WordOfMouth
{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String id;

	public WordOfMouth(String id)
	{
		super();
		this.id = id;
	}

	public void run() throws Exception
	{
		int maxPage = this.getMaxPage();
		this.log.info("start job 0/{}", maxPage);

		for (int i = 0; i < maxPage; i++)
		{
			this.log.info("start job {}/{}", i + 1, maxPage);

			String url = this.buildUrl(this.id, i);
			String html = GlobalComponents.dynamicFetch.fetch(url);
			List<WordOfMouthBean> beans = this.parse(html);

			for (WordOfMouthBean wordOfMouthBean : beans)
			{
				wordOfMouthBean.persist();
			}
		}
	}

	private List<WordOfMouthBean> parse(String html)
	{
		List<WordOfMouthBean> result = new LinkedList<WordOfMouthBean>();

		Document document = Jsoup.parse(html);

		Elements elements = document.select("div.mouthcon.js-koubeidataitembox");
		for (Element element : elements)
		{
			try
			{
				WordOfMouthBean bean = new WordOfMouthBean();

				// 用户名
				String username = element.select("div.name-text p a").first().ownText();
				bean.setUsername(username);

				// 用户url
				String userUrl = element.select("div.name-text p a").first().attr("href");
				bean.setUserUrl(userUrl);

				// 无帖子信息
				if (element.select("div.usercont-main.fn-clear div.main-text").first().ownText().equals("--"))
				{
					bean.setSourceUrl("none");
					bean.setSourceTitle("none");
				}
				else
				{
					// 帖子url
					String sourceUrl = element.select("div.usercont-main.fn-clear div.main-text p.text-list a").first().attr("href");
					bean.setSourceUrl(sourceUrl);

					// 帖子title
					String sourceTitle = element.select("div.usercont-main.fn-clear div.main-text p.text-list a").first().ownText();
					bean.setSourceTitle(sourceTitle);
				}

				// 无认证车
				if (element.getElementsMatchingOwnText("认证的车：").size() == 0)
				{
					bean.setAuthCar("none");
				}
				else
				{
					String authCar = element.getElementsMatchingOwnText("认证的车：").first().siblingElements().first().text();
					bean.setAuthCar(authCar);
				}

				// 购买车型
				String carType = element.getElementsMatchingOwnText("购买车型").first().siblingElements().first().text();
				bean.setCarType(carType);

				// 购买地点
				String purchasedFrom = element.getElementsMatchingOwnText("购买地点").first().siblingElements().first().text();
				bean.setPurchasedFrom(purchasedFrom);

				// 购车经销商
				if (element.getElementsMatchingOwnText("购车经销商").size() == 0)
				{
					bean.setDealer("none");
				}
				else
				{
					String dealer = element.getElementsMatchingOwnText("购车经销商").first().siblingElements().first().text();
					bean.setDealer(dealer);
				}

				// 购买时间
				String buyTime = element.getElementsMatchingOwnText("购买时间").first().siblingElements().first().text();
				bean.setBuyTime(buyTime);

				// 裸车购买价
				String price = element.getElementsMatchingOwnText("裸车购买价").first().siblingElements().first().text();
				bean.setPrice(price);

				// 油耗 目前行驶
				if (element.getElementsMatchingOwnText("升/百公里").size() == 0)
				{
					bean.setFuelConsumption("none");
					bean.setKilometre("none");
				}
				else
				{
					String fuelConsumption = element.getElementsMatchingOwnText("升/百公里").first().parent().ownText();
					bean.setFuelConsumption(fuelConsumption);

					String kilometre = element.getElementsMatchingOwnText("公里").first().parent().ownText();
					bean.setKilometre(kilometre);
				}

				// 评分
				Elements gradeElements = element.select("div.position-r");
				for (Element gradeElement : gradeElements)
				{
					// 0
					if (gradeElement.getElementsMatchingOwnText("空间").size() != 0)
					{
						String interspaceGrade = gradeElement.getElementsMatchingOwnText("空间").first().siblingElements().first().text();
						bean.setInterspaceGrade(interspaceGrade);
					}

					// 1
					if (gradeElement.getElementsMatchingOwnText("动力").size() != 0)
					{
						String powerGrade = gradeElement.getElementsMatchingOwnText("动力").first().siblingElements().first().text();
						bean.setPowerGrade(powerGrade);
					}

					// 2
					if (gradeElement.getElementsMatchingOwnText("操控").size() != 0)
					{
						String manipulationGrade = gradeElement.getElementsMatchingOwnText("操控").first().siblingElements().first().text();
						bean.setManipulationGrade(manipulationGrade);
					}

					// 3
					if (gradeElement.getElementsMatchingOwnText("油耗").size() != 0)
					{
						String fuelConsumptionGrade = gradeElement.getElementsMatchingOwnText("油耗").first().siblingElements().first().text();
						bean.setFuelConsumptionGrade(fuelConsumptionGrade);
					}

					// 4
					if (gradeElement.getElementsMatchingOwnText("舒适性").size() != 0)
					{
						String comfortGrade = gradeElement.getElementsMatchingOwnText("舒适性").first().siblingElements().first().text();
						bean.setComfortGrade(comfortGrade);
					}

					// 5
					if (gradeElement.getElementsMatchingOwnText("外观").size() != 0)
					{
						String appearanceGrade = gradeElement.getElementsMatchingOwnText("外观").first().siblingElements().first().text();
						bean.setAppearanceGrade(appearanceGrade);
					}

					// 6
					if (gradeElement.getElementsMatchingOwnText("内饰").size() != 0)
					{
						String innerDecorationGrade = gradeElement.getElementsMatchingOwnText("内饰").first().siblingElements().first().text();
						bean.setInnerDecorationGrade(innerDecorationGrade);
					}

					// 7
					if (gradeElement.getElementsMatchingOwnText("性价比").size() != 0)
					{
						String performancePriceGrade = gradeElement.getElementsMatchingOwnText("性价比").first().siblingElements().first().text();
						bean.setPerformancePriceGrade(performancePriceGrade);
					}

				}

				// 购车目的
				String aims = element.getElementsMatchingOwnText("购车目的").first().siblingElements().first().text();
				bean.setAims(aims);

				// 发帖时间
				String publishTime = element.select("span.time").first().text();
				publishTime = StringUtils.replace(publishTime, " 发表", "");
				bean.setPublishTime(publishTime);

				// 帖子内容
				if (element.select("div.text-con.height-list div.text-cont").size() == 0)
				{
					bean.setText("none");
				}
				else
				{
					String text = element.select("div.text-con.height-list div.text-cont").first().text();
					bean.setText(text);
				}

				// 浏览次数
				String views = element.getElementsMatchingOwnText("人看过").first().child(0).text();
				bean.setViews(views);

				// 点赞
				String likes = element.getElementsMatchingOwnText("人支持该口碑").first().child(0).text();
				bean.setLikes(likes);

				result.add(bean);
			}
			catch (Exception e)
			{
				this.log.error("解析错误...", e);
			}
		}

		return result;
	}

	private int getMaxPage() throws Exception
	{
		String url = this.buildUrl(id, 1);
		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);
		String ownText = document.select("span.page-item-info").first().ownText();
		String pageNum = StringUtils.substringBetween(ownText, "共", "页");
		return Integer.parseInt(pageNum);
	}

	public String buildUrl(String id, int page)
	{
		String baseUrl = "http://k.autohome.com.cn/{0}/index_{1}.html";
		return MessageFormat.format(baseUrl, id, page + 1 + "");
	}

	public static void main(String[] args) throws Exception
	{
		//东风风度MX6 3637 -- 目前没有口碑
		//new WordOfMouth("3637").run();

		// 哈弗H6 2123 http://club.autohome.com.cn/bbs/forum-c-2123-1.html
		new WordOfMouth("2123").run();

		// 奔腾X80 3000 http://club.autohome.com.cn/bbs/forum-c-3000-1.html
		new WordOfMouth("3000").run();

		// 长安CS75 3204 http://club.autohome.com.cn/bbs/forum-c-3204-1.html
		new WordOfMouth("3204").run();

		// 传祺GS5 2560 http://club.autohome.com.cn/bbs/forum-c-2560-1.html
		new WordOfMouth("2560").run();
	}
}
