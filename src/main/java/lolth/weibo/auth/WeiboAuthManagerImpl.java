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
		cookieList.add("SUB=_2A254WEGMDeSRGeNL7FUR-C7NyjqIHXVbo2_ErDV6PUJbrdAKLULskW0_SH9YmtXknJfDsykoTfSRvc4DqQ..; expires=Fri, 19-Jun-2015 07:03:56 GMT; path=/; domain=.weibo.cn; httponly gsid_CTandWM=4uHd655b1Kr8JKHGOBzBDnoQU1S; expires=Fri, 19-Jun-2015 07:03:56 GMT; path=/; domain=.weibo.cn; httponly PHPSESSID=c7cfa7548293cf273a28713391ff9cd3; path=/");
		
		cookieList.add("SUB=_2A254WF2ZDeSRGeNL7FQS9inIyj-IHXVbo2PRrDV6PUJbrdANLWPtkW0NtzNwJKak7E8bBgIfzq3l2yDnJQ..; expires=Fri, 19-Jun-2015 06:46:33 GMT; path=/; domain=.weibo.cn; httponly gsid_CTandWM=4u5Q655b1rpBKincYqiuRnoFp6F; expires=Fri, 19-Jun-2015 06:46:33 GMT; path=/; domain=.weibo.cn; httponly PHPSESSID=68b135dd3dadf39587c1b9c9abc0610f; path=/");
		
		cookieList.add("SUB=_2A254WEJHDeSRGeNL7FQW8CnIyjqIHXVbo24PrDV6PUJbrdAKLWvTkW2IV6IULxpe-gGQsRgQ5xuAO636Qg..; expires=Fri, 19-Jun-2015 07:04:55 GMT; path=/; domain=.weibo.cn; httponly gsid_CTandWM=4uOi655b1lXV1viJVMwzAnoKT6I; expires=Fri, 19-Jun-2015 07:04:55 GMT; path=/; domain=.weibo.cn; httponly PHPSESSID=1520653da0274719f80e24b270cb6f94; path=/");
		
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
