package lolth.auth.spi;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lakenono.db.BaseBean;
import lolth.auth.spi.bean.AuthBean;
import lolth.auth.spi.dispatch.AuthDataDispatcher;
import lolth.auth.spi.dispatch.AuthDataDistptcherImpl;
import lolth.auth.spi.login.LoginManager;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;

/**
 * 获取认证信息
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class StandardAuthManager implements AuthManager {

	/*
	 * 缓存
	 * 
	 * 一级索引：domain 二级索引：clienIP 值：认证数据
	 */
	private Map<String, Map<String, AuthBean>> authBeanCache = new HashMap<>();

	// 调度选择器
	private AuthDataDispatcher authDataDispatcher;

	// 登录器
	private LoginManager loginManager;

	public StandardAuthManager() throws SQLException {
		List<AuthBean> authBeans = BaseBean.getAll(AuthBean.class);

		authDataDispatcher = new AuthDataDistptcherImpl(authBeans);

		loginManager = new LoginManager();
	}

	@Override
	public Map<String, String> getAuthData(String domain, String clientIp) {
		AuthBean authBean = getFromCache(domain, clientIp);

		log.debug("{},{}, get from cache ！{}", domain, clientIp, authBean);
		// 调度一个
		if (authBean == null) {
			authBean = authDataDispatcher.dispatch(domain, clientIp);
			log.debug("{},{}, domain can not dispatch auth data！ {}", domain, clientIp, authBean);
		}

		// 没有认证信息
		if (authBean == null) {
			return null;
		}

		if (MapUtils.isEmpty(authBean.getAuthData())) {
			log.debug("{},{}, login {}", domain, clientIp, authBean);
			loginManager.login(authBean);
		}

		// 回写缓存
		putCache(domain, clientIp, authBean);
		log.info("{},{}, get success {}", domain, clientIp, authBean);
		return authBean.getAuthData();
	}

	@Override
	public Map<String, String> authFailure(String domain, String clientIp) {
		AuthBean authBean = getFromCache(domain, clientIp);

		// 缓存失效，则直接重新分配
		if (authBean == null) {
			return getAuthData(domain, clientIp);
		}

		// 缓存命中
		try {
			if (authBean.isAutoLogin()) {
				// 自动登录
				authBean = loginManager.login(authBean);
				authBean.updateAuthData();

			} else {
				// 设置失效
				authBean.setFailure(true);
				authBean.updateFailure();

				// 重新分配
				authBean = authDataDispatcher.dispatch(domain, clientIp);
			}
		} catch (Exception e) {
			log.error("update db error : {}", authBean, e);
		}

		if (authBean != null) {
			return authBean.getAuthData();
		}
		return null;
	}

	private AuthBean getFromCache(String domain, String clientIp) {
		Map<String, AuthBean> ipAuthBeanCache = authBeanCache.get(domain);
		if (ipAuthBeanCache == null) {
			ipAuthBeanCache = new HashMap<>();
		}

		authBeanCache.put(domain, ipAuthBeanCache);
		return ipAuthBeanCache.get(clientIp);
	}

	private void putCache(String domain, String clientIp, AuthBean authBean) {
		if (authBean == null) {
			return;
		}

		Map<String, AuthBean> ipAuthBeanCache = authBeanCache.get(domain);
		if (ipAuthBeanCache == null) {
			ipAuthBeanCache = new HashMap<>();
		}

		ipAuthBeanCache.put(clientIp, authBean);
	}

	public static void main(String[] args) throws Exception {
		AuthManager authManager = new StandardAuthManager();
		for (int i = 0; i < 5; i++) {
			Map<String, String> cookies = authManager.getAuthData("weibo.cn", "127.0.0.1" + i);
			Set<Entry<String, String>> entrySet = cookies.entrySet();
			for (Entry<String, String> e : entrySet) {
				System.out.println(e.getKey() + " | " + e.getValue());
			}

			System.out.println("--------------------------------------------------------");
		}
	}
}
