package lolthx.yhd.fetch;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lakenono.base.DistributedParser;
import lakenono.base.Queue;
import lakenono.base.Task;
import lolthx.yhd.bean.GoodsBean;
import lolthx.yhd.task.YhdSearchProduce;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 一号店商品详情爬取，并为每个商品创建n个评论task和1个问答ask
 * 抓取类型是json_fetch
 * @author yanghp
 *
 */
@Slf4j
public class YHDGoodsFetch extends DistributedParser {

	public final String commentUrl = "http://club.yhd.com/review/{0}-{1}.html#productExperience";
	public final String askUrl = "http://item-home.yhd.com/item/ajax/ajaxDetailViewQA.do?product.id={0}&pmId={1}&questionPamsMode.questiontype=0&pagenationVO.currentPage=1";
	public static final String COMQUE = "yhd_comment_queue";
	public static final String ASKQUE = "yhd_ask_queue";

	@Override
	public String getQueueName() {
		return YhdSearchProduce.QUEUENAME;
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		List<GoodsBean> beans = new ArrayList<GoodsBean>();
		JSONObject parseObject = JSON.parseObject(result);
		Object object = parseObject.get("value");
		Document doc = Jsoup.parse((String) object);
		Elements eles = doc.select("li.search_item");
		for (Element element : eles) {
			GoodsBean bean = new GoodsBean();
			Elements select = element.select("div a.search_prod_img");
			String url = select.first().attr("href");
			String id = select.first().attr("pmid");
			String name = select.first().select("img").attr("alt");
			// 如果为0 或者空则找productid
			String parentId = select.first().attr("parentid");
			Elements ele = element.select("div.pricebox span");
			// 价格
			String price = ele.first().attr("yhdprice");
			String productid = ele.first().attr("productid");

			Elements comment = element.select("div p.comment");
			// 评论个数，如果不为0则发送task抓取商品评论，商品评论页数根据评论数定
			String commments = comment.first().attr("experiencecount");
			// 评分
//			String strong = comment.first().select("span").text().substring(1);
			String strong = element.select("span.positiveRatio").attr("title");
			String shop = element.select("div.owner").text();

			bean.setGoodsId(id);
			bean.setUrl(url);
			bean.setName(name);
			bean.setParentId(parentId);
			bean.setProductid(productid);
			bean.setPrice(price);
			bean.setShop(shop);
			bean.setStrong(strong);
			bean.setCommments(commments);
			bean.setKeyword(task.getProjectName());
			beans.add(bean);
			// 如果评论个数不为0，则创建评论爬取task
			try {
				int nums = Integer.parseInt(commments);
				if (nums > 0) {
					int page = (nums % 10 > 0 ? nums / 10 + 1 : nums / 10);
					log.info("this task is {}, and total page is {}", id, page);
					// 每页一个task
					for (int p = 1; p < page; p++) {
						String comUrl = MessageFormat.format(commentUrl,
								parentId != "0" ? productid : parentId, p);
						Task t = new Task();
						// 工程名定义成商品id
						t.setProjectName(id);
						t.setUrl(comUrl);
						t.setQueueName(COMQUE);
						Queue.push(t);
					}
				}
				
			} catch (Exception e) {
				log.error("goods id is {} send comment task error!",id);
			}
			// 创建问答任务队列 只发送一个问答task
			try {
				Task ask = new Task();
				ask.setQueueName(ASKQUE);
				ask.setProjectName(id);
				ask.setUrl(MessageFormat.format(askUrl, productid, id));
				Queue.push(ask);
				
			} catch (Exception e) {
				log.error("goods id is {} send ask task error!",id);
			}
		}
		log.info("fetch list:" + beans.size());
		if (!beans.isEmpty()) {
			for (GoodsBean bean : beans) {
				if (!bean.exist()) {
					bean.persist();
				}
			}
		}
		beans.clear();

	}

}
