package lolth.auth.spi.login.client.weibo;

import java.util.List;

import lakenono.fetch.adv.HttpFetcher;
import lakenono.fetch.adv.HttpRequest;
import lakenono.fetch.adv.HttpResponse;
import lakenono.fetch.adv.selenium.SeleniumHttpFetcher;
import lakenono.fetch.adv.selenium.SeleniumHttpFetcher.SeleniumWebAction;
import lolth.auth.spi.bean.AuthBean;
import lolth.auth.spi.login.client.LoginClient;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * weibo.com登陆
 * 
 * @author shi.lei
 *
 */
@Slf4j
public class ComWeiboLogin implements LoginClient {

	public static final String WEIBO_COM_LOGIN_URL = "http://weibo.com/login.php";

	private HttpFetcher httpFetcher = null;

	@Override
	public AuthBean login(AuthBean authBean) throws Exception {
		HttpRequest loginRequest = new HttpRequest(WEIBO_COM_LOGIN_URL);
		// 需要接续cookie
		loginRequest.setNeedCookies(true);
		// 不需要内容
		loginRequest.setNeedContent(false);

		// 初始化fetcher指定登录行为
		httpFetcher = new SeleniumHttpFetcher(new SeleniumWebAction() {

			@Override
			public boolean doWeb(WebDriver webDriver, HttpRequest req, HttpResponse resp) {
				// 输入用户名
				webDriver.findElement(By.cssSelector(".username > input:nth-child(1)")).sendKeys(authBean.getUsername());
				// 输入密码
				webDriver.findElement(By.cssSelector(".password > input:nth-child(1)")).sendKeys(authBean.getPassword());

				List<WebElement> pinCodeList = webDriver.findElements(By.cssSelector("div.info_list.pin_code div.inp.verify a.code img"));

				if (!pinCodeList.isEmpty()) {
					if (!StringUtils.isBlank(pinCodeList.get(0).getAttribute("src"))) {
						// 有验证码，退出不做处理
						log.info("{} login fail ,  page have verification code ", authBean.getUsername());
						return false;
					}
				}

				// 点击登录按钮
				webDriver.findElement(By.cssSelector("div.info_list:nth-child(6) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).click();

				log.info("click");

				(new WebDriverWait(webDriver, 60)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.getTitle().indexOf("我的首页") != -1;
					}
				});

				return true;
			}
		});

		HttpResponse httpResponse = httpFetcher.run(loginRequest);
		httpFetcher.close();

		authBean.setAuthData(httpResponse.getCookies());

		return authBean;
	}

	@Override
	public void shutdown() throws Exception {

	}
}
