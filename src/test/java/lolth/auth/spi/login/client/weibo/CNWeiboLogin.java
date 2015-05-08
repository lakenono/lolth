package lolth.auth.spi.login.client.weibo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpRequestMethod;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.httpclient.HttpClientFetcher;
import lolth.auth.spi.bean.AuthBean;
import lolth.auth.spi.login.client.LoginClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;

/**
 * weibo.cn 登陆
 * 
 * @author shi.lei
 *
 */
public class CNWeiboLogin implements LoginClient {
	public static final String WAP_WEIBO_LOGIN_PAGE_URL = "http://login.weibo.cn/login/";

	public static final String WAP_WEIBO_AUTH_STRING_KEY = "gsid_CTandWM";

	private HttpFetcher httpFetcher = null;
	private String loginSubmitUrl = "";

	public CNWeiboLogin() {
		httpFetcher = new HttpClientFetcher();
	}

	// 解析登录地址，登录提交表单参数
	private Map<String, String> getLoginParams(byte[] loginPageContent, String username, String password) throws IOException {
		Document doc = Jsoup.parse(new ByteArrayInputStream(loginPageContent), "utf-8", "");
		// 获得form标签
		Elements formElements = doc.getElementsByTag("form");
		Element formElement = formElements.first();
		// 获得Form标签的action属性,拼接登录地址
		loginSubmitUrl = WAP_WEIBO_LOGIN_PAGE_URL + formElement.attr("action");

		// 解析所有的input标签
		Map<String, String> params = Maps.newHashMap();
		Elements inputElements = formElement.getElementsByTag("input");
		// 获得所有的input标签的name和value的值
		for (Element e : inputElements) {
			String name = e.attr("name");
			String value = e.attr("value");
			if (name.equals("mobile")) {
				value = username;
			} else if (name.startsWith("password")) {
				value = password;
			}
			params.put(name, value);
		}
		return params;
	}

	// 从cookies解析出认证字符串
	private Map<String, String> getAuthStr(Map<String, String> loginParams) throws Exception {
		HttpRequest loginRequest = new HttpRequest(loginSubmitUrl);
		// post请求
		loginRequest.setMethod(HttpRequestMethod.POST);
		// 需要接续cookie
		loginRequest.setNeedCookies(true);
		// 不需要内容
		loginRequest.setNeedContent(false);
		// 不需要处理302 重定向
		loginRequest.setRedirectsEnabled(false);
		// 指定请求参数
		loginRequest.setParams(loginParams);

		HttpResponse httpResponse = httpFetcher.run(loginRequest);

		return httpResponse.getCookies();
	}

	@Override
	public AuthBean login(AuthBean authBean) throws Exception {
		byte[] loginPageContent = httpFetcher.run(WAP_WEIBO_LOGIN_PAGE_URL);
		Map<String, String> loginParams = getLoginParams(loginPageContent, authBean.getUsername(), authBean.getPassword());
		Map<String, String> cookies = getAuthStr(loginParams);

		authBean.setAuthData(cookies);
		return authBean;
	}

	@Override
	public void shutdown() throws Exception{
		if (httpFetcher != null) {
			httpFetcher.close();
		}
	}

}
