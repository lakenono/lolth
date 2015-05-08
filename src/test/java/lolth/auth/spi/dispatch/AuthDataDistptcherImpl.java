package lolth.auth.spi.dispatch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lolth.auth.spi.bean.AuthBean;
import lombok.extern.slf4j.Slf4j;

/**
 * 调度认证信息
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class AuthDataDistptcherImpl implements AuthDataDispatcher {

	public AuthDataDistptcherImpl(List<AuthBean> authBeans) throws SQLException {
		init(authBeans);
	}

	private void init(List<AuthBean> authBeans) {
		for (AuthBean auth : authBeans) {
			List<AuthBean> domainAuthBeans = domainAuthBeanMap.get(auth.getDomain());

			if (domainAuthBeans == null) {
				domainAuthBeans = new ArrayList<>();
				domainAuthBeanMap.put(auth.getDomain(), domainAuthBeans);
			}

			domainAuthBeans.add(auth);
		}
	}

	/*
	 * 认证对象域名索引
	 */
	private Map<String, List<AuthBean>> domainAuthBeanMap = new HashMap<>();

	@Override
	public AuthBean dispatch(String domain, String clientIp) {
		List<AuthBean> domaninAuthBeans = domainAuthBeanMap.get(domain);

		// 域名没有配置认证信息
		if (domaninAuthBeans == null || domaninAuthBeans.isEmpty()) {
			return null;
		}

		log.debug("[dispatch] domain supoort ");

		AuthBean authBean = null;

		for (AuthBean b : domaninAuthBeans) {
			// 手动登录
			if (!b.isAutoLogin()) {
				if (b.isFailure()) {
					continue;
				}
			}

			if (b.getClients().isEmpty()) {
				authBean = b;
				break;
			}

			if (b.compareTo(authBean) <= 0) {
				authBean = b;
			}
		}

		if (authBean != null) {
			authBean.addClientIp(clientIp);
		}

		log.debug("[dispatch] authBean:{} ", authBean);
		return authBean;
	}
}
