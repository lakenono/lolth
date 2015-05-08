package lolth.auth.spi;

import java.util.Map;

/**
 * 
 * @author shi.lei
 *
 */
public interface AuthManager {
	
	/**
	 * 获取认证信息:后台会根据IP分配使用
	 * 
	 * @return 认证信息，通常是cookies，如果 没有认证信息，返回null
	 */
	public Map<String,String> getAuthData(String domain,String clientIp);
	
	
	/**
	 * 认证失效，获取新的认证数据
	 */
	public Map<String,String> authFailure(String domain,String clientIp);
	
}
