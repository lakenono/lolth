package lolth.weibo.auth;

import java.util.Map;

/**
 * 登录管理器
 * 
 * @author shilei
 * 
 */
public interface WeiboLogin {
	
	
	/**
	 * 登录
	 * 
	 * @return cookie信息
	 * @throws Exception
	 */
	public Map<String,String> login() throws Exception;
}
