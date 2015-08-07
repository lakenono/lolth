package lolth.baidu.fetch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lakenono.fetch.adv.utils.CookiesUtils;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class BaiduFetcher {
	public static final BaiduFetcher fetcher = new BaiduFetcher();

	private static List<Map<String, String>> cookiesCache = new ArrayList<>();

	public static final String[] cookies = { "BAIDUID=575159E9FE4DE038C9BA741AABD935CA:FG=1; TIEBAUID=cb23caae14130a0d384a57f1; TIEBA_USERTYPE=8cff9d75047f0efb1413514a; BDUSS=0tQWEZsZTFYaFhoNUFidHhQcVhjWX5OcEFybGR-enhFQmlpVFRlQ1JVQlY3T3RWQVFBQUFBJCQAAAAAAAAAAAEAAACIjIxhz9S640hlcnJ5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFVfxFVVX8RVQ" };

	static {

		for (String c : cookies) {
			Map<String, String> cookie = CookiesUtils.getCookies(c);
			if (cookie != null) {
				cookiesCache.add(cookie);
			}
		}
	}

	private static int i = 0;

	public Map<String, String> getAuthInfo() {
		i++;
		if (i == cookiesCache.size()) {
			i = 0;
		}
		return cookiesCache.get(i);
	}

	public Document document(String url) throws IOException, InterruptedException {
		Connection connect = Jsoup.connect(url);

		// cookie
		connect.cookies(getAuthInfo());

		// ua
		connect.userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connect.timeout(1000 * 5);

		int retry = 5;

		for (int i = 1; i <= retry; i++) {
			try {
				// 休眠时间
				// Thread.sleep(15 * 1000);
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

	public static void main(String[] args) throws Exception {
		String url = "http://tieba.baidu.com/f?kw=%E7%94%B5%E5%8A%A8%E8%BD%A6&ie=utf-8&pn=54750";
		System.out.println(BaiduFetcher.fetcher.document(url));
	}
}
