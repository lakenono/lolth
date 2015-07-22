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
		cookieList.add("_T_WM=e4f79fb749ba76fd65d21f77375b3bee; SUHB=0YhAWIMB0if4Zt; SUB=_2A254qyyUDeSRGeNL7FQW8CnIyjqIHXVYV7TcrDV6PUJbrdAKLRTMkW1bUb-L_6T22NIAxChvSf03zXfIGQ..; gsid_CTandWM=4uK6e42b1h1Iy7I2qgstfnoKT6I; M_WEIBOCN_PARAMS=from%3Dhome");
		cookieList.add("_T_WM=16e9d6f2259232ed48557e9a73f10ca7; SUB=_2A254qy3XDeSRGeNL7FUR-C7NyjqIHXVYV7OfrDV6PUJbrdAKLUjHkW19ani85lDmmYsLajtvQCvnrAAabw..; gsid_CTandWM=4uB9e42b1SYb6wh6HryDynoQU1S");
		cookieList.add("_T_WM=381052f5df15a47db4b6c216d9fa6b8e; SUB=_2A254qy2qDeSRGeNL7FQS9inIyj-IHXVYV7PirDV6PUJbrdANLVPhkW1Mx5Pwf3qtPcXl9Bixn6Md_eO72Q..; gsid_CTandWM=4uDre42b1a7eMv2kMnqKPnoFp6F");
		
		cookieList.add("_T_WM=e69827b94485b12ee98f92def5728c0c; SUB=_2A254qy7EDeTxGeNI6lEQ8CzJwjSIHXVYV7KMrDV6PUJbrdANLXnXkW1Uziu1Q9JEkOM--KWH3u7dEi6nGg..; gsid_CTandWM=4us2e42b1W2namdm2TPETnydU9E");
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
