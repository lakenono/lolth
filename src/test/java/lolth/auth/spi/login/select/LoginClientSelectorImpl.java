package lolth.auth.spi.login.select;

import java.util.HashMap;
import java.util.Map;

import lolth.auth.spi.login.client.LoginClient;
import lolth.auth.spi.login.client.weibo.CNWeiboLogin;

public class LoginClientSelectorImpl implements LoginClientSelector {

	private Map<String, LoginClient> domainLoginClientMap = new HashMap<>();

	public LoginClientSelectorImpl() {
		domainLoginClientMap.put("weibo.cn", new CNWeiboLogin());
	}

	@Override
	public LoginClient select(String domain) {
		return domainLoginClientMap.get(domain);
	}

}
