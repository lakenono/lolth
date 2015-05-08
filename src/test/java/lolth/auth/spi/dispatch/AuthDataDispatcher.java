package lolth.auth.spi.dispatch;

import lolth.auth.spi.bean.AuthBean;

public interface AuthDataDispatcher {

	/**
	 * 调度
	 * 
	 * @return
	 */
	public AuthBean dispatch(String domain, String clientIp);

}
