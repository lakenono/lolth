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
		cookieList.add("_T_WM=4c8673afeeaf7be5e7dd78359b66a34b; SUB=_2A254c9tfDeTxGedH7VQS9irNwjmIHXVbn-UXrDV6PUJbrdANLVnWkW1BbC5-YMwGqGyxytEHaNTnjyUY8Q..");
		cookieList.add("_T_WM=ac0612a664838b72b7d8662f0df280c1; SUB=_2A254c90BDeTxGeRP6VQV8CvLzD2IHXVbn-NJrDV6PUJbrdAKLVDtkW2eswvsU-yAXQyByi6neN9OZ0yNVQ..; gsid_CTandWM=4uPte42b1SJdUV3UAjo528VaRcr");
		cookieList.add("_T_WM=9fd33549a75d82ecd51a238f80937e18; SUB=_2A254c90ADeTxGeNI61cW8S7FyTmIHXVbn-NIrDV6PUJbrdAKLW_ykW2IZiTXzAsI3ego2c6Fs41DeHOuVA..; gsid_CTandWM=4u4Ndbd01GuCtKJJxIUsSnwiGeV");
		
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
