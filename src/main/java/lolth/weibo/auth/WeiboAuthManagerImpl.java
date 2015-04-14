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
		cookieList.add("_T_WM=eb041eac5d17c392db80bf728f5e6812; SUB=_2A254GnYfDeTRGeNL7FUR-C7NyjqIHXVb5RpXrDV6PUJbrdANLUvfkW1h-nB2KYcuvJsVEPzQKwRi4l1lXw..; gsid_CTandWM=4uXk655b1aQhSq6qGa8tenoQU1S");
		cookieList.add("_T_WM=bddb135cdfd413617639ece69fe03cbb; SUB=_2A254GnbhDeTRGeNL7FQS9inIyj-IHXVb5RqprDV6PUJbrdAKLRKnkW1yHAqXwgf_JYfB_y4EdkzCPa0uVw..; gsid_CTandWM=4uHp655b1tBubHYB1pSeFnoFp6F");
		cookieList.add("_T_WM=285efbcca8a562aabc6f93b7cc034c76; SUB=_2A254GnawDeTRGeNL7FQW8CnIyjqIHXVb5Rr4rDV6PUJbrdANLRfykW0-DEgG26pTkFqArdeUuVwYg8PW5w..; gsid_CTandWM=4uOi655b1lXV1viJVMwzAnoKT6I");
		
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
