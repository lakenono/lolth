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
		cookieList.add("_T_WM=ac0612a664838b72b7d8662f0df280c1; SUB=_2A254ews9DeSRGeNL7FUR-C7NyjqIHXVbh5V1rDV6PUJbrdAKLWKjkW2Ff_rOGAIhis2WmvGEDWxEFsFIfg..; gsid_CTandWM=4u3ee42b175MiIfztGzkZnoQU1S; M_WEIBOCN_PARAMS=from%3Dhome");
		cookieList.add("_T_WM=d0d1e55c38772164698110133145a81c; SUB=_2A254ewzqDeSRGeNL7FQS9inIyj-IHXVbh5SirDV6PUJbrdAKLRbbkW2KktXXSiCCEPger3MAtLGkuOO2sA..; gsid_CTandWM=4ueEe42b1ArRJkT34m8yVnoFp6F");
		cookieList.add("_T_WM=437f0da84da2c5d37944acade4a3f4c1; SUB=_2A254e7f_DeSRGeNL7FQW8CnIyjqIHXVbh9m3rDV6PUJbrdANLXPBkW1_l2tTIH07_6CFc5FaFyzWtB914w..; gsid_CTandWM=4uEpe42b1EBvPOzjXYqnInoKT6I");
		cookieList.add("_T_WM=217fecb7b067dd37173163c7c57432e0; SUB=_2A254e7tWDeTxGeRP6VQV8CvLzD2IHXVbh8UerDV6PUJbrdAKLWfekW1fkfvSFyZV9sMTeRi9a5XmTWYfQQ..; gsid_CTandWM=4u8te42b1P83L7DWZS8Mn8VaRcr");
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
