package lolth.auth.spi.login;

import lolth.auth.spi.bean.AuthBean;
import lolth.auth.spi.login.client.LoginClient;
import lolth.auth.spi.login.select.LoginClientSelector;
import lolth.auth.spi.login.select.LoginClientSelectorImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 对登陆进行管理和容错
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class LoginManager {
	private LoginClientSelector loginClientSelector;

	public LoginManager() {
		loginClientSelector = new LoginClientSelectorImpl();
	}

	public AuthBean login(AuthBean authBean) {
		// 登陆器选择
		LoginClient loginClient = loginClientSelector.select(authBean.getDomain());
		if (loginClient == null) {
			throw new RuntimeException("domain : " + authBean.getDomain() + " has no login client!");
		}

		// 登陆,5次机会
		for (int i = 0; i < 5; i++) {
			try {
				authBean = loginClient.login(authBean);

				authBean.updateAuthData();
				
				log.info("login success : " + authBean);
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
				log.warn("{} login retry 【{}】 ", authBean.getDomain(),i,e);
			}
		}
		
		// 返回
		return authBean;
	}
}
