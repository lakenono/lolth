package lolth.weibo.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lakenono.auth.AuthServiceClient;
import lombok.extern.slf4j.Slf4j;

import org.apache.thrift.transport.TTransportException;

@Slf4j
public class WeiboAuthServiceImpl {
	private String ip;
	private AuthServiceClient authService;

	public WeiboAuthServiceImpl(String server, int port) throws TTransportException, UnknownHostException {
		authService = new AuthServiceClient(server, port);
		ip = getIp();
	}

	private String getIp() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr.getHostAddress().toString();
	}

	public Map<String, String> getAuthInfo() {
		try {
			return authService.getAuthData("weibo.cn", ip);
		} catch (Exception e) {
			log.error("authService error : ", e);
		}
		return null;
	}

	public void close() {
		authService.close();
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10000; i++) {
			WeiboAuthServiceImpl authManager = new WeiboAuthServiceImpl("127.0.0.1", 9090);
			Map<String, String> cookies = authManager.getAuthInfo();
			if (cookies == null) {
				System.out.println("no cookies");
			}
			Set<Entry<String, String>> entrySet = cookies.entrySet();
			for (Entry<String, String> e : entrySet) {
				System.out.println(e.getKey() + " | " + e.getValue());
			}

			// authManager.clone();
		}

	}
}
