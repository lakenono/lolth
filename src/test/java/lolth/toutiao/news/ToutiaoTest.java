package lolth.toutiao.news;

import java.io.IOException;
import java.sql.SQLException;

import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;

public class ToutiaoTest {

	@Test
	public void test() throws IOException, InterruptedException,Exception{
		String url = "http://toutiao.com/search/?keyword=oppo";
		url = "http://toutiao.com/search_content/?offset=20&format=json&keyword=oppo&autoload=true&count=20&_="+System.currentTimeMillis();
//		url = "http://toutiao.com/search_content/?offset=180&format=json&keyword=oppo&autoload=true&count=20&_="+System.currentTimeMillis();
		HttpFetcher fetcher = new HttpClientFetcher();
		byte[] run = fetcher.run(url);
		String json = new String(run);
		JSONObject parseObject = JSON.parseObject(json);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		System.out.println(jsonArray.size());
		for(int i = 0;i<jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String abstractss = jsonObject.getString("abstract");
			System.out.println(abstractss);
			String datetime = jsonObject.getString("datetime");
			System.out.println(datetime);
			String keywords = jsonObject.getString("keywords");
			System.out.println(keywords);
			String title = jsonObject.getString("title");
			System.out.println(title);
			String bury_count = jsonObject.getString("bury_count");
			System.out.println(bury_count);
			String digg_count = jsonObject.getString("digg_count");
			System.out.println(digg_count);
			String seo_url = jsonObject.getString("seo_url");
			System.out.println(seo_url);
			String lurl = jsonObject.getString("url");
			System.out.println(lurl);
			String id = jsonObject.getString("id");
			System.out.println(id);
			System.out.println("-----------------------------------");
		}
	}
	
	@Test
	public void testTask() throws Exception{
		String url ="http://toutiao.com/a4298223260/";
//		String id = url.substring(url.lastIndexOf("a"), url.lastIndexOf("/"));
//		System.out.println(id);
		String fetch = GlobalComponents.fetcher.fetch(url);
		System.out.println("-->"+fetch);
		ArticleTextExtractor extractor = new ArticleTextExtractor();
		System.out.println("-------------------------");
		JResult content = extractor.extractContent(fetch);
		System.out.println("title-->"+content.getTitle());
		System.out.println("text-->"+content.getText());
	}
}
