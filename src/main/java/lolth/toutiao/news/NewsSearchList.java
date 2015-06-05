package lolth.toutiao.news;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Queue;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolth.toutiao.news.bean.TouTiaoNewsBean;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Slf4j
public class NewsSearchList extends Producer {

	private static final int NEWS_COUNT = 20;

	private static final String NEWS_URL = "http://toutiao.com/search_content/?offset={0}&format=json&keyword={1}&autoload=true&count={2}&_=";

	private String searchKey;

	private String searchKeyCord;

	public NewsSearchList(String projectName, String searchKey) {
		super(projectName);
		this.searchKeyCord = this.searchKey = searchKey;

		// TODO 封装url编码工具类
		try {
			this.searchKeyCord = URLEncoder.encode(searchKey, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getQueueName() {
		return "toutiao_news_search_list";
	}

	@Override
	protected int parse() throws Exception {

		int offset = 0;
		String json = null;
		byte[] context;
		JSONArray jsonArray;
		JSONObject jsonObject;
		TouTiaoNewsBean newsBean = null;
		do {
			String buildUrl = buildUrl(offset) + System.currentTimeMillis();
			context = GlobalComponents.jsonFetch.run(buildUrl);
			json = new String(context);
			jsonArray = JSON.parseObject(json).getJSONArray("data");
			if (jsonArray.size() == 0) {
				offset = 0;
			} else {
				for (int i = 0; i < jsonArray.size(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					newsBean = new TouTiaoNewsBean();
					// 标题
					newsBean.setTitle(jsonObject.getString("title"));
					// 时间
					newsBean.setDatetime(jsonObject.getString("datetime"));
					// 新闻头条地址
					newsBean.setSeoUrl(jsonObject.getString("seo_url"));
					// 原始地址
					newsBean.setUrl(jsonObject.getString("url"));
					// 新闻头条id
					newsBean.setId("a" + jsonObject.getString("id"));
					// 主题
					newsBean.setProjectName(this.projectName);
					// 搜索关键字
					newsBean.setKeyword(searchKey);
					log.debug(newsBean.toString());
					boolean exist = newsBean.exist();
					if (exist) {
						continue;
					}
					// 创建抓取任务
					Task task = buildTask(jsonObject.getString("seo_url"));
					// 推送任务
					Queue.push(task);
				}
				offset = offset + NEWS_COUNT;
				Thread.sleep(3000);
			}

		} while (offset != 0);

		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(NEWS_URL, String.valueOf(pageNum), searchKeyCord, NEWS_COUNT);
	}

	@Override
	protected Task buildTask(String url) {
		Task task = super.buildTask(url);
		task.setExtra(searchKey);
		return task;
	}

	public static void main(String[] args) throws Exception {
		String projectName = "oppo";
		String[] keywords = { "oppo" };
		for (String keyword : keywords) {
			new NewsSearchList(projectName, keyword).run();
		}
	}

}
