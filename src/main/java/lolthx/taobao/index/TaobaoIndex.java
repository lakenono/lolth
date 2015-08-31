package lolthx.taobao.index;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import lakenono.core.GlobalComponents;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpResponse;
import lakenono.util.UrlUtils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TaobaoIndex {

	private String cookie = "thw=cn; cna=hX8qDqi62S0CAT3oC8XN4Bat; sc4=1; v=0; _tb_token_=pMDCwLBragGGW49; uc1=lltime=1437360104&cookie14=UoW0FUqB3c2vJQ%3D%3D&existShop=false&cookie16=URm48syIJ1yk0MX2J7mAAEhTuw%3D%3D&cookie21=VFC%2FuZ9ainBZ&tag=7&cookie15=URm48syIIVrSKA%3D%3D&pas=0; uc3=nk2=G4mkdrsA0Z8TNdQ%3D&id2=UNaBQmidTMs%3D&vt3=F8dAT%2B%2BN6F6f8PQYDCc%3D&lg2=URm48syIIVrSKA%3D%3D; existShop=MTQzNzM3ODQ3NA%3D%3D; lgc=xierenjing5; tracknick=xierenjing5; sg=513; cookie2=1cee9f84161e7e12bacf57d6b33d849f; mt=np=&ci=1_1&cyk=-1_-1; cookie1=URonBYJhPhy%2BnHbrmgxkyi0dQK%2FFan05LAR4tJ5C8J4%3D; unb=36648661; t=cc0aa8f066f46e0d070987c30f7c8fb8; publishItemObj=Ng%3D%3D; _cc_=VFC%2FuZ9ajQ%3D%3D; tg=0; _l_g_=Ug%3D%3D; _nk_=xierenjing5; cookie17=UNaBQmidTMs%3D; sc1=s%3A9nMjScSGMDFEjncv7q2brLzz.tcLQQO2o%2FMhbp8bhtuLXyJLsYAX5AddQFPe2RJe6R%2B4; sc8=3; isg=8DAE394F01EB31E2FE590F03A3111A9F; l=Anx8iQnwm0y5nWTTBu3tezluzBQvTyCf; sc5=127UW5TcyMNYQwiAiwQRHhBfEF8QXtHcklnMWc%3D%7CUm5Ockt1T3FEfERwRH9LciQ%3D%7CU2xMHDJ7G2AHYg8hAS8WKgQkCk0kT2E3YQ%3D%3D%7CVGhXd1llXGJYZlNrU2dTaF1iVWhKcEtzTnBFeEN6TnJMeUZ%2FQG44%7CVWldfS0RMQ02DTYWKhAwHmMCZBh1QzdWOBwjBXlEOgo6FEIU%7CVmNDbUMV%7CV2NDbUMV%7CWGZGFikXNwoqESkXNwg0CioWKRwhATQJNBQoFyIfPwU6D1kP%7CWWBdYEB9XWJCfkd7W2VdZ0d%2FS2tRaUl2IA%3D%3D; sc7=1";

	private Map<String, String> mapCookie = null;

	private String keyword;

	private int day;

	public TaobaoIndex(String keyword, int day) {
		this.keyword = keyword;
		this.day = day;
		init();
	}

	public void init() {
		parseCookieStr(cookie);
	}

	private void parseCookieStr(String value) {
		String[] cookiePairs = StringUtils.splitByWholeSeparator(value, ";");
		mapCookie = new HashMap<String, String>(cookiePairs.length);
		for (String cookiePair : cookiePairs) {
			if (StringUtils.contains(cookiePair, "=")) {
				String[] kv = StringUtils.splitByWholeSeparator(cookiePair, "=");
				mapCookie.put(kv[0], kv[1]);
			}
		}
	}

	public void run() throws Exception {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat formatter_shuzi = new SimpleDateFormat("yyyyMMdd");

		String url = "http://shu.taobao.com/trendindex.json?query=" + UrlUtils.encode(keyword) + "&type=query&from=1&to=0&_=" + System.currentTimeMillis();
		System.out.println(url);
		HttpRequest request = new HttpRequest(url);
		request.setCookies(mapCookie);
		HttpResponse run = GlobalComponents.jsonFetch.run(request);
		String context = new String(run.getContent(), run.getCharset() == null ? "utf-8" : run.getCharset());
		JSONObject jsonObject = JSON.parseObject(context).getJSONArray("details").getJSONObject(0);
		JSONArray jsonArray = jsonObject.getJSONArray("trend");
		int size = jsonArray.size();
		int count = size - day;
		for (int i = count; i < size; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, i + 1 - size);
			String mdatetimeshuzi = formatter_shuzi.format(cal.getTime());
			System.out.println(keyword+":"+mdatetimeshuzi + ":" + jsonArray.getString(i));
		}
	}

	public static void main(String[] args) {
		try {
			new TaobaoIndex("花千骨", 20).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
