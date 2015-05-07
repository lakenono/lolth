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

	public static final String[] cookies = { "BAIDUID=CE512F8A7E449498E7983F5160DDC0EF:FG=1; BDUSS=21wV2RGS1Z1TnZHb0JMQmk1ZFhUZlVCaG4zd1czZkJOazM4Q2Z4MWdINzlXbkpWQUFBQUFBJCQAAAAAAAAAAAEAAACIjIxhz9S640hlcnJ5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP3NSlX9zUpVZ; Hm_lvt_6859ce5aaf00fb00387e6434e4fcc925=1430965798; Hm_lpvt_6859ce5aaf00fb00387e6434e4fcc925=1430966341; PMS_JT=%28%7B%22s%22%3A1430966344027%2C%22r%22%3A%22http%3A//zhidao.baidu.com/search%3Fword%3D%25BB%25DD%25CA%25CF%25C6%25F4%25B8%25B3%26lm%3D0%26site%3D-1%26sites%3D0%26date%3D4%26ie%3Dgbk%22%7D%29" };

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
}
