package lolth.weibo.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class WeiboAuthManagerImpl implements WeiboAuthManager {
	private static int i=0;

	private List<Map<String, String>> cookies = new ArrayList<>();

	public WeiboAuthManagerImpl() {

		List<String> cookieList = new ArrayList<>();
		cookieList.add("_T_WM=217fecb7b067dd37173163c7c57432e0; SUB=_2A254j7EDDeSRGeNL7FQS9inIyj-IHXVYc99LrDV6PUJbrdANLW3RkW2MshPPQzL-TqeJKXv7AjBGfJTf1A..; gsid_CTandWM=4ueEe42b1ArRJkT34m8yVnoFp6F; M_WEIBOCN_PARAMS=from%3Dhome");
		cookieList.add("_T_WM=fbb39b7e8664568f9afdce522bd67d6e; SUB=_2A254j7G-DeSRGeNL7FUR-C7NyjqIHXVYc9_2rDV6PUJbrdAKLVj5kW2Prdqzts7q9sIKvJqYFjy8xAACkQ..; gsid_CTandWM=4uB9e42b1SYb6wh6HryDynoQU1S");
		cookieList.add("_T_WM=975dae811d515cdb754fc4fbad945fa4; SUB=_2A254j7IWDeSRGeNL7FQW8CnIyjqIHXVYc95erDV6PUJbrdAKLUXckW01KY575dbQL-XEK4nb_GIIkK-I4w..; gsid_CTandWM=4uEpe42b1EBvPOzjXYqnInoKT6I");
		for (String cookieStr : cookieList) {
			Map<String, String> cookieMap = parseCookieStr(cookieStr);
			if (!cookieMap.isEmpty()) {
				cookies.add(cookieMap);
			}
		}
	}

	@Override
	public Map<String, String> getAuthInfo() {
		i++;
		if(i==cookies.size()){
			i=0;
		}
		return cookies.get(i);
	}

	public Map<String, String> parseCookieStr(String cookieStr) {
		Map<String, String> cookies = new HashMap<>();
		String[] cookiePairs = StringUtils.splitByWholeSeparator(cookieStr, ";");
		for (String cookiePair : cookiePairs) {
			if (StringUtils.contains(cookiePair, "=")) {
				String[] kv = StringUtils.splitByWholeSeparator(cookiePair, "=");
				cookies.put(kv[0], kv[1]);
			}
		}
		return cookies;
	}
	
	public static void main(String[] args) {
		WeiboAuthManager manager = new WeiboAuthManagerImpl();
		
		for(int i=0;i<10;i++){
			String s = manager.getAuthInfo().get("_T_WM");
			System.out.println(s);
		}
	}

}
