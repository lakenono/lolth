package lolth.weibo.fetcher;

import java.io.IOException;

import lolth.weibo.auth.WeiboAuthManager;
import lolth.weibo.auth.WeiboAuthManagerImpl;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class WeiboFetcher {
	private WeiboAuthManager authManager = new WeiboAuthManagerImpl();

	public static WeiboFetcher cnFetcher;

	static {
		cnFetcher = new WeiboFetcher();
	}


	public Document fetch(String url) throws IOException, InterruptedException {
		Connection connect = Jsoup.connect(url);

		// cookie
		connect.cookies(authManager.getAuthInfo());

		// ua
		connect.userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.timeout(1000 * 5);

		int retry = 5;

		for (int i = 1; i <= retry; i++) {
			try {
				// 休眠时间
//				Thread.sleep(15 * 1000);
				return connect.post();
			} catch (java.net.SocketTimeoutException e) {
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			} catch (java.net.ConnectException e) {
				log.error("SocketTimeoutException [1]秒后重试第[{}]次..", i);
				Thread.sleep(1000);
			}

		}
		throw new RuntimeException("fetcher重试[" + retry + "]次后无法成功.");
	}
	
	public static void main(String[] args) throws Exception{
		String url = "http://weibo.cn/tingtingmusicbox?page=2&vt=4";
		System.out.println(WeiboFetcher.cnFetcher.fetch(url));
	}

}
