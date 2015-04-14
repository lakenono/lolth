package lolth.weibo.auth;

import java.util.Map;

public interface WeiboAuthManager {
	/**
	 * 获得认证信息
	 * @return
	 */
	public  Map<String,String> getAuthInfo();
}
