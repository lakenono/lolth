package lolth.auth.spi.login.client;

import lolth.auth.spi.bean.AuthBean;

/**
 * 登录客户端
 * 
 * @author shilei
 * 
 */
public interface LoginClient {

	/**
	 * 登陆
	 * 
	 * @param authBean
	 * @return 登陆成功，返回带认证信息的authBean，登陆失败返回
	 */
	public AuthBean login(AuthBean authBean) throws Exception;

	/**
	 * 关闭
	 */
	public void shutdown() throws Exception;
}
