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
		cookieList.add("_T_WM=d3cbcc7112f1fe3327d7cfdbce31eb7b; SUB=_2A254ZroHDeTxGeVG6lUQ9y7MzD-IHXVbqMZPrDV6PUJbrdAKLWSlkW0n9faS04DbtFHoO_n8HN-udeawQg..; gsid_CTandWM=4u3X80661PUBPZJSpBayAg11g1b");
		
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
